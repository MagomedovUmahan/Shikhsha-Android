import type { PedometerModule, Snapshot } from '../../modules/shikhsha-pedometer/src/ShikhshaPedometerModule';
import { localDay } from './activity.ts';

type Storage = { getItem(key: string): Promise<string | null>; setItem(key: string, value: string): Promise<void> };
type API = {
  isAvailableAsync(): Promise<boolean>;
  getPermissionsAsync(): Promise<{ status: string }>;
  requestPermissionsAsync(): Promise<{ status: string }>;
  getStepCountAsync(start: Date, end: Date): Promise<{ steps: number }>;
  watchStepCount(listener: (value: { steps: number }) => void): { remove(): void };
};
type Ledger = { enabled: boolean; days: Record<string, number>; updatedAt: number;
  session: { start: number; base: number; end: number | null } | null };
const KEY = '@shikhsha/expo-ios-counter/v1';

/** Uses real Core Motion queries in Expo Go; no native module or generated steps. */
export function createExpoCounter(storage: Storage, api: API, now: () => number = Date.now): PedometerModule {
  let state: Ledger | undefined;
  let tail: Promise<unknown> = Promise.resolve();
  let watch: { remove(): void } | null = null;
  let watchDay = '', generation = 0, requestedStop: number | null = null;
  let error: string | null = null;
  const serial = <T,>(fn: () => Promise<T>): Promise<T> => {
    const work = tail.catch(() => undefined).then(fn);
    tail = work;
    return work;
  };
  const load = async () => {
    if (state) return state;
    const raw = await storage.getItem(KEY);
    if (raw) {
      const value = JSON.parse(raw) as Ledger;
      if (typeof value.enabled !== 'boolean' || !Number.isFinite(value.updatedAt) || value.updatedAt < 0 ||
          !value.days || typeof value.days !== 'object' || Array.isArray(value.days) ||
          Object.keys(value.days).some(k => !/^\d{4}-\d{2}-\d{2}$/.test(k)) ||
          Object.values(value.days).some(n => !Number.isInteger(n) || n < 0) || (value.enabled && !value.session) ||
          (value.session && (!Number.isFinite(value.session.start) || !Number.isFinite(value.session.base) ||
          value.session.base < 0 || (value.session.end !== null && (!Number.isFinite(value.session.end) || value.session.end < value.session.start)))))
        throw new Error('Не удалось прочитать историю шагов. Исходные данные сохранены на телефоне.');
      state = value;
    } else state = { enabled: false, days: {}, updatedAt: 0, session: null };
    return state;
  };
  const save = async (next: Ledger) => {
    const keys = Object.keys(next.days).sort().slice(-90);
    const retained = { ...next, days: Object.fromEntries(keys.map(k => [k, next.days[k]])) };
    await storage.setItem(KEY, JSON.stringify(retained));
    state = retained; error = null;
  };
  const halt = () => { generation++; watch?.remove(); watch = null; };
  const snapshot = async (): Promise<Snapshot> => {
    const value = await load();
    const available = await api.isAvailableAsync();
    const response = await api.getPermissionsAsync();
    const permission = response.status === 'granted' ? 'granted' : response.status === 'undetermined' ? 'undetermined' : 'denied';
    return { available, permission, enabled: value.enabled, running: !!watch && value.enabled && permission === 'granted', days: { ...value.days }, updatedAt: value.updatedAt, error };
  };
  const synchronize = async (at: number) => {
    const current = await load(), session = current.session;
    if (!session) return;
    const until = Math.min(at, session.end ?? Infinity, requestedStop ?? Infinity);
    const today = new Date(now()), days = { ...current.days };
    for (let offset = -6; offset <= 0; offset++) {
      const day = new Date(today.getFullYear(), today.getMonth(), today.getDate() + offset);
      const next = new Date(day.getFullYear(), day.getMonth(), day.getDate() + 1);
      const from = Math.max(day.getTime(), session.start, now() - 7 * 86400000);
      const to = Math.min(next.getTime(), until);
      if (from >= to) continue;
      const data = await api.getStepCountAsync(new Date(from), new Date(to));
      if (!Number.isFinite(data.steps) || data.steps < 0) throw new Error('Системный шагомер вернул некорректные данные.');
      const key = localDay(day);
      const base = key === localDay(new Date(session.start)) ? session.base : 0;
      days[key] = Math.max(days[key] ?? 0, base + Math.floor(data.steps));
    }
    await save({ ...current, days, updatedAt: now() });
  };
  const ensureWatch = async () => {
    const current = await load(), today = localDay(new Date(now()));
    if (!current.enabled || requestedStop !== null || (watch && watchDay === today)) return;
    halt(); watchDay = today;
    const token = generation, base = current.days[today] ?? 0;
    watch = api.watchStepCount(data => {
      if (!Number.isFinite(data.steps) || data.steps < 0) return;
      void serial(async () => {
        const value = await load();
        if (token !== generation || !value.enabled || requestedStop !== null) return;
        if (localDay(new Date(now())) !== today) { await synchronize(now()); await ensureWatch(); return; }
        await save({ ...value, days: { ...value.days, [today]: Math.max(value.days[today] ?? 0, base + Math.floor(data.steps)) }, updatedAt: now() });
      }).catch(() => { error = 'Не удалось сохранить шаги. Проверь свободное место на телефоне.'; });
    });
  };
  return {
    getSnapshot: () => serial(async () => {
      const value = await load();
      if ((await api.getPermissionsAsync()).status === 'granted' && value.session) {
        await synchronize(now());
        if (value.enabled) await ensureWatch();
      }
      return snapshot();
    }),
    getLiveSnapshot: () => serial(snapshot),
    start: () => serial(async () => {
      if (!await api.isAvailableAsync()) throw new Error('На этом устройстве недоступен шагомер.');
      if ((await api.requestPermissionsAsync()).status !== 'granted') throw new Error('Разреши Expo Go доступ к движению и фитнесу в настройках iPhone.');
      const value = await load();
      if (value.enabled && requestedStop === null) { await synchronize(now()); await ensureWatch(); return snapshot(); }
      if (value.session) await synchronize(value.session.end ?? requestedStop ?? now());
      const current = await load(), start = now(), day = localDay(new Date(start));
      requestedStop = null;
      halt();
      await save({ ...current, enabled: true, session: { start, base: current.days[day] ?? 0, end: null }, days: { ...current.days, [day]: current.days[day] ?? 0 } });
      await ensureWatch();
      return snapshot();
    }),
    stop: () => {
      const until = requestedStop ?? now();
      requestedStop = until; halt();
      return serial(async () => {
        const current = await load();
        if (current.enabled) await save({ ...current, enabled: false, session: current.session ? { ...current.session, end: until } : null });
        await synchronize(until);
        return snapshot();
      });
    },
    getStepsInInterval: async (from, to) => {
      if (from > to || from < now() - 7 * 86400000 || !await api.isAvailableAsync() || (await api.getPermissionsAsync()).status !== 'granted') return null;
      const result = await api.getStepCountAsync(new Date(from), new Date(Math.min(to, now())));
      return Number.isFinite(result.steps) && result.steps >= 0 ? Math.floor(result.steps) : null;
    },
  };
}
