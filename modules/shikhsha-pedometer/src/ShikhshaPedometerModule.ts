import { requireOptionalNativeModule } from 'expo';
import { iphoneExpoGo } from '../../../src/lib/runtime';
import { createExpoCounter } from '../../../src/lib/expoCounter';

export type Snapshot = {
  available: boolean;
  permission: 'granted' | 'denied' | 'undetermined';
  enabled: boolean;
  running: boolean;
  days: Record<string, number>;
  updatedAt: number;
  error?: string | null;
};
export type PedometerModule = {
  getSnapshot(): Promise<Snapshot>;
  getLiveSnapshot(): Promise<Snapshot>;
  start(): Promise<Snapshot>;
  stop(): Promise<Snapshot>;
  getStepsInInterval?(from: number, to: number): Promise<number | null>;
};
let backend: PedometerModule | null = requireOptionalNativeModule<PedometerModule>('ShikhshaPedometer');
if (!backend && iphoneExpoGo) {
  // These modules are bundled with Expo Go on iPhone.
  // eslint-disable-next-line @typescript-eslint/no-require-imports
  const storage = require('@react-native-async-storage/async-storage').default;
  // eslint-disable-next-line @typescript-eslint/no-require-imports
  const { Pedometer } = require('expo-sensors');
  backend = createExpoCounter(storage, Pedometer);
}
export default backend;
