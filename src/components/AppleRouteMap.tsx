import { useEffect, useMemo, useRef, useState } from 'react';
import { Pressable, Text, View } from 'react-native';
import MapView, { Marker, Polyline } from 'react-native-maps';
import Ionicons from '@expo/vector-icons/Ionicons';
import { type RoutePoint, routeLines } from '../tracking/model';
import { useTheme } from '../state/ThemeProvider';
import { MountainScene } from './Caucasus';

export default function AppleRouteMap({ points, live = false }: { points: RoutePoint[]; live?: boolean }) {
  const { colors, ui, isDark } = useTheme();
  const map = useRef<MapView>(null);
  const [ready, setReady] = useState(false), [follow, setFollow] = useState(live);
  const first = points[0], last = points.at(-1);
  const lines = useMemo(() => routeLines(points).map(line => line.map(p => ({ latitude: p[1], longitude: p[0] }))), [points]);
  useEffect(() => {
    if (ready && follow && last) map.current?.animateToRegion({ latitude: last.latitude, longitude: last.longitude, latitudeDelta: 0.006, longitudeDelta: 0.006 }, 500);
  }, [ready, follow, last]);
  const fit = () => {
    setFollow(false);
    map.current?.fitToCoordinates(points.map(p => ({ latitude: p.latitude, longitude: p.longitude })), { edgePadding: { top: 45, bottom: 45, left: 45, right: 45 }, animated: true });
  };
  if (!first || !last) return <View style={{ height: 260, borderRadius: 18, backgroundColor: colors.soft, overflow: 'hidden', padding: 22, justifyContent: 'center', gap: 12 }}>
    <View pointerEvents="none" style={{ position: 'absolute', bottom: 0, left: 0, right: 0, opacity: 0.4 }}><MountainScene /></View>
    <Ionicons name="map-outline" color={colors.green} size={32} /><Text style={ui.sectionTitle}>Твой маршрут начинается здесь</Text><Text style={ui.small}>Начни прогулку и дождись точной геопозиции. Путь появится на карте Apple Maps.</Text>
  </View>;
  return <View style={{ height: 350, borderRadius: 18, overflow: 'hidden', backgroundColor: colors.soft }}>
    <MapView ref={map} style={{ flex: 1 }} userInterfaceStyle={isDark ? 'dark' : 'light'}
      initialRegion={{ latitude: first.latitude, longitude: first.longitude, latitudeDelta: 0.006, longitudeDelta: 0.006 }}
      onMapReady={() => { setReady(true); if (!live) fit(); }} onPanDrag={() => setFollow(false)}>
      {lines.map((line, index) => <Polyline key={index} coordinates={line} strokeColor="#338E68" strokeWidth={5} />)}
      <Marker coordinate={{ latitude: first.latitude, longitude: first.longitude }} title="Начало" pinColor="#7CB09A" />
      <Marker coordinate={{ latitude: last.latitude, longitude: last.longitude }} title={live ? 'Последняя точка' : 'Конец'} pinColor="#246C50" />
    </MapView>
    <View style={{ position: 'absolute', right: 12, bottom: 48, gap: 8 }}>
      {live && <Pressable accessibilityRole="button" accessibilityLabel="Следовать за мной" onPress={() => setFollow(true)} style={{ backgroundColor: follow ? colors.green : colors.card, padding: 12, borderRadius: 12 }}><Ionicons name="locate" size={22} color={follow ? colors.onPrimary : colors.ink} /></Pressable>}
      <Pressable accessibilityRole="button" accessibilityLabel="Показать весь маршрут" onPress={fit} style={{ backgroundColor: colors.card, padding: 12, borderRadius: 12 }}><Ionicons name="scan-outline" size={22} color={colors.ink} /></Pressable>
    </View>
  </View>;
}
