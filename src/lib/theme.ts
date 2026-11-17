import { StyleSheet } from 'react-native';

export const darkColors = {
  background: '#090E12', card: '#111B21', ink: '#EEF3F5', muted: '#9BAEB8',
  green: '#89DEB5', pale: '#142E28', line: '#2B3D46', accent: '#C6D4DD',
  warning: '#E7C294', warningBg: '#302921', onPrimary: '#08251B',
  secondary: '#8ABDCF', soft: '#19272F',
};
export type ThemeColors = { [K in keyof typeof darkColors]: string };
export const lightColors: ThemeColors = {
  background: '#EEF3F4', card: '#FFFFFF', ink: '#152C34', muted: '#58707D',
  green: '#246C50', pale: '#DFEEE7', line: '#C8D9DE', accent: '#426779',
  warning: '#805026', warningBg: '#FFF0DE', onPrimary: '#FFFFFF',
  secondary: '#386D86', soft: '#E6EEF0',
};
export function makeUI(colors: ThemeColors) {
  return StyleSheet.create({
    page: { flex: 1, backgroundColor: colors.background },
    content: { paddingHorizontal: 20, paddingTop: 16, paddingBottom: 128, gap: 20, width: '100%', maxWidth: 560, alignSelf: 'center' },
    title: { fontSize: 30, lineHeight: 37, fontWeight: '700', color: colors.ink, letterSpacing: -0.7 },
    subtitle: { fontSize: 14, lineHeight: 21, color: colors.muted },
    sectionTitle: { fontSize: 18, fontWeight: '600', color: colors.ink, letterSpacing: -0.4 },
    card: { backgroundColor: colors.card, borderRadius: 18, padding: 20, gap: 14, borderWidth: 1, borderColor: colors.line },
    row: { flexDirection: 'row', alignItems: 'center', justifyContent: 'space-between', gap: 12 },
    label: { color: colors.muted, fontSize: 10, fontWeight: '600', letterSpacing: 1.6 },
    body: { color: colors.ink, fontSize: 14, lineHeight: 22 },
    small: { color: colors.muted, fontSize: 12, lineHeight: 19 },
    button: { minHeight: 52, borderRadius: 12, backgroundColor: colors.green, paddingHorizontal: 18, paddingVertical: 13, alignItems: 'center', justifyContent: 'center' },
    buttonText: { color: colors.onPrimary, fontSize: 14, fontWeight: '700' },
  });
}
