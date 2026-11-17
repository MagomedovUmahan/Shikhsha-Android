import Ionicons from '@expo/vector-icons/Ionicons';
import { ActivityIndicator, Linking, Platform, Pressable, Text, View } from 'react-native';
import { useActivity } from '../state/ActivityProvider';
import { useTheme } from '../state/ThemeProvider';

export function SensorStatus() {
  const { colors, ui } = useTheme();
  const { supported, snapshot, loading, busy, start, error, notice } = useActivity();
  if (loading) return <View style={[ui.card, ui.row]}><ActivityIndicator color={colors.green} /><Text style={ui.small}>Проверяем шагомер…</Text></View>;
  const unavailable = !supported || !snapshot.available;
  const active = snapshot.running && snapshot.permission === 'granted' && !error;
  const paused = !snapshot.enabled && Object.keys(snapshot.days).length > 0;
  const heading = !supported ? 'Подключи телефон' : !snapshot.available ? 'Датчик недоступен'
    : error ? 'Нужно проверить доступ' : active ? 'Каждый шаг на счету' : paused ? 'Счётчик на паузе' : 'Готов к первой прогулке?';
  const detail = !supported ? 'Реальные шаги доступны в Expo Go на iPhone или отдельной сборке. В браузере показан интерфейс без вымышленных данных.'
    : !snapshot.available ? 'На устройстве нет доступного аппаратного шагомера. Проверь на физическом телефоне.'
    : error || (active ? Platform.OS === 'android' ? 'Подсчёт работает в фоне. Бери телефон с собой.' : 'Данные поступают из системного шагомера iPhone.'
      : paused ? 'История сохранена. Возобнови подсчёт, когда будешь готов. Шаги паузы не добавятся.' : 'Разреши доступ к движению — остальное сделает шагомер.');
  return <View style={[ui.card, { padding: 17 }]}>
    <View style={[ui.row, { justifyContent: 'flex-start', alignItems: 'flex-start' }]}>
      <View style={{ width: 36, height: 36, borderRadius: 12, backgroundColor: active ? colors.pale : colors.soft, alignItems: 'center', justifyContent: 'center' }}>
        <Ionicons name={active ? 'checkmark-circle-outline' : 'phone-portrait-outline'} color={active ? colors.green : colors.muted} size={20} />
      </View>
      <View style={{ flex: 1, gap: 3 }}><Text style={[ui.body, { fontWeight: '600' }]}>{heading}</Text><Text style={ui.small}>{detail}</Text></View>
    </View>
    {notice && <Text style={ui.small}>{notice}</Text>}
    {!unavailable && !active && <Pressable accessibilityRole="button" disabled={busy} onPress={() => void start()}
      style={[ui.button, busy && { opacity: 0.5 }]}><Text style={ui.buttonText}>{busy ? 'Подключаем…' : paused ? 'Продолжить подсчёт' : 'Подключить шагомер'}</Text></Pressable>}
    {!unavailable && snapshot.permission === 'denied' && <Pressable accessibilityRole="button"
      onPress={() => void Linking.openSettings().catch(() => undefined)} style={{ minHeight: 44, justifyContent: 'center' }}>
      <Text style={[ui.body, { color: colors.green }]}>Открыть настройки телефона →</Text>
    </Pressable>}
  </View>;
}
