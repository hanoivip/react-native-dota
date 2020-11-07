package org.cocos2dx.lib;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import com.loopj.android.http.BuildConfig;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class Cocos2dxHelper {
    private static final String PREFS_NAME = "Cocos2dxPrefsFile";
    private static boolean sAccelerometerEnabled;
    private static AssetManager sAssetManager;
    private static Cocos2dxMusic sCocos2dMusic;
    private static Cocos2dxSound sCocos2dSound;
    private static Cocos2dxAccelerometer sCocos2dxAccelerometer;
    private static Cocos2dxHelperListener sCocos2dxHelperListener;
    private static Context sContext;
    private static String sFileDirectory;
    private static String sPackageName;

    public interface Cocos2dxHelperListener {
        void runOnGLThread(Runnable runnable);

        void showDialog(String str, String str2);

        void showEditTextDialog(String str, String str2, int i, int i2, int i3, int i4);
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxHelper.1 */
    static final class C24831 implements Runnable {
        final /* synthetic */ byte[] val$bytesUTF8;

        C24831(byte[] bArr) {
            this.val$bytesUTF8 = bArr;
        }

        public void run() {
            Cocos2dxHelper.nativeSetEditTextDialogResult(this.val$bytesUTF8);
        }
    }

    static {
        sContext = null;
    }

    public static void disableAccelerometer() {
        sAccelerometerEnabled = false;
        sCocos2dxAccelerometer.disable();
    }

    public static void enableAccelerometer() {
        sAccelerometerEnabled = true;
        sCocos2dxAccelerometer.enable();
    }

    public static void end() {
        sCocos2dMusic.end();
        sCocos2dSound.end();
    }

    public static void gcEffects() {
        sCocos2dSound.gcEffects();
    }

    public static int getAndroidVersion() {
        return VERSION.SDK_INT;
    }

    public static AssetManager getAssetManager() {
        return sAssetManager;
    }

    public static float getBackgroundMusicVolume() {
        return sCocos2dMusic.getBackgroundVolume();
    }

    public static boolean getBoolForKey(String str, boolean z) {
        return ((Activity) sContext).getSharedPreferences(PREFS_NAME, 0).getBoolean(str, z);
    }

    public static String getCocos2dxPackageName() {
        return sPackageName;
    }

    public static String getCocos2dxWritablePath() {
        return sFileDirectory;
    }

    public static String getCurrentLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static int getDPI() {
        if (sContext != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = ((Activity) sContext).getWindowManager();
            if (windowManager != null) {
                Display defaultDisplay = windowManager.getDefaultDisplay();
                if (defaultDisplay != null) {
                    defaultDisplay.getMetrics(displayMetrics);
                    return (int) (displayMetrics.density * 160.0f);
                }
            }
        }
        return -1;
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static double getDoubleForKey(String str, double d) {
        return (double) ((Activity) sContext).getSharedPreferences(PREFS_NAME, 0).getFloat(str, (float) d);
    }

    public static float getEffectsVolume() {
        return sCocos2dSound.getEffectsVolume();
    }

    public static float getFloatForKey(String str, float f) {
        return ((Activity) sContext).getSharedPreferences(PREFS_NAME, 0).getFloat(str, f);
    }

    public static int getIntegerForKey(String str, int i) {
        return ((Activity) sContext).getSharedPreferences(PREFS_NAME, 0).getInt(str, i);
    }

    public static String getStringForKey(String str, String str2) {
        return ((Activity) sContext).getSharedPreferences(PREFS_NAME, 0).getString(str, str2);
    }

    public static void init(Context context, Cocos2dxHelperListener cocos2dxHelperListener) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        sContext = context;
        sCocos2dxHelperListener = cocos2dxHelperListener;
        sPackageName = applicationInfo.packageName;
        sFileDirectory = context.getFilesDir().getAbsolutePath();
        nativeSetApkPath(applicationInfo.sourceDir);
        sCocos2dxAccelerometer = new Cocos2dxAccelerometer(context);
        sCocos2dMusic = new Cocos2dxMusic(context);
        sCocos2dSound = new Cocos2dxSound(context);
        sAssetManager = context.getAssets();
        nativeSetContext(context, sAssetManager);
        Cocos2dxBitmap.setContext(context);
    }

    public static boolean isBackgroundMusicPlaying() {
        return sCocos2dMusic.isBackgroundMusicPlaying();
    }

    private static native void nativeSetApkPath(String str);

    private static native void nativeSetContext(Context context, AssetManager assetManager);

    private static native void nativeSetEditTextDialogResult(byte[] bArr);

    public static void onPause() {
        if (sAccelerometerEnabled) {
            sCocos2dxAccelerometer.disable();
        }
    }

    public static void onResume() {
        if (sAccelerometerEnabled) {
            sCocos2dxAccelerometer.enable();
        }
    }

    public static void pauseAllEffects() {
        sCocos2dSound.pauseAllEffects();
    }

    public static void pauseBackgroundMusic() {
        sCocos2dMusic.pauseBackgroundMusic();
    }

    public static void pauseEffect(int i) {
        sCocos2dSound.pauseEffect(i);
    }

    public static void playBackgroundMusic(String str, boolean z) {
        sCocos2dMusic.playBackgroundMusic(str, z);
    }

    public static int playEffect(String str, boolean z) {
        return sCocos2dSound.playEffect(str, z);
    }

    public static void playVideo(String str) {
        Cocos2dxVideoView.playVideo(str);
    }

    public static void preloadBackgroundMusic(String str) {
        sCocos2dMusic.preloadBackgroundMusic(str);
    }

    public static void preloadEffect(String str) {
        sCocos2dSound.preloadEffect(str);
    }

    public static void resumeAllEffects() {
        sCocos2dSound.resumeAllEffects();
    }

    public static void resumeBackgroundMusic() {
        sCocos2dMusic.resumeBackgroundMusic();
    }

    public static void resumeEffect(int i) {
        sCocos2dSound.resumeEffect(i);
    }

    public static void rewindBackgroundMusic() {
        sCocos2dMusic.rewindBackgroundMusic();
    }

    public static void setAccelerometerInterval(float f) {
        sCocos2dxAccelerometer.setInterval(f);
    }

    public static void setBackgroundMusicVolume(float f) {
        sCocos2dMusic.setBackgroundVolume(f);
    }

    public static void setBoolForKey(String str, boolean z) {
        Editor edit = ((Activity) sContext).getSharedPreferences(PREFS_NAME, 0).edit();
        edit.putBoolean(str, z);
        edit.commit();
    }

    public static void setDoubleForKey(String str, double d) {
        Editor edit = ((Activity) sContext).getSharedPreferences(PREFS_NAME, 0).edit();
        edit.putFloat(str, (float) d);
        edit.commit();
    }

    public static void setEditTextDialogResult(String str) {
        try {
            sCocos2dxHelperListener.runOnGLThread(new C24831(str.getBytes("UTF8")));
        } catch (UnsupportedEncodingException e) {
        }
    }

    public static void setEffectsVolume(float f) {
        try {
            sCocos2dSound.setEffectsVolume(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFloatForKey(String str, float f) {
        Editor edit = ((Activity) sContext).getSharedPreferences(PREFS_NAME, 0).edit();
        edit.putFloat(str, f);
        edit.commit();
    }

    public static void setIntegerForKey(String str, int i) {
        Editor edit = ((Activity) sContext).getSharedPreferences(PREFS_NAME, 0).edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public static void setStringForKey(String str, String str2) {
        Editor edit = ((Activity) sContext).getSharedPreferences(PREFS_NAME, 0).edit();
        edit.putString(str, str2);
        edit.commit();
    }

    private static void showDialog(String str, String str2) {
        sCocos2dxHelperListener.showDialog(str, str2);
    }

    private static void showEditTextDialog(byte[] bArr, String str, int i, int i2, int i3, int i4) {
        String str2 = (bArr == null || bArr.length == 0) ? BuildConfig.VERSION_NAME : new String(bArr);
        sCocos2dxHelperListener.showEditTextDialog(str, str2, i, i2, i3, i4);
    }

    public static void stopAllEffects() {
        sCocos2dSound.stopAllEffects();
    }

    public static void stopBackgroundMusic() {
        sCocos2dMusic.stopBackgroundMusic();
    }

    public static void stopEffect(int i) {
        sCocos2dSound.stopEffect(i);
    }

    public static void terminateProcess() {
        Process.killProcess(Process.myPid());
    }

    public static void unloadEffect(String str) {
        sCocos2dSound.unloadEffect(str);
    }
}
