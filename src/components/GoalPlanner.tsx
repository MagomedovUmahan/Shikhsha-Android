import { useState } from 'react';
import { Linking, Pressable, Text, TextInput, View } from 'react-native';
import { goalPlan, parseProfile, walkingEnergy } from '../lib/goals';
import { lastDays, number } from '../lib/activity';
import { useActivity } from '../state/ActivityProvider';
import { useTheme } from '../state/ThemeProvider';
import { Ornament } from './Caucasus';

export function GoalPlanner({ onApplied }: { onApplied?: () => void }) {
  const { colors, ui } = useTheme();
  const { settings, saveSettings, snapshot, loading } = useActivity();
  const profile = settings.profile;
  const [weightDraft, setWeight] = useState<string | null>(null), [baselineDraft, setBaseline] = useState<string | null>(null);
  const [purposeDraft, setPurpose] = useState<'maintain' | 'loss' | null>(null);
  const [result, setResult] = useState(''), [saving, setSaving] = useState(false), [details, setDetails] = useState(false);
  const weight = weightDraft ?? (profile ? String(profile.weightKg) : '');
  const baseline = baselineDraft ?? (profile ? String(profile.baselineSteps) : '');
  const purpose = purposeDraft ?? profile?.purpose ?? 'maintain';
  const parsed = /^\d{2,3}([.,]\d)?$/.test(weight) && /^\d+$/.test(baseline)
    ? parseProfile({ weightKg: Number(weight.replace(',', '.')), baselineSteps: Number(baseline), purpose }) : null;
  const plan = parsed ? goalPlan(parsed) : null;
  const known = lastDays(snapshot.days).filter(day => day.steps !== null);
  const average = known.length ? Math.max(1000, Math.min(30000, Math.round(known.reduce((sum, d) => sum + d.steps!, 0) / known.length / 250) * 250)) : null;
  const input = { flex: 1, minWidth: 0, minHeight: 54, paddingHorizontal: 15, borderRadius: 12, backgroundColor: colors.soft, color: colors.ink, fontSize: 23, fontWeight: '600' as const };
  const save = async () => {
    if (!parsed || !plan) { setResult('Укажи вес от 30 до 300 кг (до одного знака после запятой) и привычную активность от 1 000 до 30 000 шагов.'); return; }
    setSaving(true); setResult('');
    try { await saveSettings({ ...settings, profile: parsed, goal: plan.dailyGoal }); onApplied?.(); setResult(`План сохранён. Дневная цель: ${number(plan.dailyGoal)} шагов.`); }
    catch { setResult('Не удалось сохранить план. Попробуй ещё раз.'); }
    finally { setSaving(false); }
  };
  const remove = async () => {
    setSaving(true);
    try { await saveSettings({ ...settings, profile: null }); setWeight(''); setBaseline(''); setPurpose(null); setResult('Вес и профиль удалены. Дневную цель можно настроить вручную.'); }
    catch { setResult('Не удалось удалить профиль.'); }
    finally { setSaving(false); }
  };
  return <View style={ui.card}>
    <Text style={ui.label}>ЛИЧНАЯ ЦЕЛЬ / ПЛАН АКТИВНОСТИ</Text>
    <Text style={ui.sectionTitle}>В своём весе. В своём ритме.</Text>
    <View style={{ flexDirection: 'row', gap: 9 }}>{([
      ['maintain', 'Поддержание'], ['loss', 'Похудение'],
    ] as const).map(([value, label]) => <Pressable key={value} accessibilityRole="button" accessibilityState={{ selected: purpose === value }} onPress={() => { setPurpose(value); setResult(''); }}
      style={{ flex: 1, minHeight: 49, padding: 10, borderRadius: 12, borderWidth: 1, borderColor: purpose === value ? colors.green : colors.line, backgroundColor: purpose === value ? colors.pale : colors.soft, alignItems: 'center', justifyContent: 'center' }}>
      <Text style={{ color: purpose === value ? colors.green : colors.muted, fontSize: 12, fontWeight: '600' }}>{label}</Text>
    </Pressable>)}</View>
    <Text style={ui.body}>Текущий вес</Text>
    <View style={ui.row}><TextInput accessibilityLabel="Вес в килограммах" value={weight} onChangeText={value => { setWeight(value); setResult(''); }} placeholder="Например, 75" placeholderTextColor={colors.muted} keyboardType="decimal-pad" maxLength={5} style={input} /><Text style={ui.body}>кг</Text></View>
    <Text style={ui.body}>Сколько обычно проходишь за день?</Text>
    <View style={ui.row}><TextInput accessibilityLabel="Привычное число шагов" value={baseline} onChangeText={value => { setBaseline(value); setResult(''); }} placeholder="Твоя обычная активность" placeholderTextColor={colors.muted} keyboardType="number-pad" maxLength={5} style={[input, { fontSize: 18 }]} /></View>
    {average !== null && <Pressable accessibilityRole="button" onPress={() => setBaseline(String(average))} style={{ minHeight: 44, justifyContent: 'center' }}><Text style={{ color: colors.green, fontSize: 12 }}>Взять среднее за {known.length} записанных дн.: {number(average)} →</Text></Pressable>}
    <Ornament />
    {plan && parsed ? <View style={{ gap: 10 }}>
      <Text style={ui.label}>ОРИЕНТИР НА ДЕНЬ</Text>
      <Text style={{ fontSize: 39, fontWeight: '700', color: colors.green, letterSpacing: -1 }}>{number(plan.dailyGoal)} <Text style={{ fontSize: 14, color: colors.muted }}>шагов</Text></Text>
      <Text style={ui.body}>Твоя база {number(parsed.baselineSteps)} + около {number(plan.extraSteps)} шагов целевой ходьбы.</Text>
      <Text style={ui.small}>План: около {plan.dailyMinutes} мин целевой ходьбы в день · {plan.weeklyMinutes} мин в неделю. Это расчётный ориентир, интенсивность датчиком пока не измеряется.</Text>
      <Text style={ui.small}>Если пока ходишь мало, начни с {number(plan.gentleStart)} шагов и увеличивай нагрузку постепенно.</Text>
      <Text style={ui.small}>При весе {parsed.weightKg.toLocaleString('ru-RU')} кг: ≈{walkingEnergy(plan.extraSteps, settings.strideCm, parsed.weightKg)} активных ккал от дополнительной ходьбы. Оценка зависит от темпа и длины шага.</Text>
    </View> : <Text style={ui.small}>Добавь вес и привычную активность — увидишь ориентир по шагам и оценку энергии ходьбы.</Text>}
    <Text style={ui.small}>План для взрослых. Похудение зависит также от питания и здоровья; число шагов не гарантирует изменение веса. При ограничениях нагрузку стоит согласовать с врачом.</Text>
    <Pressable accessibilityRole="button" onPress={() => void save()} disabled={saving || loading} style={[ui.button, (saving || loading) && { opacity: 0.5 }]}><Text style={ui.buttonText}>{saving ? 'Сохраняем…' : 'Применить план'}</Text></Pressable>
    {!!result && <Text accessibilityLiveRegion="polite" style={ui.small}>{result}</Text>}
    <Pressable accessibilityRole="button" accessibilityState={{ expanded: details }} onPress={() => setDetails(!details)} style={{ minHeight: 44, justifyContent: 'center' }}><Text style={{ color: colors.green, fontSize: 12 }}>Как рассчитан план {details ? '−' : '+'}</Text></Pressable>
    {details && <View style={{ gap: 10 }}><Text style={ui.small}>В этой модели для «Поддержания» выбраны 150 минут умеренной активности в неделю, для «Похудения» — 300. Ориентир 150 минут относится к общему здоровью; для поддержания веса может требоваться больше. Это не индивидуальное медицинское назначение.</Text>
      <Text style={ui.small}>Перевод условный: 100 шагов в минуту, округление до 250, плюс указанная тобой обычная активность. Если привычные шаги уже включают целевую ходьбу, скорректируй дневную цель вручную. Интенсивность и минуты ходьбы по этим шагам пока не измеряются.</Text>
      <Text style={ui.small}>Вес не определяет норму шагов сам по себе. Он используется для примерной оценки активных ккал: ходьба по ровной поверхности, 4,8 км/ч, 3,8 MET с вычетом покоя. Это модель, а не измерение калорий или жира. Показатели прошлого веса не сохраняются.</Text>
      {[
        ['CDC: активность и вес', 'https://www.cdc.gov/healthy-weight-growth/physical-activity/index.html'],
        ['NIDDK: постепенная нагрузка', 'https://www.niddk.nih.gov/health-information/weight-management/tips-get-active/tips-starting-physical-activity'],
        ['Compendium: ходьба и MET', 'https://pacompendium.com/walking/'],
      ].map(([label, url]) => <Pressable key={url} accessibilityRole="link" onPress={() => void Linking.openURL(url).catch(() => setResult('Не удалось открыть источник.'))} style={{ minHeight: 44, justifyContent: 'center' }}><Text style={{ color: colors.green, fontSize: 12 }}>{label} ↗</Text></Pressable>)}
    </View>}
    {profile && <Pressable accessibilityRole="button" disabled={saving} onPress={() => void remove()} style={{ minHeight: 44, justifyContent: 'center' }}><Text style={{ color: colors.muted, fontSize: 12 }}>Удалить вес и профиль</Text></Pressable>}
  </View>;
}
