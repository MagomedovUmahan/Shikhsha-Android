import Ionicons from '@expo/vector-icons/Ionicons';
import { Tabs } from 'expo-router';
import { StatusBar } from 'expo-status-bar';
import { SafeAreaProvider, useSafeAreaInsets } from 'react-native-safe-area-context';
import { ActivityProvider } from '../state/ActivityProvider';
import { ThemeProvider, useTheme } from '../state/ThemeProvider';
import { WalkProvider } from '../state/WalkProvider';

function Navigation() {
  const { colors, isDark } = useTheme();
  const insets = useSafeAreaInsets();
  return <><StatusBar style={isDark ? 'light' : 'dark'} />
    <Tabs screenOptions={{
      headerShown: false,
      tabBarActiveTintColor: colors.green, tabBarInactiveTintColor: colors.muted,
      tabBarActiveBackgroundColor: colors.pale,
      tabBarStyle: {
        position: 'absolute', marginHorizontal: 16, bottom: Math.max(12, insets.bottom),
        height: 72, borderRadius: 18, backgroundColor: colors.card, borderColor: colors.line,
        borderTopWidth: 1, borderWidth: 1, paddingTop: 8, paddingBottom: 8, elevation: 0,
      },
      tabBarItemStyle: { borderRadius: 12, marginHorizontal: 2, paddingVertical: 0 },
      tabBarLabelStyle: { fontSize: 9, fontWeight: '600', marginTop: 1 },
      sceneStyle: { backgroundColor: colors.background },
    }}>
      <Tabs.Screen name="index" options={{ title: 'Сегодня', tabBarIcon: ({ color, size }) => <Ionicons name="footsteps-outline" color={typeof color === 'string' ? color : colors.green} size={size - 2} /> }} />
      <Tabs.Screen name="history" options={{ title: 'Итоги', tabBarIcon: ({ color, size }) => <Ionicons name="stats-chart-outline" color={typeof color === 'string' ? color : colors.green} size={size - 2} /> }} />
      <Tabs.Screen name="walk" options={{ title: 'Прогулка', tabBarIcon: ({ color, size }) => <Ionicons name="map-outline" color={typeof color === 'string' ? color : colors.green} size={size - 2} /> }} />
      <Tabs.Screen name="achievements" options={{ title: 'Вершины', tabBarIcon: ({ color, size }) => <Ionicons name="triangle-outline" color={typeof color === 'string' ? color : colors.green} size={size - 2} /> }} />
      <Tabs.Screen name="settings" options={{ title: 'Опции', tabBarIcon: ({ color, size }) => <Ionicons name="options-outline" color={typeof color === 'string' ? color : colors.green} size={size - 2} /> }} />
      <Tabs.Screen name="walk-detail" options={{ href: null }} />
    </Tabs>
  </>;
}
export default function Layout() {
  return <SafeAreaProvider><ThemeProvider><ActivityProvider><WalkProvider><Navigation /></WalkProvider></ActivityProvider></ThemeProvider></SafeAreaProvider>;
}
