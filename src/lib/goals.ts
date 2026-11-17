export type GoalProfile = { weightKg: number; purpose: 'maintain' | 'loss'; baselineSteps: number };
export function parseProfile(raw: unknown): GoalProfile | null {
  if (!raw || typeof raw !== 'object') return null;
  const p = raw as Record<string, unknown>;
  return typeof p.weightKg === 'number' && Number.isFinite(p.weightKg) && p.weightKg >= 30 && p.weightKg <= 300 &&
    (p.purpose === 'maintain' || p.purpose === 'loss') && typeof p.baselineSteps === 'number' &&
    Number.isInteger(p.baselineSteps) && p.baselineSteps >= 1000 && p.baselineSteps <= 30000
    ? { weightKg: Math.round(p.weightKg * 10) / 10, purpose: p.purpose, baselineSteps: p.baselineSteps } : null;
}
/** A transparent planning convention, not a prediction of weight change. */
export function goalPlan(profile: GoalProfile) {
  const weeklyMinutes = profile.purpose === 'loss' ? 300 : 150;
  const extraSteps = Math.round(weeklyMinutes / 7 * 100 / 250) * 250;
  const dailyGoal = Math.min(50000, profile.baselineSteps + extraSteps);
  return { weeklyMinutes, extraSteps, dailyGoal, dailyMinutes: Math.round(weeklyMinutes / 7),
    gentleStart: Math.min(dailyGoal, profile.baselineSteps + 500) };
}
/** Net energy above rest, assuming level walking at 4.8 km/h and 3.8 MET. */
export function walkingEnergy(steps: number, strideCm: number, weightKg: number): number {
  if (![steps, strideCm, weightKg].every(Number.isFinite) || steps < 0 || strideCm <= 0 || weightKg <= 0) return 0;
  return Math.round((3.8 - 1) * weightKg * (steps * strideCm / 100000 / 4.8));
}
export function personalRank(history: Record<string, number>, goal: number, recent: (number | null)[]) {
  const total = Object.values(history).reduce((sum, n) => sum + n, 0);
  const observed = recent.filter((n): n is number => n !== null);
  const score = observed.length ? Math.round(observed.reduce((sum, n) => sum + Math.min(1, n / Math.max(1, goal)), 0) / observed.length * 1000) : null;
  return { title: total >= 200000 ? 'Покоритель вершин' : total >= 50000 ? 'Горец' : 'Исследователь', score, observed: observed.length, total };
}
