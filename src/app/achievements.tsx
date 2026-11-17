import Ionicons from '@expo/vector-icons/Ionicons';
import { Text, View } from 'react-native';
import { PageHeader } from '../components/Brand';
import { Screen } from '../components/Screen';
import { number, lastDays } from '../lib/activity';
import { personalRank } from '../lib/goals';
import { Eagle, MountainScene, Ornament } from '../components/Caucasus';
import { achievements, currentStreak } from '../lib/insights';
import { useActivity } from '../state/ActivityProvider';
import { useTheme } from '../state/ThemeProvider';

export default function Achievements() {
  const { colors, ui } = useTheme();
  const { snapshot, settings } = useActivity();
  const badges = achievements(snapshot.days, settings.goal);
  const unlocked = badges.filter(badge => badge.unlocked).length;
  const streak = currentStreak(snapshot.days, settings.goal);
  const rank = personalRank(snapshot.days, settings.goal, lastDays(snapshot.days).map(d => d.steps));
  return <Screen><PageHeader title="Твои вершины" subtitle="EAGLE VISION / личный прогресс" />
    <View style={[ui.card, { overflow: 'hidden', minHeight: 170 }]}>
      <View pointerEvents="none" style={{ position: 'absolute', bottom: 0, right: 0, left: 0, opacity: 0.5 }}><MountainScene height={170} /></View>
      <Text style={ui.label}>ТВОЁ ЗВАНИЕ</Text><View style={[ui.row, { justifyContent: 'flex-start' }]}><Eagle size={44} /><Text style={[ui.sectionTitle, { flex: 1 }]}>{rank.title}</Text></View>
      <Text style={ui.small}>{rank.score === null ? 'Индекс активности появится после первых данных' : `Личный индекс: ${rank.score}/1 000 · ${rank.observed} записанных дн.`}</Text>
      <Text style={[ui.small, { fontSize: 10 }]}>Исследователь → 50 000: Горец → 200 000: Покоритель вершин</Text>
    </View>
    <Ornament />
    <View style={[ui.card, { backgroundColor: colors.pale, borderColor: colors.pale, gap: 18 }]}>
      <View style={ui.row}><View style={{ flex: 1, gap: 7 }}><Text style={ui.label}>ТВОЯ КОЛЛЕКЦИЯ</Text>
        <Text style={[ui.title, { fontSize: 37, lineHeight: 45 }]}>{unlocked}<Text style={{ fontSize: 19, color: colors.muted }}> / {badges.length}</Text></Text>
        <Text style={ui.small}>{unlocked ? 'Каждый результат — часть пути' : 'Первая награда начинается с прогулки'}</Text></View>
        <View style={{ width: 76, height: 76, borderRadius: 26, borderWidth: 1, borderColor: colors.green + '40', backgroundColor: colors.green + '16', alignItems: 'center', justifyContent: 'center' }}>
          <Ionicons name="ribbon-outline" size={41} color={colors.green} /></View>
      </View>
      <View style={{ height: 5, backgroundColor: colors.green + '20', borderRadius: 10, overflow: 'hidden' }}><View style={{ width: `${unlocked / badges.length * 100}%`, height: 5, backgroundColor: colors.green }} /></View>
    </View>
    <View style={[ui.card, ui.row, { padding: 18 }]}>
      <View style={[ui.row, { justifyContent: 'flex-start', flex: 1 }]}><Ionicons name="flame" size={29} color={colors.accent} />
        <View style={{ flex: 1 }}><Text style={ui.sectionTitle}>Серия: {streak} дн.</Text><Text style={ui.small}>Дни подряд с текущей целью</Text></View></View>
      <Text style={{ color: colors.green, fontSize: 24, fontWeight: '600' }}>{streak}</Text>
    </View>
    <Text style={ui.sectionTitle}>Твои рубежи</Text>
    <View style={{ flexDirection: 'row', flexWrap: 'wrap', gap: 12 }}>
      {badges.map(badge => <View key={badge.id} accessible
        accessibilityLabel={`${badge.title}. ${badge.subtitle}. ${badge.unlocked ? 'Порог достигнут' : 'Пока закрыто'}. ${number(Math.min(badge.value, badge.target))} из ${number(badge.target)}`}
        style={[ui.card, { width: '48%', flexGrow: 1, flexBasis: '45%', padding: 16, gap: 12, borderColor: badge.unlocked ? colors.green + '60' : colors.line }]}>
        <View style={{ width: 53, height: 53, borderRadius: 18, backgroundColor: badge.unlocked ? colors.pale : colors.soft, justifyContent: 'center', alignItems: 'center' }}>
          <Ionicons name="triangle-outline" size={26} color={badge.unlocked ? colors.green : colors.muted} />
        </View>
        <View style={{ gap: 5 }}><Text style={[ui.body, { fontWeight: '600' }]}>{badge.title}</Text><Text style={[ui.small, { minHeight: 38 }]}>{badge.subtitle}</Text></View>
        <View style={{ height: 4, backgroundColor: colors.soft, borderRadius: 5, overflow: 'hidden' }}><View style={{ width: `${badge.progress * 100}%`, height: 4, backgroundColor: colors.green }} /></View>
        <Text style={{ fontSize: 10, color: badge.unlocked ? colors.green : colors.muted, lineHeight: 16 }}>{badge.unlocked ? 'Порог достигнут ✓' : `${number(Math.min(badge.value, badge.target))} / ${number(badge.target)} ${badge.unit}`}</Text>
      </View>)}
    </View>
    <Text style={ui.small}>Вершины — виртуальные рубежи активности, не реальные восхождения. Индекс: среднее выполнение дневной цели по записанным дням последних 7 дней, до 1 000 баллов; это личный показатель. Звания и награды используют сохранённые данные максимум за 90 дат. Изменение цели влияет на серии и индекс.</Text>
  </Screen>;
}
