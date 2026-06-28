import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.wavycards.blackjack',
  appName: 'Wavy Cards',
  webDir: 'dist',
  server: {
    androidScheme: 'http',
    cleartext: true, // allow ws:// on Android (required for LAN play)
  },
};

export default config;
