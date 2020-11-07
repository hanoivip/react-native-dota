package org.cocos2dx.lib;

import android.opengl.GLSurfaceView.Renderer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Cocos2dxRenderer implements Renderer {
    private static final long NANOSECONDSPERMICROSECOND = 1000000;
    private static final long NANOSECONDSPERSECOND = 1000000000;
    private static long sAnimationInterval;
    private long mLastTickInNanoSeconds;
    private int mScreenHeight;
    private int mScreenWidth;

    static {
        sAnimationInterval = 16666666;
    }

    private static native void nativeDeleteBackward();

    private static native String nativeGetContentText();

    private static native void nativeInit(int i, int i2);

    private static native void nativeInsertText(String str);

    private static native boolean nativeKeyDown(int i);

    private static native void nativeOnPause();

    private static native void nativeOnResume();

    private static native void nativeRender();

    private static native void nativeTouchesBegin(int i, float f, float f2);

    private static native void nativeTouchesCancel(int[] iArr, float[] fArr, float[] fArr2);

    private static native void nativeTouchesEnd(int i, float f, float f2);

    private static native void nativeTouchesMove(int[] iArr, float[] fArr, float[] fArr2);

    public static void setAnimationInterval(double d) {
        sAnimationInterval = (long) (1.0E9d * d);
    }

    public String getContentText() {
        return nativeGetContentText();
    }

    public void handleActionCancel(int[] iArr, float[] fArr, float[] fArr2) {
        nativeTouchesCancel(iArr, fArr, fArr2);
    }

    public void handleActionDown(int i, float f, float f2) {
        nativeTouchesBegin(i, f, f2);
    }

    public void handleActionMove(int[] iArr, float[] fArr, float[] fArr2) {
        nativeTouchesMove(iArr, fArr, fArr2);
    }

    public void handleActionUp(int i, float f, float f2) {
        nativeTouchesEnd(i, f, f2);
    }

    public void handleDeleteBackward() {
        nativeDeleteBackward();
    }

    public void handleInsertText(String str) {
        nativeInsertText(str);
    }

    public void handleKeyDown(int i) {
        nativeKeyDown(i);
    }

    public void handleOnPause() {
        nativeOnPause();
    }

    public void handleOnResume() {
        nativeOnResume();
    }

    public void onDrawFrame(GL10 gl10) {
        try {
            nativeRender();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
    }

    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        nativeInit(this.mScreenWidth, this.mScreenHeight);
        this.mLastTickInNanoSeconds = System.nanoTime();
    }

    public void setScreenWidthAndHeight(int i, int i2) {
        this.mScreenWidth = i;
        this.mScreenHeight = i2;
    }
}
