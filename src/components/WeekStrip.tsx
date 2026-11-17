import { Pressable, StyleSheet, Text, View } from 'react-native';
import Svg, { Circle } from 'react-native-svg';
import { type Day, number } from '../lib/activity';
import { useTheme } from '../state/ThemeProvider';

export function WeekStrip({ days, goal, selected, onSelect }: { days: Day[]; goal: number; selected: string; onSelect(day: Day): void }) {
  const { colors } = useTheme();
  return <View style={styles.row}>
    {days.map(day => {
      const active = selected === day.key;
      const progress = Math.min(1, (day.steps ?? 0) / goal);
      return <Pressable key={day.key} accessibilityRole="button" accessibilityState={{ selected: active }}
        accessibilityLabel={`${day.date.toLocaleDateString('ru-RU')}, ${day.steps === null ? 'нет данных' : number(day.steps) + ' шагов'}`}
        onPress={() => onSelect(day)} style={[styles.day, { backgroundColor: active ? colors.card : 'transparent', borderColor: active ? colors.line : 'transparent' }]}>
        <Text style={{ color: active ? colors.ink : colors.muted, fontSize: 10 }}>{day.date.toLocaleDateString('ru-RU', { weekday: 'short' })}</Text>
        <View style={styles.circle}><Svg width={33} height={33} style={StyleSheet.absoluteFill} accessibilityElementsHidden>
          <Circle cx={16.5} cy={16.5} r={14} stroke={colors.line} strokeWidth={2} fill="none" />
          {progress > 0 && <Circle cx={16.5} cy={16.5} r={14} stroke={colors.green} strokeWidth={2.5} fill="none"
            strokeDasharray="88 88" strokeDashoffset={88 * (1 - progress)} rotation={-90} origin="16.5,16.5" strokeLinecap="round" />}
        </Svg><Text style={{ fontSize: 11, fontWeight: '600', color: active ? colors.green : colors.ink }}>{day.date.getDate()}</Text></View>
      </Pressable>;
    })}
  </View>;
}
const styles = StyleSheet.create({
  row: { flexDirection: 'row', gap: 3, justifyContent: 'space-between' },
  day: { flex: 1, alignItems: 'center', gap: 6, borderRadius: 17, borderWidth: 1, paddingVertical: 8, minHeight: 63 },
  circle: { width: 33, height: 33, alignItems: 'center', justifyContent: 'center' },
});
