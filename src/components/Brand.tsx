import { Pressable, StyleSheet, Text, View } from 'react-native';
import Svg, { Path } from 'react-native-svg';
import { useTheme } from '../state/ThemeProvider';
import Ionicons from '@expo/vector-icons/Ionicons';
import { router } from 'expo-router';
import { Eagle } from './Caucasus';

export function Tree({ size = 28, color }: { size?: number; color?: string }) {
  const { colors } = useTheme();
  return <Svg width={size} height={size} viewBox="0 0 32 32" accessibilityElementsHidden>
    <Path d="M16 3 7 14h5L5 23h9v6h4v-6h9l-7-9h5Z" fill={color ?? colors.green} />
  </Svg>;
}
export function Brand({ compact = false }: { compact?: boolean }) {
  const { colors } = useTheme();
  return <View style={styles.row}>
    <View style={[styles.mark, { backgroundColor: colors.pale, borderWidth: 1, borderColor: colors.green + '50' }]}><Eagle size={33} /></View>
    <View><Text style={[styles.name, { color: colors.ink }]}>ШИХША<Text style={{ color: colors.green }}> /</Text></Text>
      {!compact && <Text style={[styles.caption, { color: colors.muted }]}>EAGLE VISION</Text>}</View>
  </View>;
}
export function PageHeader({ title, subtitle }: { title?: string; subtitle?: string }) {
  const { colors, ui } = useTheme();
  return <View style={ui.row}>
    {title ? <View style={{ flex: 1, gap: 4 }}><Text style={ui.title}>{title}</Text>{subtitle && <Text style={ui.small}>{subtitle}</Text>}</View> : <Brand />}
    <Pressable accessibilityRole="button" accessibilityLabel="Открыть настройки" onPress={() => router.navigate('/settings')}
      style={[styles.settings, { borderColor: colors.line, backgroundColor: colors.card }]}>
      <Ionicons name="options-outline" size={20} color={colors.ink} />
    </Pressable>
  </View>;
}
const styles = StyleSheet.create({
  row: { flexDirection: 'row', alignItems: 'center', gap: 10 },
  mark: { width: 44, height: 44, borderRadius: 12, justifyContent: 'center', alignItems: 'center' },
  name: { fontSize: 22, letterSpacing: 3, fontWeight: '700' },
  caption: { fontSize: 7, fontWeight: '600', letterSpacing: 1.7, marginTop: 3 },
  settings: { width: 44, height: 44, borderRadius: 16, borderWidth: 1, alignItems: 'center', justifyContent: 'center' },
});
