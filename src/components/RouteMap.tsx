import { useEffect, useMemo, useRef, useState } from 'react';
import { Pressable, Text, View } from 'react-native';
import Ionicons from '@expo/vector-icons/Ionicons';
import type { CameraRef } from '@maplibre/maplibre-react-native';
import Pedometer from '../../modules/shikhsha-pedometer/src/ShikhshaPedometerModule';
import { type RoutePoint, routeLines } from '../tracking/model';
import { useTheme } from '../state/ThemeProvider';
import { iphoneExpoGo } from '../lib/runtime';

let MapLibre: typeof import('@maplibre/maplibre-react-native') | null = null;
try {
  // Expo Go/older installed builds must be able to show a rebuild notice.
  // eslint-disable-next-line @typescript-eslint/no-require-imports
  if (Pedometer && !iphoneExpoGo) MapLibre = require('@maplibre/maplibre-react-native');
} catch { /* The native map is available after rebuilding the application. */ }

export type RouteMapProps = { points: RoutePoint[]; live?: boolean };
export default function RouteMap(props: RouteMapProps) {
  if (iphoneExpoGo) {
    // eslint-disable-next-line @typescript-eslint/no-require-imports
    const AppleRouteMap = require('./AppleRouteMap').default;
    return <AppleRouteMap {...props} />;
  }
  return <NativeRouteMap {...props} />;
}
function NativeRouteMap({ points, live = false }: RouteMapProps) {
  const { colors, ui } = useTheme();
  const camera = useRef<CameraRef>(null);
  const [failed, setFailed] = useState(false), [ready, setReady] = useState(false);
  const [following, setFollowing] = useState(live);
  const first = points[0], last = points.at(-1);
  const data = useMemo<GeoJSON.Feature<GeoJSON.MultiLineString>>(() => ({ type: 'Feature', properties: {}, geometry: { type: 'MultiLineString', coordinates: routeLines(points) } }), [points]);
  const markers = useMemo<GeoJSON.FeatureCollection<GeoJSON.Point>>(() => ({ type: 'FeatureCollection', features: first && last ? [
    { type: 'Feature', properties: { kind: 'start' }, geometry: { type: 'Point', coordinates: [first.longitude, first.latitude] } },
    { type: 'Feature', properties: { kind: 'end' }, geometry: { type: 'Point', coordinates: [last.longitude, last.latitude] } },
  ] : [] }), [first, last]);
  useEffect(() => {
    if (ready && following && last) camera.current?.easeTo({ center: [last.longitude, last.latitude], zoom: 16, duration: 600 });
  }, [following, last, ready]);
  const fit = () => {
    if (!points.length) return;
    const lng = points.map(p => p.longitude), lat = points.map(p => p.latitude);
    setFollowing(false);
    if (points.length === 1) camera.current?.easeTo({ center: [points[0].longitude, points[0].latitude], zoom: 16 });
    else camera.current?.fitBounds([Math.min(...lng), Math.min(...lat), Math.max(...lng), Math.max(...lat)], { padding: { top: 45, bottom: 45, left: 45, right: 45 }, duration: 500 });
  };
  if (!MapLibre || !first) return <View style={{ height: 290, backgroundColor: colors.soft, borderRadius: 24, alignItems: 'center', justifyContent: 'center', padding: 28, gap: 14 }}>
    <View style={{ width: 76, height: 76, backgroundColor: colors.pale, borderRadius: 28, alignItems: 'center', justifyContent: 'center' }}><Ionicons name="map-outline" size={34} color={colors.green} /></View>
    <Text style={ui.sectionTitle}>{first ? 'Нужна новая сборка' : 'Твой маршрут начинается здесь'}</Text>
    <Text style={[ui.small, { textAlign: 'center' }]}>{first ? 'Пересобери приложение с модулем карты.' : 'После запуска прогулки выйди на открытое место. Карта появится, когда GPS определит точную позицию.'}</Text>
  </View>;
  const { Map, Camera, GeoJSONSource, Layer } = MapLibre;
  return <View style={{ height: 350, borderRadius: 24, overflow: 'hidden', backgroundColor: colors.soft }}>
    <Map style={{ flex: 1 }} mapStyle="https://tiles.openfreemap.org/styles/liberty" attribution
      onDidFailLoadingMap={() => setFailed(true)} onDidFinishLoadingMap={() => { setFailed(false); setReady(true); if (!live) fit(); }}
      onRegionDidChange={event => { if (event.nativeEvent.userInteraction) setFollowing(false); }}>
      <Camera ref={camera} initialViewState={{ center: [first.longitude, first.latitude], zoom: 16 }} maxZoom={18} />
      <GeoJSONSource id="walk-route" data={data}>
        <Layer id="walk-outline" type="line" paint={{ 'line-color': '#243621', 'line-width': 8 }} layout={{ 'line-cap': 'round', 'line-join': 'round' }} />
        <Layer id="walk-line" type="line" paint={{ 'line-color': '#90C94C', 'line-width': 4 }} layout={{ 'line-cap': 'round', 'line-join': 'round' }} />
      </GeoJSONSource>
      <GeoJSONSource id="walk-markers" data={markers}>
        <Layer id="walk-endpoints" type="circle" paint={{ 'circle-radius': 6, 'circle-color': ['case', ['==', ['get', 'kind'], 'start'], '#FFFFFF', '#293C27'], 'circle-stroke-color': '#90C94C', 'circle-stroke-width': 3 }} />
      </GeoJSONSource>
    </Map>
    {failed && <Text style={[ui.small, { position: 'absolute', top: 12, left: 12, right: 12, padding: 12, borderRadius: 12, backgroundColor: colors.card }]}>Карта не загрузилась. Проверь интернет. Запись GPS продолжается.</Text>}
    <View style={{ position: 'absolute', right: 12, bottom: 48, gap: 8 }}>
      {live && <Pressable accessibilityRole="button" accessibilityLabel="Следовать за мной" onPress={() => setFollowing(true)} style={{ backgroundColor: following ? colors.green : colors.card, padding: 12, borderRadius: 16 }}><Ionicons name="locate" size={22} color={following ? colors.onPrimary : colors.ink} /></Pressable>}
      <Pressable accessibilityRole="button" accessibilityLabel="Показать весь маршрут" onPress={fit} style={{ backgroundColor: colors.card, padding: 12, borderRadius: 16 }}><Ionicons name="scan-outline" size={22} color={colors.ink} /></Pressable>
    </View>
  </View>;
}
