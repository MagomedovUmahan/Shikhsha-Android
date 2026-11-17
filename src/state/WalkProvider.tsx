import React, { createContext, useCallback, useContext, useEffect, useRef, useState } from 'react';
import { AppState, Platform } from 'react-native';
import * as Location from 'expo-location';
import * as TaskManager from 'expo-task-manager';
import Pedometer from '../../modules/shikhsha-pedometer/src/ShikhshaPedometerModule';
import { localDay } from '../lib/activity';
import { LOCATION_TASK, toFix } from '../tracking/locationTask';
import { type Segment, type StepBaseline, type Walk, type WalkView, stepDelta } from '../tracking/model';
import { activeWalk, appendFixes, createWalk, editWalk, getWalk, listWalks, trackingWarning } from '../tracking/store';
import { useActivity } from './ActivityProvider';
import { expoGo } from '../lib/runtime';

type ContextValue = {
  active: WalkView | null; history: Walk[]; loading: boolean; busy: boolean; supported: boolean;
  error: string | null; now: number;
  start(background: boolean): Promise<void>; pause(): Promise<void>; resume(): Promise<void>;
  finish(): Promise<WalkView | null>; refresh(): Promise<void>; loadMore(): Promise<void>;
};
const Context = createContext<ContextValue | null>(null);
const message = (cause: unknown) => cause instanceof Error ? cause.message : 'Не удалось обновить прогулку.';
const id = () => `${Date.now()}-${Math.random().toString(36).slice(2, 10)}`;

async function baseline(): Promise<StepBaseline | null> {
  try {
    const value = await Pedometer?.getLiveSnapshot();
    if (!value?.available || value.permission !== 'granted' || !value.enabled) return null;
    // Android can resolve start() before Service.onCreate sets `running`.
    // Persisted `enabled` is the baseline contract; later samples flag outages.
    return { days: value.days, day: localDay(), enabled: value.enabled };
  } catch { return null; }
}
async function segmentSteps(segment: Segment) {
  if (Platform.OS === 'ios') {
    try {
      const count = await Pedometer?.getStepsInInterval?.(segment.start, segment.end ?? Date.now());
      return { count: typeof count === 'number' ? count : null, partial: count === null || count === undefined };
    } catch { return { count: null, partial: true }; }
  }
  try {
    const value = await Pedometer?.getLiveSnapshot();
    if (!value) return { count: null, partial: true };
    return stepDelta(segment.baseline, value.days, value.enabled && value.running && value.permission === 'granted');
  } catch { return { count: null, partial: true }; }
}

export function WalkProvider({ children }: React.PropsWithChildren) {
  const activity = useActivity();
  const [active, setActive] = useState<WalkView | null>(null);
  const [history, setHistory] = useState<Walk[]>([]);
  const supported = Platform.OS !== 'web' && Pedometer !== null;
  const [loading, setLoading] = useState(supported);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [now, setNow] = useState(Date.now);
  const lock = useRef(false), mounted = useRef(true), refreshing = useRef(false);
  const watcher = useRef<Location.LocationSubscription | null>(null);
  const activeCache = useRef<WalkView | null>(null);
  const historyLimit = useRef(20);

  const refresh = useCallback(async () => {
    if (!supported || refreshing.current) return;
    refreshing.current = true;
    try {
      const walk = await activeWalk();
      let view: WalkView | null = null;
      if (walk) {
        const cached = activeCache.current;
        view = cached?.walk.id === walk.id && cached.walk.pointCount === walk.pointCount
          ? { walk, points: cached.points } : await getWalk(walk.id);
      }
      const recent = await listWalks(historyLimit.current);
      activeCache.current = view;
      if (mounted.current) { setActive(view); setHistory(recent); }
    } catch (cause) { if (mounted.current) setError(message(cause)); }
    finally { refreshing.current = false; }
  }, [supported]);

  const stopGPS = useCallback(async () => {
    watcher.current?.remove(); watcher.current = null;
    if (!expoGo && await Location.hasStartedLocationUpdatesAsync(LOCATION_TASK)) await Location.stopLocationUpdatesAsync(LOCATION_TASK);
  }, []);
  const saveSteps = useCallback(async (walkId: string, segment: Segment) => {
    const reading = await segmentSteps(segment);
    await editWalk(walkId, value => {
      const target = value.segments.find(s => s.id === segment.id);
      if (!target) return;
      // A delayed live query must not overwrite a closed segment.
      if (target.end !== segment.end) return;
      target.steps = reading.count === null ? target.steps : Math.max(target.steps ?? 0, reading.count);
      target.stepsPartial ||= reading.partial;
    });
  }, []);
  const pauseInternal = useCallback(async (reason?: string) => {
    const walk = await activeWalk();
    if (!walk) { await stopGPS(); return; }
    if (walk.status !== 'recording') { await stopGPS(); return; }
    const end = Date.now();
    const updated = await editWalk(walk.id, value => {
      for (const s of value.segments) if (s.end === null) s.end = end;
      value.status = 'paused';
      if (reason) value.warning = reason;
    });
    await stopGPS();
    const segment = updated?.segments.at(-1);
    if (segment) await saveSteps(walk.id, segment);
  }, [saveSteps, stopGPS]);

  const permission = async (background: boolean) => {
    if (!supported) throw new Error('Запись маршрута работает в отдельной сборке Шихши на телефоне.');
    const foreground = await Location.requestForegroundPermissionsAsync();
    if (!foreground.granted) throw new Error('Разреши геолокацию в настройках телефона, чтобы записать маршрут.');
    const precise = Platform.OS === 'android' ? foreground.android?.accuracy === 'fine' : foreground.ios?.accuracy !== 'reduced';
    if (!precise) throw new Error('Включи точную геопозицию в настройках разрешений Шихши. Приблизительной позиции недостаточно для маршрута.');
    if (!await Location.hasServicesEnabledAsync()) throw new Error('Включи геолокацию телефона и повтори запуск.');
    if (background) {
      if (expoGo) throw new Error('В Expo Go на iPhone доступна запись при открытом приложении. Для записи с выключенным экраном нужна отдельная сборка.');
      if (!await TaskManager.isAvailableAsync()) throw new Error('Фоновая геолокация недоступна в этой сборке. Установи новую сборку приложения.');
      const response = await Location.requestBackgroundPermissionsAsync();
      if (!response.granted) throw new Error('Для записи с выключенным экраном нужен доступ к геопозиции «Всегда». Разреши его в настройках или выбери запись только с открытым приложением.');
    }
  };
  const startGPS = async (walk: Walk) => {
    const options: Location.LocationTaskOptions = {
      accuracy: Location.Accuracy.BestForNavigation, timeInterval: 2000, distanceInterval: 0,
      activityType: Location.ActivityType.Fitness, pausesUpdatesAutomatically: false,
      showsBackgroundLocationIndicator: true,
      foregroundService: { notificationTitle: 'Шихша • прогулка', notificationBody: 'Маршрут записывается. Открой приложение, чтобы поставить на паузу или завершить.', killServiceOnDestroy: false },
    };
    if (walk.mode === 'background') await Location.startLocationUpdatesAsync(LOCATION_TASK, options);
    else watcher.current = await Location.watchPositionAsync(options, fix => {
      void appendFixes([toFix(fix)]).catch(cause => { setError(message(cause)); });
    }, failure => { void trackingWarning('Геолокация: ' + failure).catch(() => undefined); });
    if (walk.mode === 'foreground' && AppState.currentState !== 'active') {
      await pauseInternal('Запись поставлена на паузу: приложение было свёрнуто. Нажми «Продолжить», когда будешь готов.');
    }
  };
  const run = async <T,>(action: () => Promise<T>): Promise<T | undefined> => {
    if (lock.current) return;
    lock.current = true; setBusy(true); setError(null);
    try { return await action(); }
    catch (cause) { setError(message(cause)); }
    finally { lock.current = false; if (mounted.current) setBusy(false); await refresh(); }
  };

  const start = async (background: boolean) => { await run(async () => {
    if (await activeWalk()) throw new Error('Сначала заверши текущую прогулку.');
    await permission(background);
    // Step permission is independent. A missing step sensor must not block GPS.
    await activity.start();
    const first = await baseline(), start = Date.now(), walkId = id();
    const walk: Walk = { id: walkId, startedAt: start, endedAt: null, status: 'recording',
      mode: background ? 'background' : 'foreground', distance: 0, maxSpeed: 0, pointCount: 0,
      lastFix: null, lastSample: null, lastAccuracy: null, rejected: 0, gaps: 0, warning: null,
      segments: [{ id: id(), start, end: null, baseline: first, steps: null, stepsPartial: false, lastSample: null }] };
    await createWalk(walk);
    try { await startGPS(walk); }
    catch (cause) { await pauseInternal('Запись не запущена. Проверь разрешения и нажми «Продолжить».'); throw cause; }
  }); };
  const pause = async () => { await run(() => pauseInternal()); };
  const resume = async () => { await run(async () => {
    const walk = await activeWalk();
    if (!walk || walk.status !== 'paused') return;
    await permission(walk.mode === 'background');
    await activity.start();
    const first = await baseline();
    const updated = await editWalk(walk.id, value => {
      value.segments.push({ id: id(), start: Date.now(), end: null, baseline: first, steps: null, stepsPartial: false, lastSample: null });
      value.status = 'recording'; value.warning = null;
    });
    if (updated) {
      try { await startGPS(updated); }
      catch (cause) { await pauseInternal('Не удалось возобновить геолокацию. Проверь разрешения.'); throw cause; }
    }
  }); };
  const finish = async () => (await run(async () => {
    const walk = await activeWalk();
    if (!walk) return null;
    if (walk.status === 'recording') await pauseInternal();
    else await stopGPS();
    await editWalk(walk.id, value => { value.status = 'completed'; value.endedAt = Date.now(); });
    return getWalk(walk.id);
  })) ?? null;

  useEffect(() => {
    mounted.current = true;
    let disposed = false;
    if (!supported) return;
    void (async () => {
      try {
        const walk = await activeWalk();
        if (walk?.status === 'recording') {
          const registered = !expoGo && walk.mode === 'background' && await Location.hasStartedLocationUpdatesAsync(LOCATION_TASK);
          const granted = (await Location.getForegroundPermissionsAsync()).granted;
          const stale = Date.now() - (walk.lastSample ?? walk.startedAt) > 5 * 60 * 1000;
          if (!registered || !granted || stale) {
            // Never count an unknown process-death interval as walking time.
            await editWalk(walk.id, value => {
              const s = value.segments.at(-1);
              if (s && s.end === null) {
                s.end = Math.max(s.start, value.lastSample ?? s.start);
                // We cannot place a daily Android delta at an unknown crash time.
                s.steps = null; s.stepsPartial = true;
              }
              value.status = 'paused'; value.warning = 'Запись прервалась. Сохранён доступный маршрут. Нажми «Продолжить», чтобы начать новый участок.';
            });
            await stopGPS();
          }
        } else await stopGPS();
        if (!disposed) await refresh();
      } catch (cause) { if (!disposed) setError(message(cause)); }
      finally { if (!disposed) setLoading(false); }
    })();
    const subscription = AppState.addEventListener('change', state => {
      if (state === 'active') void refresh();
      else if (!lock.current && activeCache.current?.walk.mode === 'foreground' && activeCache.current.walk.status === 'recording') {
        lock.current = true;
        void pauseInternal('Запись поставлена на паузу: приложение свёрнуто. Для записи с выключенным экраном выбери фоновый режим перед новой прогулкой.')
          .catch(cause => { setError(message(cause)); })
          .finally(() => { lock.current = false; void refresh(); });
      }
    });
    let ticks = 0, updatingSteps = false;
    const timer = setInterval(() => {
      if (AppState.currentState !== 'active') return;
      setNow(Date.now()); void refresh();
      const walk = activeCache.current?.walk, segment = walk?.segments.at(-1);
      if (++ticks % 5 === 0 && walk?.status === 'recording' && segment && !lock.current && !updatingSteps) {
        updatingSteps = true;
        void saveSteps(walk.id, segment).catch(cause => { setError(message(cause)); }).finally(() => { updatingSteps = false; });
      }
    }, 1000);
    return () => { disposed = true; mounted.current = false; subscription.remove(); clearInterval(timer); watcher.current?.remove(); watcher.current = null; };
  }, [pauseInternal, refresh, saveSteps, stopGPS, supported]);

  return <Context.Provider value={{ active, history, loading, busy, supported, error, now, start, pause, resume, finish, refresh,
    loadMore: async () => { historyLimit.current += 20; await refresh(); } }}>{children}</Context.Provider>;
}
export function useWalk() {
  const context = useContext(Context);
  if (!context) throw new Error('WalkProvider is missing');
  return context;
}
