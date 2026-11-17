import { useState } from 'react';
import Ionicons from '@expo/vector-icons/Ionicons';
import { Linking, Platform, Pressable, StyleSheet, Text, TextInput, View } from 'react-native';
import { Brand } from '../components/Brand';
import { Screen } from '../components/Screen';
import { SensorStatus } from '../components/SensorStatus';
import { GoalPlanner } from '../components/GoalPlanner';
import { CounterControl } from '../components/CounterControl';
import { type ThemeColors } from '../lib/theme';
import { useActivity } from '../state/ActivityProvider';
import { useTheme, type Appearance } from '../state/ThemeProvider';
import { iphoneExpoGo } from '../lib/runtime';

export default function Settings() {
  const { colors, ui, appearance, setAppearance } = useTheme();
  const styles = makeStyles(colors);
  const { settings, saveSettings, loading } = useActivity();
  const [goalDraft, setGoal] = useState<string | null>(null);
  const [strideDraft, setStride] = useState<string | null>(null);
  const goal = goalDraft ?? String(settings.goal);
  const stride = strideDraft ?? String(settings.strideCm);
  const [result, setResult] = useState('');
  const [themeResult, setThemeResult] = useState('');
  const [saving, setSaving] = useState(false);
  const [themeSaving, setThemeSaving] = useState(false);
  const save = async () => {
    if (!/^\d+$/.test(goal) || !/^\d+$/.test(stride)) { setResult('Введите целые числа без пробелов.'); return; }
    const parsedGoal = Number(goal), parsedStride = Number(stride);
    if (parsedGoal < 1000 || parsedGoal > 50000) { setResult('Цель должна быть от 1 000 до 50 000 шагов.'); return; }
    if (parsedStride < 30 || parsedStride > 150) { setResult('Длина шага должна быть от 30 до 150 см.'); return; }
    setSaving(true);
    try { await saveSettings({ ...settings, goal: parsedGoal, strideCm: parsedStride }); setResult('Настройки сохранены.'); }
    catch { setResult('Не удалось сохранить настройки. Попробуйте ещё раз.'); }
    finally { setSaving(false); }
  };
  const chooseTheme = async (value: Appearance) => {
    setThemeSaving(true);
    try { await setAppearance(value); setThemeResult('Оформление сохранено.'); }
    catch { setThemeResult('Не удалось сохранить оформление. Попробуйте ещё раз.'); }
    finally { setThemeSaving(false); }
  };
  return <Screen>
    <Brand compact /><View style={{ gap: 5 }}><Text style={ui.title}>Настройки</Text><Text style={ui.subtitle}>Твой темп. Твои правила.</Text></View>
    <GoalPlanner onApplied={() => setGoal(null)} />
    <View style={ui.card}>
      <View style={[ui.row, { justifyContent: 'flex-start' }]}><Ionicons name="color-palette-outline" size={22} color={colors.green} /><Text style={ui.sectionTitle}>Оформление</Text></View>
      <View style={{ flexDirection: 'row', gap: 8 }}>
        {([
          { value: 'dark', label: 'Тёмная', icon: 'moon-outline' },
          { value: 'light', label: 'Светлая', icon: 'sunny-outline' },
          { value: 'system', label: 'Системная', icon: 'phone-portrait-outline' },
        ] as const).map(theme => <Pressable key={theme.value} accessibilityRole="button"
          accessibilityLabel={theme.label + ' тема'} accessibilityState={{ selected: appearance === theme.value, disabled: themeSaving }}
          disabled={themeSaving} onPress={() => void chooseTheme(theme.value)}
          style={{ flex: 1, minHeight: 76, borderRadius: 17, alignItems: 'center', justifyContent: 'center', gap: 9, borderWidth: 1,
            backgroundColor: appearance === theme.value ? colors.pale : colors.soft, borderColor: appearance === theme.value ? colors.green : 'transparent' }}>
          <Ionicons name={theme.icon} size={21} color={appearance === theme.value ? colors.green : colors.muted} />
          <Text style={{ fontSize: 10, color: appearance === theme.value ? colors.green : colors.muted, fontWeight: '600' }}>{theme.label}</Text>
        </Pressable>)}
      </View>
      {!!themeResult && <Text style={ui.small} accessibilityLiveRegion="polite">{themeResult}</Text>}
    </View>
    <View style={ui.card}>
      <View style={[ui.row, { justifyContent: 'flex-start' }]}><Ionicons name="flag-outline" size={21} color={colors.green} /><Text style={ui.sectionTitle}>Дневная цель</Text></View>
      <Text style={ui.small}>Выбери комфортное число от 1 000 до 50 000 шагов.</Text>
      <View style={ui.row}><TextInput accessibilityLabel="Дневная цель в шагах" value={goal}
        onChangeText={text => { setGoal(text); setResult(''); }} keyboardType="number-pad" maxLength={5} style={styles.input} placeholderTextColor={colors.muted} />
        <Text style={ui.body}>шагов</Text></View>
      <View style={styles.presets}>{[5000, 8000, 10000].map(value => <Pressable key={value} accessibilityRole="button"
        accessibilityState={{ selected: goal === String(value) }} onPress={() => { setGoal(String(value)); setResult(''); }}
        style={[styles.chip, goal === String(value) && styles.chipActive]}>
        <Text style={[styles.chipText, goal === String(value) && { color: colors.onPrimary }]}>{value.toLocaleString('ru-RU')}</Text></Pressable>)}</View>
      <View style={{ height: 1, backgroundColor: colors.line, marginVertical: 8 }} />
      <Text style={ui.sectionTitle}>Длина шага</Text>
      <Text style={ui.small}>Для примерного расстояния. Пройди 10 метров обычным шагом и раздели 1 000 см на число шагов.</Text>
      <View style={ui.row}><TextInput accessibilityLabel="Длина шага в сантиметрах" value={stride}
        onChangeText={text => { setStride(text); setResult(''); }} keyboardType="number-pad" maxLength={3} style={styles.input} placeholderTextColor={colors.muted} />
        <Text style={ui.body}>см</Text></View>
      <Pressable accessibilityRole="button" onPress={() => void save()} disabled={saving || loading} style={[ui.button, (saving || loading) && { opacity: 0.5 }]}>
        <Text style={ui.buttonText}>{saving ? 'Сохраняем…' : 'Сохранить настройки'}</Text></Pressable>
      {!!result && <Text accessibilityLiveRegion="polite" style={ui.small}>{result}</Text>}
    </View>
    <SensorStatus />
    <CounterControl />
    {Platform.OS !== 'web' && <Pressable accessibilityRole="button" onPress={() => void Linking.openSettings().catch(() => undefined)} style={[ui.row, { minHeight: 48 }]}>
      <Text style={[ui.body, { flex: 1 }]}>Разрешения в настройках телефона</Text><Ionicons name="open-outline" size={19} color={colors.green} /></Pressable>}
    <View style={ui.card}><View style={[ui.row, { justifyContent: 'flex-start' }]}><Ionicons name="shield-checkmark-outline" size={22} color={colors.green} /><Text style={[ui.sectionTitle, { flex: 1 }]}>Твои данные — у тебя</Text></View>
      <Text style={ui.small}>Шаги, маршруты и настройки хранятся на устройстве. Без аккаунтов и рекламы. Координаты маршрута отправляются только при выбранном тобой экспорте GPX.</Text>
      <Text style={ui.small}>{iphoneExpoGo ? 'В Expo Go на iPhone карта загружается через Apple Maps. Apple получает запросы видимой области. GPS записывается только при открытом приложении.' : 'Подложка карты загружается через OpenFreeMap. Сервис получает IP-адрес и запросы тайлов видимой области. Геопозиция используется только во время записи прогулки.'}</Text>
      <Text style={ui.small}>Вес и цель хранятся только на этом телефоне. Пауза сохраняет историю и исключает шаги перерыва. После перезагрузки или принудительной остановки проверь статус счётчика и разрешения.</Text>
      <Text style={ui.small}>Телефон нужно носить с собой. Датчик может передавать шаги с задержкой. Дневное расстояние — оценка по длине шага; расстояние прогулки считается по GPS отдельно.</Text>
      {Platform.OS === 'android' && <Text style={ui.small}>Пачка шагов около полуночи относится к дате последнего события. Распределение между соседними днями может быть неточным.</Text>}
    </View>
    <Text style={[ui.small, { textAlign: 'center', letterSpacing: 2 }]}>EAGLE VISION / ШИХША · 1.3.0</Text>
  </Screen>;
}
const makeStyles = (colors: ThemeColors) => StyleSheet.create({
  input: { flex: 1, minWidth: 0, minHeight: 56, borderRadius: 15, backgroundColor: colors.soft, color: colors.ink, fontSize: 25, fontWeight: '600', paddingHorizontal: 16, fontVariant: ['tabular-nums'] },
  presets: { flexDirection: 'row', gap: 10 },
  chip: { flex: 1, minHeight: 44, borderRadius: 13, backgroundColor: colors.soft, alignItems: 'center', justifyContent: 'center' },
  chipActive: { backgroundColor: colors.green },
  chipText: { color: colors.green, fontSize: 14, fontWeight: '600' },
});
