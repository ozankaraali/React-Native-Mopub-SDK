package com.reactlibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.mopub.common.MoPub;
import com.mopub.common.SdkConfiguration;
import com.mopub.common.SdkInitializationListener;
import com.mopub.common.privacy.ConsentDialogListener;
import com.mopub.common.privacy.PersonalInfoManager;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.common.logging.MoPubLog;
import androidx.annotation.NonNull;

/**
 * Created by usamaazam on 29/03/2019.
 */

public class AdLibSDK {
    static void initializeAdSDK(final RNMoPubBanner banner, final String adUnitId, final Activity context) {
        initializeAdSDKWithGDPR(banner, adUnitId, context, false);
    }

    static void initializeAdSDKWithGDPR(final RNMoPubBanner banner, final String adUnitId, final Activity context, final Boolean shouldShowGDPR) {

        Handler mainHandler = new Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            
            @Override
            public void run() {
                SdkConfiguration sdkConfiguration = new SdkConfiguration.Builder(adUnitId)
                        .withLogLevel(MoPubLog.LogLevel.DEBUG)
                        .withLegitimateInterestAllowed(true)
                        .build();

                MoPub.initializeSdk(context, sdkConfiguration, initSdkListener());

            }

            PersonalInfoManager mPersonalInfoManager = MoPub.getPersonalInformationManager();

            private SdkInitializationListener initSdkListener() {
                return new SdkInitializationListener() {
                    @Override
                    public void onInitializationFinished() {
                        if (mPersonalInfoManager != null && mPersonalInfoManager.shouldShowConsentDialog()) {
                            mPersonalInfoManager.loadConsentDialog(initDialogLoadListener());
                        }
                        
                        if (banner != null) {
                            banner.setAdUnitId(adUnitId);
                            banner.loadAd();
                        }

                    }
                };
            }

            private ConsentDialogListener initDialogLoadListener() {
                return new ConsentDialogListener() {
        
                    @Override
                    public void onConsentDialogLoaded() {
                        if (mPersonalInfoManager != null) {
                            mPersonalInfoManager.showConsentDialog();
                            //mShowingConsentDialog = true;
                        }
                    }
        
                    @Override
                    public void onConsentDialogLoadFailed(@NonNull MoPubErrorCode moPubErrorCode) {
                        MoPubLog.i("Consent dialog failed to load.");
                        // Utils.logToast(MoPubSampleActivity.this, "Consent dialog failed to load.");
                    }
                };
            }
        };
        mainHandler.post(myRunnable);

    }
}
