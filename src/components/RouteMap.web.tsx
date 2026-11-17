import { Text, View } from 'react-native';
import Ionicons from '@expo/vector-icons/Ionicons';
import { useTheme } from '../state/ThemeProvider';
import type { RoutePoint } from '../tracking/model';
import { MountainScene } from './Caucasus';

export default function RouteMap(_props: { points: RoutePoint[]; live?: boolean }) {
  const { colors, ui } = useTheme();
  return <View style={{ minHeight: 260, backgroundColor: colors.soft, borderRadius: 18, padding: 28, gap: 18, alignItems: 'center', justifyContent: 'center', overflow: 'hidden' }}>
    <View pointerEvents="none" style={{ position: 'absolute', bottom: 0, left: 0, right: 0, opacity: 0.5 }}><MountainScene height={220} /></View>
    <View style={{ width: 76, height: 76, borderRadius: 28, backgroundColor: colors.pale, alignItems: 'center', justifyContent: 'center' }}><Ionicons name="map-outline" size={34} color={colors.green} /></View>
    <Text style={[ui.sectionTitle, { textAlign: 'center' }]}>У каждой прогулки своя вершина</Text>
    <Text style={[ui.small, { textAlign: 'center' }]}>Живая карта доступна в Expo Go на iPhone и в отдельной сборке Шихши.</Text>
  </View>;
}
