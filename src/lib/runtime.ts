import { isRunningInExpoGo } from 'expo';
import { Platform } from 'react-native';

export const expoGo = isRunningInExpoGo();
export const iphoneExpoGo = Platform.OS === 'ios' && expoGo;
