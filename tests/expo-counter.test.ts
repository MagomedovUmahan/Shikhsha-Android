import { test } from 'node:test';
import assert from 'node:assert/strict';
import { createExpoCounter } from '../src/lib/expoCounter.ts';
import { localDay } from '../src/lib/activity.ts';

function fixture(start = new Date(2026, 10, 4, 12).getTime()) {
  let time = start, raw: string | null = null, permission = 'granted', failWrite = false;
  const steps: number[] = [], queries: [number, number][] = [];
  const listeners: ((value: { steps: number }) => void)[] = [];
  const storage = { getItem: async () => raw, setItem: async (_key: string, value: string) => { if (failWrite) throw new Error('disk full'); raw = value; } };
  const api = {
    isAvailableAsync: async () => true, getPermissionsAsync: async () => ({ status: permission }), requestPermissionsAsync: async () => ({ status: permission }),
    getStepCountAsync: async (a: Date, b: Date) => { queries.push([a.getTime(), b.getTime()]); return { steps: steps.filter(t => t > a.getTime() && t <= b.getTime()).length }; },
    watchStepCount: (listener: (value: { steps: number }) => void) => { listeners.push(listener); return { remove() {} }; },
  };
  return { create: () => createExpoCounter(storage, api, () => time), at: (value: number) => { time = value; }, start, steps, queries, listeners,
    deny: () => { permission = 'denied'; }, corrupt: (value: string) => { raw = value; }, raw: () => raw, fail: () => { failWrite = true; } };
}
test('Expo iPhone counter excludes steps before enable, throughout pause and after process restart', async () => {
  const f = fixture(), c = f.create(), day = localDay(new Date(f.start));
  f.steps.push(f.start - 1000); await c.start();
  assert.equal((await c.getLiveSnapshot()).days[day], 0);
  f.steps.push(f.start + 1000); f.at(f.start + 2000); await c.stop();
  f.steps.push(f.start + 3000); f.at(f.start + 4000);
  const recovered = f.create();
  assert.equal((await recovered.getSnapshot()).days[day], 1);
  assert.equal((await recovered.getLiveSnapshot()).enabled, false);
  await recovered.start(); f.steps.push(f.start + 5000); f.at(f.start + 6000);
  assert.equal((await recovered.getSnapshot()).days[day], 2);
  assert.equal((await recovered.getSnapshot()).days[day], 2, 'refresh must be idempotent');
});
test('enabled iPhone session recovers real background history with no JS timer-generated steps', async () => {
  const f = fixture(), c = f.create(); await c.start();
  f.steps.push(f.start + 1000, f.start + 2000); f.at(f.start + 5000);
  const recovered = f.create();
  const snapshot = await recovered.getSnapshot();
  assert.equal(snapshot.days[localDay(new Date(f.start))], 2);
  assert.equal(snapshot.enabled, true);
});
test('midnight queries split into local calendar days without double counting', async () => {
  const start = new Date(2026, 10, 4, 23, 59, 50).getTime(), f = fixture(start), c = f.create();
  await c.start(); f.steps.push(start + 5000, start + 15000); f.at(start + 20000);
  const s = await c.getSnapshot();
  assert.equal(s.days['2026-11-04'], 1); assert.equal(s.days['2026-11-05'], 1);
  f.at(start + 25000); await c.stop(); f.at(start + 35000); f.steps.push(start + 30000);
  await c.start(); f.steps.push(start + 40000); f.at(start + 45000);
  assert.equal((await c.getSnapshot()).days['2026-11-05'], 2);
});
test('late subscription events cannot mutate a paused session', async () => {
  const f = fixture(), c = f.create(); await c.start(); const stale = f.listeners[0];
  f.at(f.start + 1000); await c.stop(); stale({ steps: 999 });
  assert.equal((await c.getLiveSnapshot()).days[localDay(new Date(f.start))], 0);
});
test('permission denial and storage errors do not report successful recording', async () => {
  const f = fixture(), c = f.create(); f.deny(); await assert.rejects(c.start(), /движению/);
  assert.equal((await c.getLiveSnapshot()).enabled, false);
  const other = fixture(), second = other.create(); other.fail(); await assert.rejects(second.start(), /disk full/);
  assert.equal((await second.getLiveSnapshot()).enabled, false);
});
test('corrupt saved history is rejected and left untouched', async () => {
  const f = fixture(); f.corrupt('{"enabled":true,"days":{"bad":"steps"}}'); const original = f.raw();
  await assert.rejects(f.create().getSnapshot()); assert.equal(f.raw(), original);
});
