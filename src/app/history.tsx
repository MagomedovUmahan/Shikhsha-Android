import { useState } from 'react';
import Ionicons from '@expo/vector-icons/Ionicons';
import { Pressable, Text, View } from 'react-native';
import { PageHeader } from '../components/Brand';
import { Screen } from '../components/Screen';
import { WeekChart } from '../components/WeekChart';
import { distanceKm, km, number } from '../lib/activity';
import { period, summary } from '../lib/insights';
import { useActivity } from '../state/ActivityProvider';
import { useTheme } from '../state/ThemeProvider';

export default function History() {
  const { colors, ui } = useTheme();
  const { snapshot, settings, refresh, today } = useActivity();
  const [range, setRange] = useState<7 | 30 | 90>(7);
  const [limit, setLimit] = useState(7);
  const [selected, setSelected] = useState<string | null>(null);
  const [refreshing, setRefreshing] = useState(false);
  const days = period(snapshot.days, range);
  const stats = summary(days, settings.goal);
  const chosen = days.find(day => day.key === selected) ?? days[days.length - 1];
  const pull = async () => { setRefreshing(true); try { await refresh(); } finally { setRefreshing(false); } };
  return <Screen refresh={() => void pull()} refreshing={refreshing}>
    <PageHeader title="Статистика" subtitle="Каждый шаг складывается в путь" />
    <View style={{ flexDirection: 'row', backgroundColor: colors.card, padding: 5, borderRadius: 17, gap: 5 }}>
      {([7, 30, 90] as const).map(value => <Pressable key={value} accessibilityRole="button"
        accessibilityState={{ selected: range === value }} onPress={() => { setRange(value); setLimit(7); }}
        style={{ flex: 1, minHeight: 43, borderRadius: 13, backgroundColor: range === value ? colors.green : 'transparent', alignItems: 'center', justifyContent: 'center' }}>
        <Text style={{ fontSize: 12, fontWeight: '600', color: range === value ? colors.onPrimary : colors.muted }}>{value} дней</Text>
      </Pressable>)}
    </View>
    <View style={ui.card}><View style={ui.row}><Text style={ui.label}>ШАГОВ ЗА {range} ДНЕЙ</Text><Ionicons name="analytics-outline" size={19} color={colors.green} /></View>
      <Text style={{ fontSize: 42, lineHeight: 49, fontWeight: '700', color: colors.ink, letterSpacing: -1.7, fontVariant: ['tabular-nums'] }}>{stats.known ? number(stats.total) : '—'}</Text>
      <View style={ui.row}><Text style={ui.small}>{stats.known ? km(distanceKm(stats.total, settings.strideCm)) + ' км · оценка' : 'Пока нет записей'}</Text>
        <Text style={[ui.small, { color: colors.green }]}>{stats.achieved} дней с целью</Text></View>
      <WeekChart days={days} selected={chosen.key} goal={settings.goal} onSelect={day => setSelected(day.key)} />
      <View style={[ui.row, { justifyContent: 'flex-start' }]}><View style={{ width: 15, height: 2, backgroundColor: colors.green }} /><Text style={ui.small}>Текущая дневная цель · {number(settings.goal)}</Text></View>
    </View>
    <View style={{ flexDirection: 'row', gap: 12 }}>
      <View style={[ui.card, { flex: 1, padding: 17 }]}><Ionicons name="trending-up-outline" size={21} color={colors.secondary} />
        <Text style={[ui.sectionTitle, { fontSize: 25 }]}>{stats.average === null ? '—' : number(stats.average)}</Text><Text style={ui.small}>В среднем за записанный день</Text></View>
      <View style={[ui.card, { flex: 1, padding: 17 }]}><Ionicons name="trophy-outline" size={21} color={colors.accent} />
        <Text style={[ui.sectionTitle, { fontSize: 25 }]}>{stats.best ? number(stats.best.steps!) : '—'}</Text><Text style={ui.small}>Лучший день периода</Text></View>
    </View>
    <View style={[ui.card, { backgroundColor: colors.pale, borderColor: colors.pale }]}>
      <Text style={ui.label}>{chosen.date.toLocaleDateString('ru-RU', { weekday: 'long', day: 'numeric', month: 'long' }).toUpperCase()}</Text>
      <Text style={ui.sectionTitle}>{chosen.steps === null ? 'За этот день нет данных' : number(chosen.steps) + ' шагов'}</Text>
      <Text style={ui.small}>{chosen.steps === null ? 'Прочерк — это отсутствие записи, а не ноль шагов.' : `Примерный путь — ${km(distanceKm(chosen.steps, settings.strideCm))} км. ${chosen.steps >= settings.goal ? 'Текущая цель достигнута.' : 'До текущей цели: ' + number(settings.goal - chosen.steps) + ' шагов.'}`}</Text>
    </View>
    <View style={ui.card}><View style={ui.row}><Text style={ui.sectionTitle}>По дням</Text><Text style={ui.small}>{stats.known}/{range} с записями</Text></View>
      {[...days].reverse().slice(0, limit).map(day => <Pressable key={day.key} accessibilityRole="button"
        accessibilityLabel={`Выбрать ${day.date.toLocaleDateString('ru-RU')}`} onPress={() => setSelected(day.key)}
        style={[ui.row, { paddingVertical: 12, borderBottomWidth: 1, borderBottomColor: colors.line }]}>
        <View style={{ flexDirection: 'row', alignItems: 'center', gap: 11, flex: 1 }}>
          <View style={{ width: 32, height: 32, borderRadius: 11, backgroundColor: day.steps !== null && day.steps >= settings.goal ? colors.pale : colors.soft, alignItems: 'center', justifyContent: 'center' }}>
            <Ionicons name={day.steps !== null && day.steps >= settings.goal ? 'checkmark' : 'footsteps-outline'} size={15} color={day.steps !== null && day.steps >= settings.goal ? colors.green : colors.muted} /></View>
          <View style={{ flex: 1 }}><Text style={ui.body}>{day.key === today ? 'Сегодня' : day.date.toLocaleDateString('ru-RU', { day: 'numeric', month: 'long' })}</Text>
            <Text style={ui.small}>{day.steps === null ? 'Нет записи' : day.steps >= settings.goal ? 'Цель достигнута' : 'Записано телефоном'}</Text></View>
        </View>
        <Text style={[ui.body, { fontWeight: '600', fontVariant: ['tabular-nums'] }]}>{day.steps === null ? '—' : number(day.steps)}</Text>
      </Pressable>)}
      {limit < range && <Pressable accessibilityRole="button" onPress={() => setLimit(Math.min(range, limit + 14))} style={{ minHeight: 44, alignItems: 'center', justifyContent: 'center' }}><Text style={{ color: colors.green, fontSize: 13 }}>Показать ещё</Text></Pressable>}
    </View>
    <Text style={ui.small}>Среднее учитывает только дни с записями, в том числе неполные. Цели и расстояние пересчитываются по текущим настройкам. Хранятся до 90 дат с записями; пропущенные дни не восстанавливаются.</Text>
  </Screen>;
}
