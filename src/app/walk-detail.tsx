import { useEffect, useState } from 'react';
import { Alert, Pressable, Text, View } from 'react-native';
import { router, useLocalSearchParams } from 'expo-router';
import { Screen } from '../components/Screen';
import RouteMap from '../components/RouteMap';
import { WalkStats } from '../components/WalkStats';
import { useTheme } from '../state/ThemeProvider';
import { useWalk } from '../state/WalkProvider';
import { deleteWalk, getWalk } from '../tracking/store';
import { toGPX, type WalkView } from '../tracking/model';

export default function WalkDetail() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const { colors, ui } = useTheme();
  const { refresh, now } = useWalk();
  const [view, setView] = useState<WalkView | null>(null), [loading, setLoading] = useState(true), [busy, setBusy] = useState(false), [error, setError] = useState('');
  useEffect(() => {
    let mounted = true;
    void getWalk(id).then(value => { if (mounted) { setView(value); if (!value) setError('Прогулка не найдена.'); } })
      .catch(() => { if (mounted) setError('Не удалось открыть маршрут.'); }).finally(() => { if (mounted) setLoading(false); });
    return () => { mounted = false; };
  }, [id]);
  const share = async () => {
    setBusy(true); setError('');
    try {
      const full = await getWalk(id, false);
      if (!full?.points.length) throw new Error('В этой прогулке нет GPS-точек для экспорта.');
      const Sharing = await import('expo-sharing');
      if (!await Sharing.isAvailableAsync()) throw new Error('На этом устройстве недоступна отправка файлов.');
      const { File, Paths } = await import('expo-file-system');
      const file = new File(Paths.cache, `Shikhsha-${full.walk.startedAt}.gpx`);
      file.create({ overwrite: true }); file.write(toGPX(full));
      // Android's chooser can resolve before the receiving app reads the URI.
      // Keep the cache file available; delete it with this walk or OS cache cleanup.
      await Sharing.shareAsync(file.uri, { mimeType: 'application/gpx+xml', UTI: 'com.topografix.gpx', dialogTitle: 'Поделиться маршрутом' });
    } catch (cause) { setError(cause instanceof Error ? cause.message : 'Не удалось отправить маршрут.'); }
    finally { setBusy(false); }
  };
  const remove = () => Alert.alert('Удалить прогулку?', 'Маршрут и её показатели будут удалены с телефона без восстановления.', [
    { text: 'Отмена', style: 'cancel' },
    { text: 'Удалить', style: 'destructive', onPress: () => {
      setBusy(true);
      void (async () => {
        if (view) {
          const { File, Paths } = await import('expo-file-system');
          const exported = new File(Paths.cache, `Shikhsha-${view.walk.startedAt}.gpx`);
          if (exported.exists) exported.delete();
        }
        await deleteWalk(id); await refresh(); router.replace('/walk');
      })().catch(() => { setError('Не удалось удалить прогулку.'); setBusy(false); });
    } },
  ]);
  return <Screen>
    <Pressable accessibilityRole="button" onPress={() => router.replace('/walk')} style={{ minHeight: 44, justifyContent: 'center' }}><Text style={{ color: colors.green, fontWeight: '600' }}>‹ Все маршруты</Text></Pressable>
    <View style={{ gap: 5 }}><Text style={ui.title}>Твой маршрут</Text><Text style={ui.subtitle}>{view ? new Date(view.walk.startedAt).toLocaleString('ru-RU', { day: 'numeric', month: 'long', hour: '2-digit', minute: '2-digit' }) : loading ? 'Загрузка…' : ''}</Text></View>
    {!!error && <Text style={[ui.body, { color: colors.warning }]} accessibilityLiveRegion="polite">{error}</Text>}
    {view && <><RouteMap points={view.points} /><View style={ui.card}><Text style={ui.sectionTitle}>Прогулка сохранена</Text><WalkStats walk={view.walk} now={view.walk.endedAt ?? now} />
      <Text style={ui.small}>Максимальная скорость: {view.walk.maxSpeed > 0 ? (view.walk.maxSpeed * 3.6).toFixed(1) : '—'} км/ч · GPS-точек: {view.walk.pointCount}</Text>
      <Text style={ui.small}>{view.walk.gaps ? `Пропуски GPS: ${view.walk.gaps}. Участки через пропуски не соединяются. ` : ''}Расстояние и темп приблизительные: точность зависит от сигнала GPS. Время включает остановки без ручной паузы.</Text>
      {!!view.walk.warning && <Text style={[ui.small, { color: colors.warning }]}>{view.walk.warning}</Text>}
      <Text style={ui.small}>Android может передавать шаги с задержкой около начала и конца записи. На iPhone используются запросы Core Motion за интервалы прогулки. Прочерк означает отсутствие данных.</Text>
    </View>
      <Pressable accessibilityRole="button" disabled={busy || !view.points.length} onPress={() => void share()} style={[ui.button, (busy || !view.points.length) && { opacity: 0.5 }]}><Text style={ui.buttonText}>{busy ? 'Подожди…' : 'Поделиться маршрутом · GPX'}</Text></Pressable>
      <Text style={ui.small}>GPX содержит точные координаты и время. Отправляй файл только тем, кому хочешь показать свой маршрут.</Text>
      {view.walk.status === 'completed' && <Pressable accessibilityRole="button" disabled={busy} onPress={remove} style={[ui.button, { backgroundColor: colors.soft }]}><Text style={[ui.buttonText, { color: colors.warning }]}>Удалить прогулку</Text></Pressable>}
    </>}
  </Screen>;
}
