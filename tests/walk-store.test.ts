import { after, test } from 'node:test';
import assert from 'node:assert/strict';
import { DatabaseSync } from 'node:sqlite';
import { registerHooks } from 'node:module';
import type { Walk } from '../src/tracking/model.ts';

// Execute production SQL against real SQLite. Only the native bridge is adapted.
const sqlite = new DatabaseSync(':memory:');
const adapter = {
  execAsync: async (sql: string) => { sqlite.exec(sql); },
  runAsync: async (sql: string, ...args: (string | number)[]) => sqlite.prepare(sql).run(...args),
  getFirstAsync: async (sql: string, ...args: (string | number)[]) => sqlite.prepare(sql).get(...args) ?? null,
  getAllAsync: async (sql: string, ...args: (string | number)[]) => sqlite.prepare(sql).all(...args),
  withExclusiveTransactionAsync: async (fn: (value: unknown) => Promise<void>) => {
    sqlite.exec('BEGIN IMMEDIATE');
    try { await fn(adapter); sqlite.exec('COMMIT'); }
    catch (error) { sqlite.exec('ROLLBACK'); throw error; }
  },
};
(globalThis as typeof globalThis & { walkTestSQLite: typeof adapter }).walkTestSQLite = adapter;
const hooks = registerHooks({ resolve(specifier, context, nextResolve) {
  if (specifier === 'react-native') return { url: 'data:text/javascript,export const Platform = { OS: "android" };', shortCircuit: true };
  if (specifier === 'expo-sqlite') return { url: 'data:text/javascript,export const openDatabaseAsync = async () => globalThis.walkTestSQLite;', shortCircuit: true };
  return nextResolve(specifier, context);
} });
const store = await import('../src/tracking/store.ts');
const walk: Walk = { id: 'persisted', startedAt: 1000, endedAt: null, status: 'recording', mode: 'background', distance: 0, maxSpeed: 0,
  pointCount: 0, lastFix: null, lastSample: null, lastAccuracy: null, rejected: 0, gaps: 0, warning: null,
  segments: [{ id: 'segment', start: 1000, end: null, baseline: null, steps: null, stepsPartial: false, lastSample: null }] };
const point = (latitude: number, timestamp: number) => ({ latitude, longitude: 37, timestamp, accuracy: 5 });

test('durable walk lifecycle: duplicate batches, pause, resume, completion, export and deletion', async () => {
  await store.createWalk(walk);
  await assert.rejects(store.createWalk({ ...walk, id: 'duplicate' }), /Сначала/);
  await Promise.all([store.appendFixes([point(55, 1000), point(55.0001, 11000)]), store.appendFixes([point(55, 1000), point(55.0001, 11000)])]);
  let saved = (await store.getWalk(walk.id))!;
  assert.equal(saved.points.length, 2); assert.ok(saved.walk.distance > 11 && saved.walk.distance < 12);
  await store.editWalk(walk.id, value => { value.status = 'paused'; value.segments[0].end = 12000; });
  await store.appendFixes([point(56, 15000)]);
  assert.equal((await store.getWalk(walk.id))!.points.length, 2);
  await store.editWalk(walk.id, value => {
    value.status = 'recording'; value.segments.push({ ...walk.segments[0], id: 'resumed', start: 20000 });
  });
  await store.appendFixes([point(56, 20000), point(56.0001, 30000)]);
  saved = (await store.getWalk(walk.id))!;
  assert.equal(saved.points.length, 4); assert.ok(saved.walk.distance < 24);
  assert.notEqual(saved.points[1].part, saved.points[2].part);
  await assert.rejects(store.editWalk(walk.id, value => { value.distance = 999; throw new Error('disk failure'); }));
  assert.ok((await store.getWalk(walk.id))!.walk.distance < 24);
  await assert.rejects(store.deleteWalk(walk.id), /завершённую/);
  await store.editWalk(walk.id, value => { value.status = 'completed'; value.endedAt = 31000; value.segments[1].end = 31000; });
  await store.appendFixes([point(56.0002, 40000)]);
  assert.equal(await store.activeWalk(), null);
  assert.equal((await store.listWalks()).length, 1);
  assert.equal((await store.getWalk(walk.id, false))!.points.length, 4);
  await store.deleteWalk(walk.id);
  assert.equal(await store.getWalk(walk.id), null);
  assert.equal(sqlite.prepare('SELECT count(*) AS n FROM points').get()!.n, 0);
});

test('map sampling retains starts and ends of each part and full resolution for GPX', async () => {
  const many = { ...walk, id: 'long', pointCount: 7002 };
  await store.createWalk(many);
  const insert = sqlite.prepare('INSERT INTO points (walk_id, timestamp, segment_id, data) VALUES (?, ?, ?, ?)');
  sqlite.exec('BEGIN');
  for (let i = 0; i < 7002; i++) insert.run(many.id, i + 1000, 'segment', JSON.stringify({ ...point(55 + i * 0.0001, i + 1000), part: i < 3501 ? 'one' : 'two' }));
  sqlite.exec('COMMIT');
  const display = (await store.getWalk(many.id))!.points;
  assert.ok(display.length < 4000);
  for (const timestamp of [1000, 4500, 4501, 8001]) assert.ok(display.some(p => p.timestamp === timestamp));
  assert.equal((await store.getWalk(many.id, false))!.points.length, 7002);
});
after(() => { hooks.deregister(); sqlite.close(); });
