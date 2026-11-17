import { Platform } from 'react-native';
import * as TaskManager from 'expo-task-manager';
import type { LocationObject } from 'expo-location';
import { appendFixes, trackingWarning } from './store';
import type { Fix } from './model';
import { expoGo } from '../lib/runtime';

export const LOCATION_TASK = 'shikhsha.walk.location.v1';
export const toFix = (location: LocationObject): Fix => ({
  timestamp: location.timestamp, latitude: location.coords.latitude, longitude: location.coords.longitude,
  accuracy: location.coords.accuracy, altitude: location.coords.altitude, mocked: location.mocked,
});
// Registered before Expo Router, also runs when the OS starts JS without a screen.
if (Platform.OS !== 'web' && !expoGo && !TaskManager.isTaskDefined(LOCATION_TASK)) {
  TaskManager.defineTask<{ locations: LocationObject[] }>(LOCATION_TASK, async ({ data, error }) => {
    try {
      if (error) { await trackingWarning('Геолокация: ' + error.message); return; }
      if (data?.locations) await appendFixes(data.locations.map(toFix));
    } catch (cause) {
      // Do not report success or fabricate a track when durable storage fails.
      console.error('Walk location could not be saved', cause);
      try { await trackingWarning('Не удалось сохранить GPS-точки. Проверь свободное место и запись прогулки.'); } catch { /* DB unavailable; retained points remain intact. */ }
    }
  });
}
