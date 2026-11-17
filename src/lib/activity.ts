import { parseProfile, type GoalProfile } from './goals.ts';
export const DEFAULT_SETTINGS: { goal: number; strideCm: number; profile: GoalProfile | null } = { goal: 8000, strideCm: 70, profile: null };
export type Settings = typeof DEFAULT_SETTINGS;
export type Day = { key: string; date: Date; steps: number | null };

export function localDay(date = new Date()): string {
  return [date.getFullYear(), String(date.getMonth() + 1).padStart(2, '0'), String(date.getDate()).padStart(2, '0')].join('-');
}
export function lastDays(history: Record<string, number>, length = 7, now = new Date()): Day[] {
  return Array.from({ length }, (_, index) => {
    const date = new Date(now.getFullYear(), now.getMonth(), now.getDate() - length + index + 1, 12);
    const key = localDay(date);
    return { key, date, steps: Object.hasOwn(history, key) ? history[key] : null };
  });
}
export function distanceKm(steps: number, strideCm: number) {
  return steps * strideCm / 100_000;
}
export function number(value: number) {
  return new Intl.NumberFormat('ru-RU').format(value);
}
export function km(value: number) {
  return value.toLocaleString('ru-RU', { minimumFractionDigits: 1, maximumFractionDigits: 2 });
}
export function parseSettings(raw: unknown): Settings {
  const input = raw && typeof raw === 'object' ? raw as Record<string, unknown> : {};
  return {
    goal: typeof input.goal === 'number' && Number.isInteger(input.goal) && input.goal >= 1000 && input.goal <= 50000 ? input.goal : DEFAULT_SETTINGS.goal,
    strideCm: typeof input.strideCm === 'number' && Number.isInteger(input.strideCm) && input.strideCm >= 30 && input.strideCm <= 150 ? input.strideCm : DEFAULT_SETTINGS.strideCm,
    profile: parseProfile(input.profile),
  };
}
