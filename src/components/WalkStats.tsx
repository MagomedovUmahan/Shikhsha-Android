import { Text, View } from 'react-native';
import { activeMilliseconds, duration, pace, type Walk, walkSteps } from '../tracking/model';
import { useTheme } from '../state/ThemeProvider';

export function WalkStats({ walk, now }: { walk: Walk; now: number }) {
  const { colors, ui } = useTheme();
  const ms = activeMilliseconds(walk, now), steps = walkSteps(walk);
  const items = [
    ['ВРЕМЯ В ЗАПИСИ', duration(ms), 'без пауз'],
    ['ШАГИ', steps.count === null ? '—' : steps.count.toLocaleString('ru-RU'), steps.partial ? 'доступная часть' : 'за прогулку'],
    ['СРЕДНИЙ ТЕМП', pace(walk.distance, ms), 'мин/км'],
    ['СРЕДНЯЯ СКОРОСТЬ', walk.distance >= 30 && ms > 0 ? (walk.distance / ms * 3600).toFixed(1) : '—', 'км/ч'],
  ];
  return <View style={{ gap: 18 }}>
    <View style={{ gap: 3 }}><Text style={ui.label}>ПРОЙДЕНО ПО GPS</Text><Text style={{ color: colors.ink, fontSize: 52, lineHeight: 62, fontWeight: '700', letterSpacing: -2 }}>{(walk.distance / 1000).toFixed(2).replace('.', ',')} <Text style={{ fontSize: 19, color: colors.muted, letterSpacing: 0 }}>км</Text></Text></View>
    <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 12 }}>{items.map(([label, value, unit]) => <View key={label} style={{ width: '47%', flexGrow: 1, backgroundColor: colors.soft, padding: 15, borderRadius: 18, gap: 7 }}>
      <Text style={[ui.label, { fontSize: 8, letterSpacing: 1 }]}>{label}</Text><Text style={{ color: colors.ink, fontSize: 23, fontWeight: '600', fontVariant: ['tabular-nums'] }}>{value}</Text><Text style={ui.small}>{unit}</Text>
    </View>)}</View>
  </View>;
}
