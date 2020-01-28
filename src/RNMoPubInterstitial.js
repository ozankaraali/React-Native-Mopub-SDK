import {NativeModules, NativeEventEmitter} from 'react-native';
const { RNMoPubInterstitial } = NativeModules;


const emitter = new NativeEventEmitter(RNMoPubInterstitial);

module.exports = {
    initializeInterstitialAd: (adUnitId: string, shouldShowGDPR: bool) => RNMoPubInterstitial.initializeInterstitialAd(adUnitId, shouldShowGDPR),
    loadAd: () => RNMoPubInterstitial.loadAd(),
    setKeywords: (keywords: string) => RNMoPubInterstitial.setKeywords(keywords),
    isReady: (): Promise => RNMoPubInterstitial.isReady(),
    show: () => RNMoPubInterstitial.show(),
    addEventListener: (eventType: string, listener: Function)  => emitter.addListener(eventType, listener),
    removeAllListeners: (eventType: string) => emitter.removeAllListeners(eventType)
};