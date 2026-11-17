import { useEffect, useState } from 'react';
import { AccessibilityInfo, Animated, Easing, StyleSheet, Text, View, useWindowDimensions } from 'react-native';
import Svg, { Circle, Defs, LinearGradient, Stop } from 'react-native-svg';
import { Eagle } from './Caucasus';
import { number } from '../lib/activity';
import { useTheme } from '../state/ThemeProvider';

export function ProgressRing({ steps, goal, label = 'шагов сегодня' }: { steps: number | null; goal: number; label?: string }) {
  const { colors } = useTheme();
  const { width } = useWindowDimensions();
  const size = Math.max(180, Math.min(262, width - 100));
  const radius = (size - 24) / 2;
  const length = 2 * Math.PI * radius;
  const progress = Math.min(1, Math.max(0, (steps ?? 0) / goal));
  const [animated] = useState(() => new Animated.Value(0));
  const [displayed, setDisplayed] = useState(0);
  useEffect(() => {
    const listener = animated.addListener(({ value }) => setDisplayed(value));
    return () => animated.removeListener(listener);
  }, [animated]);
  useEffect(() => {
    let mounted = true;
    let animation: Animated.CompositeAnimation | undefined;
    const animate = (reduce: boolean) => {
      animated.stopAnimation();
      if (reduce) animated.setValue(progress);
      else {
        animation = Animated.timing(animated, { toValue: progress, duration: 650, easing: Easing.out(Easing.cubic), useNativeDriver: false });
        animation.start();
      }
    };
    void AccessibilityInfo.isReduceMotionEnabled().then(reduce => { if (mounted) animate(reduce); }).catch(() => { if (mounted) animated.setValue(progress); });
    const subscription = AccessibilityInfo.addEventListener('reduceMotionChanged', animate);
    return () => { mounted = false; animation?.stop(); subscription.remove(); };
  }, [animated, progress]);
  return <View accessible accessibilityLabel={steps === null ? 'Шаги ещё не получены' : `${number(steps)} шагов из ${number(goal)}`}
    style={[styles.container, { width: size, height: size }]}>
    <Svg width={size} height={size} style={StyleSheet.absoluteFill} accessibilityElementsHidden>
      <Defs><LinearGradient id="progress" x1="0" y1="0" x2="1" y2="1"><Stop offset="0" stopColor={colors.green} /><Stop offset="1" stopColor={colors.green} stopOpacity={0.62} /></LinearGradient></Defs>
      <Circle cx={size / 2} cy={size / 2} r={radius} fill="none" stroke={colors.soft} strokeWidth={13} />
      <Circle cx={size / 2} cy={size / 2} r={radius - 17} fill="none" stroke={colors.line} strokeWidth={1} strokeDasharray="1 7" />
      <Circle cx={size / 2} cy={size / 2} r={radius} fill="none" stroke="url(#progress)"
        strokeWidth={13} strokeLinecap="round" strokeDasharray={`${length} ${length}`}
        strokeDashoffset={length * (1 - displayed)}
        opacity={progress > 0 ? 1 : 0} rotation={-90} origin={`${size / 2}, ${size / 2}`} />
    </Svg>
    <Eagle size={30} />
    <Text style={[styles.count, { color: colors.ink, fontSize: size < 225 ? 45 : 61 }]} adjustsFontSizeToFit numberOfLines={1}>{steps === null ? '—' : number(steps)}</Text>
    <Text style={{ color: colors.muted, fontSize: 12 }}>{label}</Text>
    <View style={[styles.goal, { backgroundColor: colors.pale }]}><Text style={{ color: colors.green, fontSize: 10, fontWeight: '700' }}>Цель {number(goal)}</Text></View>
  </View>;
}
const styles = StyleSheet.create({
  container: { justifyContent: 'center', alignItems: 'center', alignSelf: 'center', gap: 5 },
  count: { fontWeight: '700', letterSpacing: -2.7, fontVariant: ['tabular-nums'], maxWidth: '80%' },
  goal: { marginTop: 7, borderRadius: 20, paddingHorizontal: 12, paddingVertical: 6 },
});
