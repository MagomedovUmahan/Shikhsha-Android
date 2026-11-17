import { Platform } from 'react-native';
import type { SQLiteDatabase } from 'expo-sqlite';
import { acceptFix, meters, usableFix, type Fix, type RoutePoint, type Walk, type WalkView } from './model.ts';

let database: Promise<SQLiteDatabase> | null = null;
async function db() {
  if (Platform.OS === 'web') throw new Error('Запись маршрута доступна в приложении на телефоне.');
  database ??= (async () => {
    const SQLite = await import('expo-sqlite');
    const value = await SQLite.openDatabaseAsync('shikhsha-walks-v1.db');
    await value.execAsync(`PRAGMA journal_mode = WAL; PRAGMA busy_timeout = 10000;
      CREATE TABLE IF NOT EXISTS walks (id TEXT PRIMARY KEY, active INTEGER NOT NULL, data TEXT NOT NULL);
      CREATE UNIQUE INDEX IF NOT EXISTS one_active_walk ON walks(active) WHERE active = 1;
      CREATE TABLE IF NOT EXISTS points (walk_id TEXT NOT NULL, timestamp REAL NOT NULL, segment_id TEXT NOT NULL, data TEXT NOT NULL, PRIMARY KEY(walk_id, timestamp));
      CREATE INDEX IF NOT EXISTS segment_points ON points(walk_id, segment_id, timestamp);`);
    return value;
  })().catch(error => { database = null; throw error; });
  return database;
}
let queue: Promise<unknown> = Promise.resolve();
function transaction<T>(fn: (tx: SQLiteDatabase) => Promise<T>): Promise<T> {
  const next = queue.catch(() => undefined).then(async () => {
    let result!: T;
    await (await db()).withExclusiveTransactionAsync(async tx => {
      await tx.execAsync('PRAGMA busy_timeout = 10000;');
      result = await fn(tx);
    });
    return result;
  });
  queue = next;
  return next;
}
type Row = { data: string };
const decode = (row: Row | null): Walk | null => row ? JSON.parse(row.data) : null;
async function write(tx: SQLiteDatabase, walk: Walk) {
  await tx.runAsync('INSERT OR REPLACE INTO walks (id, active, data) VALUES (?, ?, ?)', walk.id, walk.status === 'completed' ? 0 : 1, JSON.stringify(walk));
}
export async function activeWalk(): Promise<Walk | null> {
  if (Platform.OS === 'web') return null;
  return decode(await (await db()).getFirstAsync<Row>('SELECT data FROM walks WHERE active = 1'));
}
export async function listWalks(limit = 20, offset = 0): Promise<Walk[]> {
  if (Platform.OS === 'web') return [];
  const rows = await (await db()).getAllAsync<Row>('SELECT data FROM walks WHERE active = 0 ORDER BY rowid DESC LIMIT ? OFFSET ?', limit, offset);
  return rows.map(row => JSON.parse(row.data));
}
export async function getWalk(id: string, display = true): Promise<WalkView | null> {
  const value = await db();
  const walk = decode(await value.getFirstAsync<Row>('SELECT data FROM walks WHERE id = ?', id));
  if (!walk) return null;
  // Limit bridge/memory use for the map. Full-resolution points remain in SQLite and GPX.
  // Keep both ends of each track part when sampling so gaps never get bridged.
  const stride = display ? Math.max(1, Math.ceil(walk.pointCount / 6000)) : 1;
  const rows = stride === 1
    ? await value.getAllAsync<Row>('SELECT data FROM points WHERE walk_id = ? ORDER BY timestamp', id)
    : await value.getAllAsync<Row>(`WITH sampled AS (
        SELECT data, timestamp, json_extract(data, '$.part') AS part,
          row_number() OVER (ORDER BY timestamp) AS n,
          lag(json_extract(data, '$.part')) OVER (ORDER BY timestamp) AS prev,
          lead(json_extract(data, '$.part')) OVER (ORDER BY timestamp) AS next
        FROM points WHERE walk_id = ?)
      SELECT data FROM sampled WHERE n % ? = 0 OR prev IS NULL OR next IS NULL OR prev != part OR next != part ORDER BY timestamp`, id, stride);
  const points: RoutePoint[] = rows.map(row => JSON.parse(row.data));
  return { walk, points };
}
export async function createWalk(walk: Walk) {
  await transaction(async tx => {
    if (await tx.getFirstAsync('SELECT id FROM walks WHERE active = 1')) throw new Error('Сначала заверши текущую прогулку.');
    await write(tx, walk);
  });
}
export async function editWalk(id: string, change: (walk: Walk) => void) {
  return transaction(async tx => {
    const walk = decode(await tx.getFirstAsync<Row>('SELECT data FROM walks WHERE id = ?', id));
    if (!walk) return null;
    change(walk);
    await write(tx, walk);
    return walk;
  });
}
export async function appendFixes(fixes: Fix[]) {
  if (!fixes.length) return;
  await transaction(async tx => {
    const walk = decode(await tx.getFirstAsync<Row>('SELECT data FROM walks WHERE active = 1'));
    if (!walk) return;
    for (const fix of [...fixes].sort((a, b) => a.timestamp - b.timestamp)) {
      if (fix.timestamp > Date.now() + 10000) { walk.rejected++; continue; }
      const segment = walk.segments.find(s => fix.timestamp >= s.start && (s.end === null || fix.timestamp <= s.end));
      if (!segment) continue;
      if (await tx.getFirstAsync('SELECT timestamp FROM points WHERE walk_id = ? AND timestamp = ?', walk.id, fix.timestamp)) continue;
      const row = await tx.getFirstAsync<Row>('SELECT data FROM points WHERE walk_id = ? AND segment_id = ? ORDER BY timestamp DESC LIMIT 1', walk.id, segment.id);
      const previous: RoutePoint | null = row ? JSON.parse(row.data) : null;
      const accepted = acceptFix(fix, previous, segment);
      if (usableFix(fix, segment) && (!previous || (fix.timestamp > previous.timestamp &&
        (accepted?.gap || meters(previous, fix) / ((fix.timestamp - previous.timestamp) / 1000) <= 8)))) {
        segment.lastSample = Math.max(segment.lastSample ?? 0, fix.timestamp);
        if (fix.timestamp >= (walk.lastSample ?? 0)) { walk.lastSample = fix.timestamp; walk.lastAccuracy = fix.accuracy; }
      }
      if (!accepted) { walk.rejected++; continue; }
      await tx.runAsync('INSERT INTO points (walk_id, timestamp, segment_id, data) VALUES (?, ?, ?, ?)', walk.id, fix.timestamp, segment.id, JSON.stringify(accepted.point));
      walk.distance += accepted.distance;
      walk.maxSpeed = Math.max(walk.maxSpeed, accepted.speed);
      walk.pointCount++;
      walk.lastFix = Math.max(walk.lastFix ?? 0, fix.timestamp);
      if (accepted.gap) walk.gaps++;
    }
    await write(tx, walk);
  });
}
export async function trackingWarning(message: string) {
  const walk = await activeWalk();
  if (walk) await editWalk(walk.id, value => { value.warning = message; });
}
export async function deleteWalk(id: string) {
  await transaction(async tx => {
    const walk = decode(await tx.getFirstAsync<Row>('SELECT data FROM walks WHERE id = ?', id));
    if (!walk || walk.status !== 'completed') throw new Error('Можно удалить только завершённую прогулку.');
    await tx.runAsync('DELETE FROM points WHERE walk_id = ?', id);
    await tx.runAsync('DELETE FROM walks WHERE id = ?', id);
  });
}
