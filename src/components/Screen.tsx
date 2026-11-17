import React from 'react';
import { RefreshControl, ScrollView } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { useTheme } from '../state/ThemeProvider';

export function Screen({ children, refresh, refreshing = false }: React.PropsWithChildren<{
  refresh?: () => void; refreshing?: boolean;
}>) {
  const { colors, ui } = useTheme();
  return <SafeAreaView edges={['top', 'left', 'right']} style={ui.page}>
    <ScrollView keyboardShouldPersistTaps="handled" showsVerticalScrollIndicator={false}
      contentContainerStyle={ui.content}
      refreshControl={refresh ? <RefreshControl refreshing={refreshing} onRefresh={refresh} tintColor={colors.green} /> : undefined}>
      {children}
    </ScrollView>
  </SafeAreaView>;
}
