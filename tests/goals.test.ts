import { test } from 'node:test';
import assert from 'node:assert/strict';
import { goalPlan, parseProfile, personalRank, walkingEnergy } from '../src/lib/goals.ts';
import { parseSettings } from '../src/lib/activity.ts';

test('old settings retain manual goals and migrate without inventing a weight', () => {
  assert.deepEqual(parseSettings({ goal: 6500, strideCm: 72 }), { goal: 6500, strideCm: 72, profile: null });
});
test('profile validation rejects nonfinite values, invalid purpose and unreasonable input', () => {
  const p = { weightKg: 75, purpose: 'loss', baselineSteps: 5000 };
  assert.deepEqual(parseProfile(p), p);
  for (const invalid of [{ ...p, weightKg: NaN }, { ...p, weightKg: 29 }, { ...p, weightKg: 301 },
    { ...p, baselineSteps: 999 }, { ...p, baselineSteps: 30001 }, { ...p, baselineSteps: 5000.5 }, { ...p, purpose: 'fast' }]) assert.equal(parseProfile(invalid), null);
});
test('activity goals derive from stated baseline and purpose; weight alone does not prescribe steps', () => {
  const p = { weightKg: 75, purpose: 'maintain' as const, baselineSteps: 5000 };
  assert.equal(goalPlan(p).dailyGoal, 7250);
  assert.equal(goalPlan({ ...p, purpose: 'loss' }).dailyGoal, 9250);
  assert.equal(goalPlan(p).dailyGoal, goalPlan({ ...p, weightKg: 150 }).dailyGoal);
  assert.equal(goalPlan(p).gentleStart, 5500);
  assert.equal(goalPlan({ ...p, baselineSteps: 1000 }).gentleStart, 1500);
});
test('energy is a labelled model, proportional to weight, distance and zero at no steps', () => {
  assert.equal(walkingEnergy(0, 70, 75), 0);
  assert.ok(Math.abs(walkingEnergy(5000, 70, 150) - 2 * walkingEnergy(5000, 70, 75)) <= 1);
  assert.equal(walkingEnergy(NaN, 70, 75), 0);
});
test('personal activity index distinguishes missing from zero and caps completion', () => {
  assert.equal(personalRank({}, 8000, [null, null]).score, null);
  assert.equal(personalRank({}, 8000, [0, null]).score, 0);
  assert.equal(personalRank({}, 8000, [8000, 16000]).score, 1000);
  assert.equal(personalRank({ day: 50000 }, 8000, [4000]).title, 'Горец');
  assert.equal(personalRank({ day: 200000 }, 8000, [4000]).title, 'Покоритель вершин');
});
