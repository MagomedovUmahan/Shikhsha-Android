import Ionicons from '@expo/vector-icons/Ionicons';
import { Link, router } from 'expo-router';
import { useState } from 'react';
import { Pressable, StyleSheet, Text, View } from 'react-native';
import { PageHeader } from '../components/Brand';
import { ProgressRing } from '../components/ProgressRing';
import { Screen } from '../components/Screen';
import { SensorStatus } from '../components/SensorStatus';
import { WeekChart } from '../components/WeekChart';
import { WeekStrip } from '../components/WeekStrip';
import { Eagle, MountainScene, Ornament } from '../components/Caucasus';
import { CounterControl } from '../components/CounterControl';
import { distanceKm, km, lastDays, number } from '../lib/activity';
import { currentStreak, summary } from '../lib/insights';
import { goalPlan, personalRank, walkingEnergy } from '../lib/goals';
import { useActivity } from '../state/ActivityProvider';
import { useTheme } from '../state/ThemeProvider';

export default function Today() {
  const { colors, ui } = useTheme();
  const { snapshot, settings, today, refresh, error } = useActivity();
  const [refreshing, setRefreshing] = useState(false);
  const [selected, setSelected] = useState<string | null>(null);
  const days = lastDays(snapshot.days);
  const chosen = days.find(day => day.key === selected) ?? days[days.length - 1];
  const isToday = chosen.key === today;
  const steps = chosen.steps;
  const remaining = Math.max(0, settings.goal - (steps ?? 0));
  const streak = currentStreak(snapshot.days, settings.goal);
  const week = summary(days, settings.goal);
  const active = snapshot.running && !error && snapshot.permission === 'granted';
  const paused = !snapshot.enabled && Object.keys(snapshot.days).length > 0;
  const rank = personalRank(snapshot.days, settings.goal, days.map(day => day.steps));
  const profile = settings.profile;
  const pull = async () => { setRefreshing(true); try { await refresh(); } finally { setRefreshing(false); } };
  return <Screen refresh={() => void pull()} refreshing={refreshing}>
    <PageHeader />
    <View style={[ui.card, { height: 215, padding: 18, overflow: 'hidden', borderColor: colors.green + '40', justifyContent: 'space-between' }]}>
      <View pointerEvents="none" style={{ position: 'absolute', left: 0, right: 0, bottom: 0, opacity: 0.9 }}><MountainScene height={190} /></View>
      <View style={ui.row}><Text style={[ui.label, { color: colors.green, fontSize: 8 }]}>CAUCASUS / EAGLE VISION</Text><Text style={[ui.small, { fontSize: 10 }]}>{new Date().toLocaleDateString('ru-RU', { day: 'numeric', month: 'short' })}</Text></View>
      <View style={{ gap: 8 }}><Text style={[ui.title, { fontSize: 29, lineHeight: 33, maxWidth: 245 }]}>Выше. С каждым шагом.</Text><View style={{ flexDirection: 'row', alignItems: 'center', gap: 7 }}><Eagle size={22} /><Text style={{ color: colors.green, fontSize: 11, fontWeight: '600' }}>{rank.title}</Text></View></View>
    </View>
    <WeekStrip days={days} goal={settings.goal} selected={chosen.key} onSelect={day => setSelected(day.key)} />
    <View style={[ui.card, { padding: 18, gap: 14 }]}>
      <View style={ui.row}>
        <Text style={[ui.body, { fontWeight: '600' }]}>{isToday ? 'Сегодня' : chosen.date.toLocaleDateString('ru-RU', { day: 'numeric', month: 'long' })}</Text>
        <View style={[styles.status, { backgroundColor: colors.soft }]}><View style={{ width: 5, height: 5, borderRadius: 3, backgroundColor: active ? colors.green : colors.muted }} />
          <Text style={{ color: paused ? colors.green : colors.muted, fontSize: 9 }}>{paused ? 'На паузе' : active ? 'Датчик активен' : 'Ожидаем данные'}</Text></View>
        {isToday && <CounterControl compact />}
      </View>
      <ProgressRing steps={steps} goal={settings.goal} label={isToday ? 'шагов сегодня' : 'шагов за день'} />
      <Ornament />
      <View style={[ui.row, { borderTopWidth: 1, borderTopColor: colors.line, paddingTop: 15 }]}>
        <Text style={[ui.small, { flex: 1 }]}>{steps === null ? 'Начни с первого шага' : remaining === 0 ? 'Цель достигнута!' : `Ещё ${number(remaining)} до цели`}</Text>
        <Link href="/settings" style={{ color: colors.green, fontSize: 12, paddingVertical: 8 }}>Изменить цель ↗</Link>
      </View>
      {isToday && paused && <Text style={ui.small}>История сохранена. Шаги во время паузы не добавляются. Для возобновления нажми «Продолжить».</Text>}
    </View>
    <View style={styles.stats}>
      <View style={[styles.stat, { backgroundColor: colors.card, borderColor: colors.line }]}>
        <Ionicons name="navigate-outline" size={20} color={colors.secondary} />
        <Text style={[styles.statValue, { color: colors.ink }]}>{steps === null ? '—' : km(distanceKm(steps, settings.strideCm))}<Text style={[styles.unit, { color: colors.muted }]}> км</Text></Text>
        <Text style={ui.small}>Расстояние · оценка</Text></View>
      <View style={[styles.stat, { backgroundColor: colors.card, borderColor: colors.line }]}>
        <Ionicons name={profile && isToday ? 'flash-outline' : 'flag-outline'} size={20} color={colors.green} />
        <Text style={[styles.statValue, { color: colors.ink }]}>{steps === null ? '—' : profile && isToday ? `≈${walkingEnergy(steps, settings.strideCm, profile.weightKg)}` : Math.floor(steps / settings.goal * 100)}<Text style={[styles.unit, { color: colors.muted }]}>{profile && isToday ? ' ккал' : ' %'}</Text></Text>
        <Text style={ui.small}>{profile && isToday ? 'Активная энергия · оценка' : 'От дневной цели'}</Text></View>
    </View>
    <Pressable accessibilityRole="button" onPress={() => router.navigate('/settings')} style={[ui.card, { borderLeftWidth: 3, borderLeftColor: colors.green }]}>
      <View style={ui.row}><Text style={ui.label}>ТВОЙ ПЛАН</Text><Ionicons name="arrow-forward" color={colors.green} size={18} /></View>
      <Text style={ui.sectionTitle}>{profile ? profile.purpose === 'loss' ? 'Похудение' : 'Поддержание' : 'Выбери личную цель'}</Text>
      <Text style={ui.small}>{profile ? `${profile.weightKg.toLocaleString('ru-RU')} кг · цель ${number(settings.goal)} шагов в день · ориентир плана ${number(goalPlan(profile).dailyGoal)}` : 'Добавь вес и привычную активность: получи понятный ориентир по шагам.'}</Text>
      {profile && <Text style={ui.small}>Ккал рассчитаны для условной ровной ходьбы. Достижение шагов само по себе не гарантирует изменение веса.</Text>}
    </Pressable>
    <Pressable accessibilityRole="button" onPress={() => router.navigate('/achievements')} style={[ui.row, { backgroundColor: colors.pale, borderRadius: 21, padding: 18 }]}>
      <View style={[ui.row, { flex: 1, justifyContent: 'flex-start' }]}><Ionicons name="flame" size={26} color={colors.accent} />
        <View style={{ flex: 1, gap: 3 }}><Text style={[ui.body, { fontWeight: '600' }]}>{streak ? `Серия: ${streak} дн.` : 'Маленькие шаги. Большая привычка.'}</Text>
          <Text style={ui.small}>{rank.score === null ? 'Личный индекс появится после первых данных' : `Личный индекс: ${rank.score}/1 000 · по ${rank.observed} записанным дн.`}</Text></View></View>
      <Ionicons name="chevron-forward" size={18} color={colors.green} />
    </Pressable>
    <View style={ui.card}><View style={ui.row}><Text style={ui.sectionTitle}>Твоя неделя</Text><Link href="/history" style={{ color: colors.green, fontSize: 12, paddingVertical: 8 }}>Подробнее ↗</Link></View>
      <View style={ui.row}><Text style={[ui.body, { fontWeight: '700', fontSize: 24 }]}>{week.known ? number(week.total) : '—'} <Text style={ui.small}>шагов</Text></Text>
        <Text style={ui.small}>{week.achieved}/7 дней с целью</Text></View>
      <WeekChart days={days} selected={chosen.key} goal={settings.goal} onSelect={day => setSelected(day.key)} />
      {!week.known && <Text style={ui.small}>График заполнится после подключения шагомера.</Text>}
    </View>
    <SensorStatus />
    {!isToday && <Pressable accessibilityRole="button" onPress={() => setSelected(null)} style={[ui.button, { backgroundColor: colors.soft }]}><Text style={{ color: colors.ink, fontWeight: '600' }}>Вернуться к сегодняшнему дню</Text></Pressable>}
  </Screen>;
}
const styles = StyleSheet.create({
  status: { flexDirection: 'row', alignItems: 'center', gap: 6, borderRadius: 20, paddingHorizontal: 9, paddingVertical: 6 },
  stats: { flexDirection: 'row', gap: 12 },
  stat: { flex: 1, borderRadius: 16, borderWidth: 1, padding: 17, gap: 10 },
  statValue: { fontSize: 29, fontWeight: '600', fontVariant: ['tabular-nums'], letterSpacing: -1 },
  unit: { fontSize: 12, fontWeight: '400' },
});
