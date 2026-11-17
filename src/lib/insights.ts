import { lastDays, localDay, type Day } from './activity.ts';

export function summary(days: Day[], goal: number) {
  const known = days.filter(day => day.steps !== null);
  const total = known.reduce((sum, day) => sum + day.steps!, 0);
  const best = known.reduce<Day | null>((best, day) => !best || day.steps! > best.steps! ? day : best, null);
  return {
    known: known.length, total, average: known.length ? Math.round(total / known.length) : null,
    best, achieved: known.filter(day => day.steps! >= goal).length,
  };
}
export function currentStreak(history: Record<string, number>, goal: number, now = new Date()) {
  let date = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 12);
  // An incomplete today does not break yesterday's ongoing streak.
  if ((history[localDay(date)] ?? 0) < goal) date.setDate(date.getDate() - 1);
  let streak = 0;
  while ((history[localDay(date)] ?? 0) >= goal && streak < 90) {
    streak++;
    date.setDate(date.getDate() - 1);
  }
  return streak;
}
export type Achievement = {
  id: string; title: string; subtitle: string; icon: 'leaf-outline' | 'footsteps-outline' | 'flash-outline' | 'flame-outline' | 'trophy-outline' | 'planet-outline';
  value: number; target: number; unit: string; unlocked: boolean; progress: number;
};
export function achievements(history: Record<string, number>, goal: number, now = new Date()): Achievement[] {
  const values = Object.values(history);
  const total = values.reduce((sum, value) => sum + value, 0);
  const best = Math.max(0, ...values);
  const streak = currentStreak(history, goal, now);
  const items: Omit<Achievement, 'unlocked' | 'progress'>[] = [
    { id: 'start', title: 'Предгорье', subtitle: '1 000 шагов за день', icon: 'leaf-outline', value: best, target: 1000, unit: 'шагов' },
    { id: 'five', title: 'Фишт', subtitle: '5 000 шагов за день', icon: 'footsteps-outline', value: best, target: 5000, unit: 'шагов' },
    { id: 'ten', title: 'Казбек', subtitle: '10 000 шагов за день', icon: 'flash-outline', value: best, target: 10000, unit: 'шагов' },
    { id: 'three', title: 'Столовая гора', subtitle: 'Текущая серия из 3 дней', icon: 'flame-outline', value: streak, target: 3, unit: 'дн.' },
    { id: 'fifty', title: 'Дыхтау', subtitle: '50 000 записанных шагов', icon: 'trophy-outline', value: total, target: 50000, unit: 'шагов' },
    { id: 'hundred', title: 'Эльбрус', subtitle: '100 000 записанных шагов', icon: 'planet-outline', value: total, target: 100000, unit: 'шагов' },
  ];
  return items.map(item => ({ ...item, unlocked: item.value >= item.target, progress: Math.min(1, Math.max(0, item.value / item.target)) }));
}
export function period(history: Record<string, number>, length: 7 | 30 | 90, now = new Date()) {
  return lastDays(history, length, now);
}
