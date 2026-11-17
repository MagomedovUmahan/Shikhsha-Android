import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { createContext, useCallback, useContext, useEffect, useRef, useState } from 'react';
import { AppState, PermissionsAndroid, Platform } from 'react-native';
import Pedometer, { type Snapshot } from '../../modules/shikhsha-pedometer/src/ShikhshaPedometerModule';
import { DEFAULT_SETTINGS, localDay, parseSettings, type Settings } from '../lib/activity';

const SETTINGS_KEY = '@shikhsha/settings/v1';
const EMPTY: Snapshot = {
  available: false, permission: 'undetermined', enabled: false, running: false, days: {}, updatedAt: 0,
};
type ActivityContext = {
  snapshot: Snapshot; settings: Settings; today: string; loading: boolean; busy: boolean;
  supported: boolean; error: string | null; notice: string | null;
  refresh(): Promise<void>; start(): Promise<void>; stop(): Promise<void>; pause(): Promise<void>;
  saveSettings(settings: Settings): Promise<void>;
};
const Context = createContext<ActivityContext | null>(null);
const message = (error: unknown) => error instanceof Error ? error.message : 'Не удалось обновить данные. Попробуйте ещё раз.';

export function ActivityProvider({ children }: React.PropsWithChildren) {
  const [snapshot, setSnapshot] = useState(EMPTY);
  const [settings, setSettings] = useState(DEFAULT_SETTINGS);
  const [today, setToday] = useState(localDay);
  const [loading, setLoading] = useState(true);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [notice, setNotice] = useState<string | null>(null);
  const mounted = useRef(true);
  const operation = useRef(false);
  const readTask = useRef<Promise<void> | null>(null);
  const dayRef = useRef(localDay());
  const supported = Platform.OS !== 'web' && Pedometer !== null;

  const refresh = useCallback(async (live = false) => {
    if (!Pedometer || Platform.OS === 'web' || operation.current) return;
    if (readTask.current) return readTask.current;
    const task = (async () => {
      try {
        const next = await (live ? Pedometer.getLiveSnapshot() : Pedometer.getSnapshot());
        if (mounted.current) {
          setSnapshot(next);
          setError(next.error || null);
          setToday(localDay());
        }
      } catch (cause) { if (mounted.current) setError(message(cause)); }
    })();
    readTask.current = task;
    try { await task; } finally { readTask.current = null; }
  }, []);

  useEffect(() => {
    mounted.current = true;
    void (async () => {
      try {
        const raw = await AsyncStorage.getItem(SETTINGS_KEY);
        if (raw && mounted.current) setSettings(parseSettings(JSON.parse(raw)));
      } catch { if (mounted.current) setNotice('Не удалось прочитать настройки. Использованы значения по умолчанию.'); }
      await refresh();
      if (mounted.current) setLoading(false);
    })();
    const subscription = AppState.addEventListener('change', state => {
      if (state === 'active') void refresh();
    });
    const timer = setInterval(() => {
      if (AppState.currentState !== 'active') return;
      const date = localDay();
      const changed = dayRef.current !== date;
      dayRef.current = date;
      setToday(date);
      void refresh(!changed);
    }, 3000);
    return () => { mounted.current = false; subscription.remove(); clearInterval(timer); };
  }, [refresh]);

  const start = useCallback(async () => {
    if (!Pedometer || !supported || operation.current) return;
    operation.current = true;
    setBusy(true); setError(null); setNotice(null);
    try {
      await readTask.current;
      if (Platform.OS === 'android' && Number(Platform.Version) >= 29) {
        const permission = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.ACTIVITY_RECOGNITION);
        if (permission !== PermissionsAndroid.RESULTS.GRANTED) {
          setSnapshot(await Pedometer.getLiveSnapshot());
          throw new Error('Разрешите физическую активность в настройках телефона, чтобы считать шаги.');
        }
      }
      if (Platform.OS === 'android' && Number(Platform.Version) >= 33) {
        const permission = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS);
        if (permission !== PermissionsAndroid.RESULTS.GRANTED)
          setNotice('Уведомления отключены. Шагомер может работать, но его уведомление будет скрыто системой.');
      }
      setSnapshot(await Pedometer.start());
    } catch (cause) {
      try { setSnapshot(await Pedometer.getLiveSnapshot()); } catch { /* preserve the last real reading */ }
      setError(message(cause));
    } finally { operation.current = false; setBusy(false); }
  }, [supported]);

  const stop = useCallback(async () => {
    if (!Pedometer || operation.current) return;
    operation.current = true; setBusy(true); setError(null);
    try { await readTask.current; setSnapshot(await Pedometer.stop()); }
    catch (cause) { setError(message(cause)); }
    finally { operation.current = false; setBusy(false); }
  }, []);

  const saveSettings = useCallback(async (next: Settings) => {
    await AsyncStorage.setItem(SETTINGS_KEY, JSON.stringify(next));
    setSettings(next);
  }, []);

  return <Context.Provider value={{
    snapshot, settings, today, loading, busy, supported, error, notice,
    refresh: () => refresh(), start, stop, pause: stop, saveSettings,
  }}>{children}</Context.Provider>;
}
export function useActivity() {
  const value = useContext(Context);
  if (!value) throw new Error('ActivityProvider is missing');
  return value;
}
