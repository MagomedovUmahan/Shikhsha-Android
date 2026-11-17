import { Pressable, StyleSheet, Text, View } from 'react-native';
import Svg, { Line } from 'react-native-svg';
import { number, type Day } from '../lib/activity';
import { useTheme } from '../state/ThemeProvider';

export function WeekChart({ days, selected, onSelect, goal }: {
  days: Day[]; selected?: string; onSelect?: (day: Day) => void; goal?: number;
}) {
  const { colors } = useTheme();
  const max = Math.max(goal ?? 1000, ...days.map(d => d.steps ?? 0));
  const compact = days.length > 7;
  return <View style={{ paddingTop: 8 }}>
    <View style={styles.plot}>
      <Svg width="100%" height={124} style={StyleSheet.absoluteFill} accessibilityElementsHidden>
        {[0, 0.5, 1].map(ratio => <Line key={ratio} x1="0" x2="100%" y1={ratio * 120 + 2} y2={ratio * 120 + 2} stroke={colors.line} strokeWidth={1} strokeDasharray="2 5" />)}
        {goal !== undefined && <Line x1="0" x2="100%" y1={122 - goal / max * 120} y2={122 - goal / max * 120} stroke={colors.green} strokeOpacity={0.45} strokeDasharray="4 4" />}
      </Svg>
      {days.map(day => <Pressable key={day.key} onPress={() => onSelect?.(day)} disabled={!onSelect}
        accessibilityRole={onSelect ? 'button' : undefined}
        accessibilityLabel={`${day.date.toLocaleDateString('ru-RU')}: ${day.steps === null ? 'нет данных' : number(day.steps) + ' шагов'}`}
        accessibilityState={{ selected: selected === day.key }} style={styles.column}>
        <View style={[styles.bar, {
          width: compact ? '65%' : '62%', maxWidth: 28,
          height: day.steps === null ? 2 : Math.max(4, day.steps / max * 120),
          backgroundColor: day.steps === null ? colors.line : selected === day.key ? colors.green : colors.green + '65',
          borderRadius: compact ? 2 : 6,
        }]} />
      </Pressable>)}
    </View>
    <View style={[styles.labels, { marginTop: 10 }]}>
      {compact ? [days[0], days[Math.floor(days.length / 2)], days[days.length - 1]].map(day => <Text key={day.key} style={{ fontSize: 10, color: colors.muted }}>{day.date.toLocaleDateString('ru-RU', { day: 'numeric', month: 'short' })}</Text>)
        : days.map(day => <Text key={day.key} style={{ flex: 1, textAlign: 'center', fontSize: 11, color: day.key === selected ? colors.ink : colors.muted }}>{day.date.toLocaleDateString('ru-RU', { weekday: 'short' })}</Text>)}
    </View>
  </View>;
}
const styles = StyleSheet.create({
  plot: { height: 124, flexDirection: 'row', gap: 2, alignItems: 'flex-end', paddingTop: 2 },
  column: { flex: 1, height: 122, alignItems: 'center', justifyContent: 'flex-end', minWidth: 0 },
  bar: { minWidth: 1 },
  labels: { flexDirection: 'row', justifyContent: 'space-between' },
});
