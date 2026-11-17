import { test } from 'node:test';
import assert from 'node:assert/strict';
import { DEFAULT_SETTINGS, distanceKm, lastDays, localDay, parseSettings } from '../src/lib/activity.ts';

test('local dates use device calendar rather than a UTC ISO date', () => {
  const date = new Date(2026, 10, 1, 0, 5);
  assert.equal(localDay(date), '2026-11-01');
});
test('history crosses month and year boundaries and retains missing vs zero', () => {
  const days = lastDays({ '2025-12-31': 0, '2026-01-01': 8500 }, 7, new Date(2026, 0, 2, 12));
  assert.equal(days.length, 7);
  assert.equal(days[0].key, '2025-12-27');
  assert.equal(days[4].steps, 0);
  assert.equal(days[5].steps, 8500);
  assert.equal(days[6].steps, null);
});
test('kilometres derive from actual steps and user stride, without rounding the source', () => {
  assert.equal(distanceKm(10000, 70), 7);
  assert.equal(distanceKm(0, 70), 0);
  assert.equal(distanceKm(1234, 65), 0.8021);
});
test('invalid persisted settings fall back independently', () => {
  assert.deepEqual(parseSettings(null), DEFAULT_SETTINGS);
  assert.deepEqual(parseSettings({ goal: -1, strideCm: 80 }), { goal: 8000, strideCm: 80, profile: null });
  assert.deepEqual(parseSettings({ goal: 10000, strideCm: '70' }), { goal: 10000, strideCm: 70, profile: null });
  assert.deepEqual(parseSettings({ goal: Infinity, strideCm: NaN }), DEFAULT_SETTINGS);
  assert.deepEqual(parseSettings({ goal: 8000.5, strideCm: 29 }), DEFAULT_SETTINGS);
});
