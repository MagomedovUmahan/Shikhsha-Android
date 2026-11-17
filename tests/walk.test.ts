import { test } from 'node:test';
import assert from 'node:assert/strict';
import { acceptFix, activeMilliseconds, duration, meters, pace, routeLines, stepDelta, toGPX, walkSteps, type Fix, type Segment, type Walk } from '../src/tracking/model.ts';

const segment: Segment = { id: 's1', start: 1000, end: 100000, baseline: null, steps: null, stepsPartial: false, lastSample: null };
const fix = (latitude = 55, timestamp = 1000): Fix => ({ latitude, longitude: 37, timestamp, accuracy: 5 });
const first = acceptFix(fix(), null, segment)!.point;
const walk: Walk = { id: 'test', startedAt: 1000, endedAt: null, status: 'recording', mode: 'background',
  segments: [segment], distance: 0, maxSpeed: 0, pointCount: 1, lastFix: 1000, lastSample: 1000, lastAccuracy: 5, rejected: 0, gaps: 0, warning: null };

test('GPS distance uses geodesic metres and accepts real walking movement', () => {
  assert.ok(Math.abs(meters(fix(), fix(55.001)) - 111.195) < 0.1);
  const value = acceptFix(fix(55.0001, 11000), first, segment)!;
  assert.ok(value.distance > 11 && value.distance < 12);
  assert.equal(value.point.part, first.part);
  assert.ok(value.speed > 1 && value.speed < 1.2);
});
test('GPS rejects jitter, mock fixes, low accuracy, impossible jumps and nonmonotonic time', () => {
  for (const invalid of [fix(55.000001, 2000), fix(56, 2000), fix(55.0001, 1000), fix(55.0001, 500),
    { ...fix(), accuracy: null }, { ...fix(), accuracy: 36 }, { ...fix(), latitude: NaN },
    { ...fix(), longitude: 181 }, { ...fix(), mocked: true }]) assert.equal(acceptFix(invalid, first, segment), null);
});
test('a GPS gap starts a new line and adds no imaginary connecting distance', () => {
  const value = acceptFix(fix(55.01, 41000), first, segment)!;
  assert.equal(value.gap, true);
  assert.equal(value.distance, 0);
  assert.notEqual(value.point.part, first.part);
});
test('stationary reliable samples prevent false gaps while preserving jitter filtering', () => {
  const value = acceptFix(fix(55.0001, 41000), first, { ...segment, lastSample: 39000 })!;
  assert.equal(value.gap, false);
  assert.equal(value.point.part, first.part);
  assert.ok(value.distance > 11);
});
test('only fixes inside the recording interval are accepted; resumed segments stay separate', () => {
  assert.equal(acceptFix(fix(55, 100001), null, segment), null);
  assert.equal(acceptFix(fix(55, 999), null, segment), null);
  const next = acceptFix(fix(56, 110000), null, { ...segment, id: 'resume', start: 110000, end: null })!;
  assert.equal(next.distance, 0);
  assert.notEqual(next.point.part, first.part);
});
test('pause time is excluded and speed/pace units are consistent', () => {
  const value = { ...walk, segments: [{ ...segment, start: 1000, end: 61000 }, { ...segment, start: 121000, end: null }] };
  assert.equal(activeMilliseconds(value, 181000), 120000);
  assert.equal(duration(3661000), '01:01:01');
  assert.equal(pace(1000, 720000), '12:00');
  assert.equal(pace(0, 720000), '—');
});
test('walk steps handle midnight without recounting the previous day or imported old history', () => {
  const baseline = { days: { '2026-10-31': 4000, '2026-11-01': 100 }, day: '2026-11-01', enabled: true };
  assert.deepEqual(stepDelta(baseline, { '2026-10-30': 20000, '2026-10-31': 6000, '2026-11-01': 160, '2026-11-02': 20 }, true), { count: 80, partial: false });
  assert.deepEqual(stepDelta(baseline, { '2026-11-01': 80 }, false), { count: 0, partial: true });
  assert.equal(stepDelta(null, {}, true).count, null);
  assert.deepEqual(walkSteps({ ...walk, segments: [{ ...segment, steps: 20 }, { ...segment, steps: null }] }), { count: 20, partial: true });
  assert.equal(walkSteps(walk).count, null);
});
test('GPX preserves timestamped independent segments and escapes metadata', () => {
  const b = { ...first, timestamp: 11000, latitude: 55.0001 };
  const c = { ...first, part: 'paused', timestamp: 50000, latitude: 56 };
  assert.equal(routeLines([first, b, c]).length, 1);
  const xml = toGPX({ walk, points: [first, b, c] }, 'Парк & <Лес>');
  assert.equal((xml.match(/<trkseg>/g) ?? []).length, 2);
  assert.equal((xml.match(/<trkpt /g) ?? []).length, 3);
  assert.match(xml, /Парк &amp; &lt;Лес&gt;/);
  assert.match(xml, /1970-01-01T00:00:01.000Z/);
  assert.doesNotMatch(xml, /<ele>/);
});
