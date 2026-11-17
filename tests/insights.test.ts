import { test } from 'node:test';
import assert from 'node:assert/strict';
import { achievements, currentStreak, period, summary } from '../src/lib/insights.ts';

const now = new Date(2026, 10, 1, 12);

test('summary distinguishes missing dates from actual zero and uses observed days only', () => {
  const result = summary(period({ '2026-10-30': 0, '2026-10-31': 6000, '2026-11-01': 10000 }, 7, now), 8000);
  assert.equal(result.known, 3);
  assert.equal(result.total, 16000);
  assert.equal(result.average, 5333);
  assert.equal(result.achieved, 1);
  assert.equal(result.best?.key, '2026-11-01');
  assert.equal(summary(period({}, 7, now), 8000).average, null);
});

test('current streak tolerates an unfinished today and stops at a missing calendar day', () => {
  const history = { '2026-10-28': 9000, '2026-10-30': 8000, '2026-10-31': 9500, '2026-11-01': 200 };
  assert.equal(currentStreak(history, 8000, now), 2);
  assert.equal(currentStreak({ ...history, '2026-11-01': 8500 }, 8000, now), 3);
  assert.equal(currentStreak({ '2026-10-30': 9000 }, 8000, now), 0);
});

test('periods contain exact local calendar dates across a year boundary', () => {
  const date = new Date(2026, 0, 2, 12);
  assert.equal(period({}, 30, date).length, 30);
  assert.equal(period({}, 30, date)[0].key, '2025-12-04');
  assert.equal(period({}, 90, date).length, 90);
  assert.equal(period({}, 90, date)[89].key, '2026-01-02');
});

test('badges never unlock from missing data and progress caps at one', () => {
  assert.ok(achievements({}, 8000, now).every(badge => !badge.unlocked && badge.progress === 0));
  const badges = achievements({ '2026-10-30': 20000, '2026-10-31': 20000, '2026-11-01': 20000 }, 8000, now);
  assert.equal(badges.find(badge => badge.id === 'three')?.unlocked, true);
  assert.equal(badges.find(badge => badge.id === 'fifty')?.unlocked, true);
  assert.equal(badges.find(badge => badge.id === 'hundred')?.unlocked, false);
  assert.equal(badges.find(badge => badge.id === 'start')?.progress, 1);
});
