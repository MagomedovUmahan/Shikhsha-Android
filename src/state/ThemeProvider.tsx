import AsyncStorage from '@react-native-async-storage/async-storage';
import React, { createContext, useContext, useEffect, useMemo, useState } from 'react';
import { useColorScheme } from 'react-native';
import { darkColors, lightColors, makeUI, type ThemeColors } from '../lib/theme';

export type Appearance = 'dark' | 'light' | 'system';
type Value = {
  colors: ThemeColors; ui: ReturnType<typeof makeUI>; isDark: boolean;
  appearance: Appearance; setAppearance(value: Appearance): Promise<void>;
};
const KEY = '@shikhsha/appearance/v1';
const Context = createContext<Value | null>(null);
export function ThemeProvider({ children }: React.PropsWithChildren) {
  const [appearance, setValue] = useState<Appearance>('dark');
  const system = useColorScheme();
  useEffect(() => {
    let active = true;
    void AsyncStorage.getItem(KEY).then(value => {
      if (active && (value === 'dark' || value === 'light' || value === 'system')) setValue(value);
    }).catch(() => undefined);
    return () => { active = false; };
  }, []);
  const isDark = appearance === 'system' ? system !== 'light' : appearance === 'dark';
  const colors = isDark ? darkColors : lightColors;
  const ui = useMemo(() => makeUI(colors), [colors]);
  const setAppearance = async (value: Appearance) => {
    await AsyncStorage.setItem(KEY, value);
    setValue(value);
  };
  return <Context.Provider value={{ colors, ui, isDark, appearance, setAppearance }}>{children}</Context.Provider>;
}
export function useTheme() {
  const value = useContext(Context);
  if (!value) throw new Error('ThemeProvider is missing');
  return value;
}
