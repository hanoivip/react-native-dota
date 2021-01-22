package com.chukong.cocosplay.client;

import android.app.Activity;
import com.loopj.android.http.BuildConfig;

public final class CocosPlayClient {
    
    public static Activity m_activity = null;

    public static boolean fileExists(String str) {
        return false;
    }

    public static String getGameRoot() {
        return BuildConfig.VERSION_NAME;
    }

    public static boolean init(Activity activity, boolean z) {
        m_activity = activity;
        return z;
    }

    public static boolean isDemo() {
        return false;
    }

    public static boolean isEnabled() {
        return false;
    }

    public static void notifyDemoEnded() {
    }

    public static String updateAssetsAndReturnFullPath(String str) {
        return BuildConfig.VERSION_NAME;
    }
}
