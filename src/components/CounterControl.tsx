import { useState } from 'react';
import Ionicons from '@expo/vector-icons/Ionicons';
import { Pressable, Text, View } from 'react-native';
import { useActivity } from '../state/ActivityProvider';
import { useWalk } from '../state/WalkProvider';
import { useTheme } from '../state/ThemeProvider';

export function CounterControl({ compact = false }: { compact?: boolean }) {
  const { colors, ui } = useTheme();
  const { snapshot, pause, start, supported, busy, loading } = useActivity();
  const walk = useWalk();
  const [changing, setChanging] = useState(false);
  const [error, setError] = useState('');
  const paused = !snapshot.enabled && Object.keys(snapshot.days).length > 0;
  const disabled = changing || busy || walk.busy || loading || !supported || !snapshot.available;
  const toggle = async () => {
    setChanging(true); setError('');
    try {
      if (snapshot.enabled) {
        if (walk.active?.walk.status === 'recording') {
          await walk.pause();
          const { activeWalk } = await import('../tracking/store');
          if ((await activeWalk())?.status === 'recording') { setError('Сначала приостанови прогулку на её экране.'); return; }
        }
        await pause();
      } else await start();
    } catch { setError('Не удалось изменить состояние. Проверь данные на экране прогулки и повтори.'); }
    finally { setChanging(false); }
  };
  return <View style={{ gap: 8, ...(compact ? { maxWidth: 130 } : {}) }}>
    <Pressable accessibilityRole="button" accessibilityLabel={snapshot.enabled ? 'Пауза счётчика' : paused ? 'Продолжить подсчёт' : 'Начать считать шаги'} disabled={disabled} onPress={() => void toggle()}
      style={[ui.button, { flexDirection: 'row', gap: compact ? 6 : 10, minHeight: compact ? 44 : 52, paddingHorizontal: compact ? 10 : 18, paddingVertical: compact ? 8 : 13, backgroundColor: snapshot.enabled ? colors.soft : colors.green, borderWidth: 1, borderColor: snapshot.enabled ? colors.line : colors.green, opacity: disabled ? 0.5 : 1 }]}>
      <Ionicons name={snapshot.enabled ? 'pause' : 'play'} size={16} color={snapshot.enabled ? colors.ink : colors.onPrimary} />
      <Text style={[ui.buttonText, { color: snapshot.enabled ? colors.ink : colors.onPrimary, fontSize: compact ? 10 : 14 }]}>{changing || busy ? 'Обновляем…' : compact ? snapshot.enabled ? 'Пауза' : paused ? 'Продолжить' : 'Старт' : snapshot.enabled ? 'Пауза счётчика' : paused ? 'Продолжить подсчёт' : 'Начать считать шаги'}</Text>
    </Pressable>
    {!!error && <Text style={[ui.small, { color: colors.warning }]} accessibilityLiveRegion="polite">{error}</Text>}
    {!compact && <Text style={[ui.small, { textAlign: 'center', fontSize: 10 }]}>{snapshot.enabled ? walk.active?.walk.status === 'recording' ? 'Пауза счётчика также приостановит текущую прогулку.' : 'Шаги сохраняются на телефоне.' : paused ? 'Счётчик на паузе. История сохранена; шаги паузы не добавятся.' : 'Подключи датчик, чтобы начать свой путь.'}</Text>}
  </View>;
}
