import { useState } from 'react';
import { Alert, Linking, Platform, Pressable, Switch, Text, View } from 'react-native';
import Ionicons from '@expo/vector-icons/Ionicons';
import { router } from 'expo-router';
import { Brand } from '../components/Brand';
import { Screen } from '../components/Screen';
import RouteMap from '../components/RouteMap';
import { WalkStats } from '../components/WalkStats';
import { useTheme } from '../state/ThemeProvider';
import { useWalk } from '../state/WalkProvider';
import { duration, activeMilliseconds } from '../tracking/model';
import { expoGo, iphoneExpoGo } from '../lib/runtime';

export default function WalkScreen() {
  const { colors, ui } = useTheme();
  const { active, history, loading, busy, error, supported, now, start, pause, resume, finish, loadMore } = useWalk();
  const [background, setBackground] = useState(!expoGo);
  const walk = active?.walk;
  const finishWalk = () => Alert.alert('Завершить прогулку?', 'Маршрут и показатели останутся в истории.', [
    { text: 'Продолжать прогулку', style: 'cancel' },
    { text: 'Завершить', onPress: () => { void finish().then(view => { if (view) router.push({ pathname: '/walk-detail', params: { id: view.walk.id } }); }); } },
  ]);
  return <Screen>
    <Brand compact />
    <View style={{ gap: 5 }}><Text style={ui.title}>На прогулку</Text><Text style={ui.subtitle}>Оставь свой след. Найди свой ритм.</Text></View>
    {!!error && <View style={[ui.card, { backgroundColor: colors.warningBg }]}><Text style={[ui.body, { color: colors.warning }]} accessibilityLiveRegion="polite">{error}</Text>
      {Platform.OS !== 'web' && <Pressable accessibilityRole="button" onPress={() => void Linking.openSettings()}><Text style={{ color: colors.warning, fontWeight: '700' }}>Открыть настройки</Text></Pressable>}</View>}
    <RouteMap key={walk?.id ?? 'empty'} points={active?.points ?? []} live={walk?.status === 'recording'} />
    {walk ? <>
      <View style={ui.card}>
        <View style={ui.row}><View style={{ flexDirection: 'row', gap: 8, alignItems: 'center' }}><View style={{ width: 7, height: 7, borderRadius: 4, backgroundColor: walk.status === 'recording' ? colors.green : colors.accent }} />
          <Text style={{ color: colors.ink, fontWeight: '600' }}>{walk.status === 'paused' ? 'На паузе' : 'Маршрут записывается'}</Text></View>
          <Ionicons name={walk.mode === 'background' ? 'moon-outline' : 'sunny-outline'} size={18} color={colors.muted} /></View>
        <WalkStats walk={walk} now={now} />
        <Text style={ui.small}>{walk.status === 'paused' ? 'На паузе GPS, время и шаги прогулки не добавляются.' : !walk.lastSample ? 'Ожидаем точный GPS-сигнал. Выйди на открытое место.' : now - walk.lastSample > 45000 ? 'GPS-сигнал давно не обновлялся. Выйди на открытое место; пропуск маршрута не будет дорисован.' : `GPS: ${new Date(walk.lastSample).toLocaleTimeString('ru-RU')} · точность ±${Math.round(walk.lastAccuracy ?? 0)} м`}</Text>
        {!!walk.warning && <Text style={[ui.small, { color: colors.warning }]}>{walk.warning}</Text>}
        <View style={{ flexDirection: 'row', gap: 10 }}>
          <Pressable accessibilityRole="button" disabled={busy} onPress={() => void (walk.status === 'paused' ? resume() : pause())} style={[ui.button, { flex: 1, opacity: busy ? 0.5 : 1 }]}>
            <Text style={ui.buttonText}>{busy ? 'Подожди…' : walk.status === 'paused' ? 'Продолжить' : 'Пауза'}</Text></Pressable>
          <Pressable accessibilityRole="button" disabled={busy} onPress={finishWalk} style={[ui.button, { flex: 1, backgroundColor: colors.soft, opacity: busy ? 0.5 : 1 }]}><Text style={[ui.buttonText, { color: colors.ink }]}>Завершить</Text></Pressable>
        </View>
      </View>
    </> : <View style={ui.card}>
      <Text style={ui.sectionTitle}>Готов к новому маршруту?</Text>
      <Text style={ui.body}>Шихша запишет путь, расстояние по GPS, время, темп и доступные шаги. Геопозиция запрашивается после нажатия «Начать прогулку».</Text>
      <View style={ui.row}><View style={{ flex: 1, gap: 4 }}><Text style={ui.body}>Запись с выключенным экраном</Text><Text style={ui.small}>Требуется фоновый доступ к геопозиции</Text></View>
        <Switch accessibilityLabel="Запись с выключенным экраном" disabled={expoGo} value={background} onValueChange={setBackground} trackColor={{ false: colors.line, true: colors.green }} thumbColor={background ? colors.onPrimary : colors.muted} /></View>
      {expoGo && <Text style={ui.small}>Expo Go: маршрут записывается с открытым приложением. Для фонового GPS нужна отдельная сборка.</Text>}
      <Text style={ui.small}>{background ? 'Разреши точную геопозицию и доступ «Всегда». Android может открыть системные настройки: выбери доступ в любом режиме. Запись продолжается при блокировке; её статус виден в системном уведомлении.' : 'Приложение должно оставаться открытым. При сворачивании или блокировке прогулка автоматически встанет на паузу.'}</Text>
      <Pressable accessibilityRole="button" disabled={!supported || loading || busy} onPress={() => void start(background)} style={[ui.button, (!supported || loading || busy) && { opacity: 0.5 }]}>
        <Text style={ui.buttonText}>{loading ? 'Загрузка…' : busy ? 'Подключаем GPS…' : 'Начать прогулку'}</Text></Pressable>
      {!supported && <Text style={ui.small}>Нужна сборка для телефона или Expo Go на iPhone. В браузере запись недоступна.</Text>}
    </View>}
    <View style={ui.row}><Text style={ui.sectionTitle}>Твои маршруты</Text><Text style={ui.small}>{history.length ? `${history.length} сохранено` : 'Пока чистый лист'}</Text></View>
    {history.length ? history.map(item => <Pressable key={item.id} accessibilityRole="button" onPress={() => router.push({ pathname: '/walk-detail', params: { id: item.id } })} style={[ui.card, { flexDirection: 'row', alignItems: 'center', gap: 14 }]}>
      <View style={{ width: 46, height: 46, backgroundColor: colors.pale, borderRadius: 16, alignItems: 'center', justifyContent: 'center' }}><Ionicons name="trail-sign-outline" size={24} color={colors.green} /></View>
      <View style={{ flex: 1, gap: 5 }}><Text style={{ color: colors.ink, fontSize: 15, fontWeight: '600' }}>{new Date(item.startedAt).toLocaleDateString('ru-RU', { day: 'numeric', month: 'long' })} · {new Date(item.startedAt).toLocaleTimeString('ru-RU', { hour: '2-digit', minute: '2-digit' })}</Text>
        <Text style={ui.small}>{(item.distance / 1000).toFixed(2).replace('.', ',')} км · {duration(activeMilliseconds(item))}</Text></View><Ionicons name="chevron-forward" size={19} color={colors.muted} />
    </Pressable>) : <View style={ui.card}><Text style={ui.body}>Здесь появится первая прогулка</Text><Text style={ui.small}>После завершения можно снова открыть карту и поделиться маршрутом в GPX.</Text></View>}
    {history.length >= 20 && history.length % 20 === 0 && <Pressable accessibilityRole="button" onPress={() => void loadMore()} style={[ui.button, { backgroundColor: colors.soft }]}><Text style={[ui.buttonText, { color: colors.ink }]}>Ещё маршруты</Text></Pressable>}
    <Text style={ui.small}>Маршруты хранятся на телефоне. {iphoneExpoGo ? 'Карту предоставляет Apple Maps; сервис получает запросы отображаемой области.' : 'Для подложки карты нужен интернет: OpenFreeMap получает запросы тайлов.'} Запись GPS работает независимо от загрузки карты.</Text>
  </Screen>;
}
