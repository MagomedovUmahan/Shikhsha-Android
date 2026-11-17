import Svg, { Circle, Defs, G, LinearGradient, Path, Stop } from 'react-native-svg';
import { useTheme } from '../state/ThemeProvider';

export function Eagle({ size = 32, color }: { size?: number; color?: string }) {
  const { colors } = useTheme();
  return <Svg width={size} height={size} viewBox="0 0 64 64" accessibilityElementsHidden>
    <Path d="M5 19 25 27 32 20 39 27 59 19 49 34 38 35 32 49 26 35 15 34Z" fill="none" stroke={color ?? colors.green} strokeWidth={3} strokeLinejoin="round" />
    <Path d="m15 26 13 7 4-5 4 5 13-7M28 39h8M32 20v-6l6 3-6 3" fill="none" stroke={color ?? colors.green} strokeWidth={2} />
  </Svg>;
}
export function Ornament() {
  const { colors } = useTheme();
  return <Svg height={16} width="100%" viewBox="0 0 360 16" preserveAspectRatio="none" accessibilityElementsHidden>
    <Path d="M0 8h126m108 0h126M140 8l8-6 8 6-8 6Zm24 0 8-6 8 6-8 6Zm24 0 8-6 8 6-8 6Zm24 0 8-6 8 6-8 6Z" fill="none" stroke={colors.green} strokeWidth={0.8} opacity={0.5} />
  </Svg>;
}
export function MountainScene({ height = 190 }: { height?: number }) {
  const { colors, isDark } = useTheme();
  return <Svg width="100%" height={height} viewBox="0 0 400 190" preserveAspectRatio="xMidYMax slice" accessibilityElementsHidden>
    <Defs><LinearGradient id="summit-fill" x1="0" y1="0" x2="0" y2="1"><Stop offset="0" stopColor={colors.secondary} stopOpacity={isDark ? 0.25 : 0.18} /><Stop offset="1" stopColor={colors.card} stopOpacity={0} /></LinearGradient></Defs>
    <G fill="none" stroke={colors.secondary} strokeWidth={0.5} opacity={0.18}>
      <Path d="M0 58h400M0 108h400M0 158h400M50 0v190M150 0v190M250 0v190M350 0v190" />
      <Circle cx={275} cy={80} r={58} /><Circle cx={275} cy={80} r={78} strokeDasharray="2 7" />
    </G>
    <Path d="M-15 190 57 99 105 146 177 73 208 109 273 28 327 102 351 81 420 190Z" fill="url(#summit-fill)" stroke={colors.secondary} strokeOpacity={0.4} />
    <Path d="m208 109 65-81 54 74-41-25-14 8-12-18ZM105 146l72-73 31 36-27-10-9 19-10-4Z" fill={colors.ink} opacity={isDark ? 0.23 : 0.18} />
    <Path d="M0 184 80 153 118 171 184 130 226 159 278 119 322 153 400 133" fill="none" stroke={colors.green} strokeWidth={1.2} opacity={0.6} />
    <Path d="m273 20 0 16m-8-8h16" stroke={colors.green} strokeWidth={1} /><Circle cx={273} cy={28} r={3} fill={colors.green} />
  </Svg>;
}
