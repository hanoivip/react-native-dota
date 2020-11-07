package org.cocos2dx.lib;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import org.cocos2dx.lib.Cocos2dxHandler.DialogMessage;
import org.cocos2dx.lib.Cocos2dxHandler.EditBoxMessage;
import org.cocos2dx.lib.Cocos2dxHelper.Cocos2dxHelperListener;

public abstract class Cocos2dxActivity extends Activity implements Cocos2dxHelperListener {
    private static final String TAG;
    private static Context sContext;
    private Cocos2dxGLSurfaceView mGLSurfaceView;
    private Cocos2dxHandler mHandler;

    static {
        TAG = Cocos2dxActivity.class.getSimpleName();
        sContext = null;
    }

    public static Context getContext() {
        return sContext;
    }

    public void init() {
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        FrameLayout frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(layoutParams);
        layoutParams = new LayoutParams(-1, -2);
        Cocos2dxEditText cocos2dxEditText = new Cocos2dxEditText(this);
        cocos2dxEditText.setLayoutParams(layoutParams);
        frameLayout.addView(cocos2dxEditText);
        this.mGLSurfaceView = onCreateView();
        this.mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
        this.mGLSurfaceView.setKeepScreenOn(true);
        frameLayout.addView(this.mGLSurfaceView);
        this.mGLSurfaceView.setCocos2dxRenderer(new Cocos2dxRenderer());
        this.mGLSurfaceView.setCocos2dxEditText(cocos2dxEditText);
        setContentView(frameLayout);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        sContext = this;
        this.mHandler = new Cocos2dxHandler(this);
        init();
        Cocos2dxHelper.init(this, this);
    }

    public Cocos2dxGLSurfaceView onCreateView() {
        return new Cocos2dxGLSurfaceView(this);
    }

    protected void onPause() {
        super.onPause();
        Cocos2dxHelper.onPause();
        this.mGLSurfaceView.onPause();
    }

    protected void onResume() {
        super.onResume();
        Cocos2dxHelper.onResume();
        this.mGLSurfaceView.onResume();
    }

    public void runOnGLThread(Runnable runnable) {
        this.mGLSurfaceView.queueEvent(runnable);
    }

    public void showDialog(String str, String str2) {
        Message message = new Message();
        message.what = 1;
        message.obj = new DialogMessage(str, str2);
        this.mHandler.sendMessage(message);
    }

    public void showEditTextDialog(String str, String str2, int i, int i2, int i3, int i4) {
        Message message = new Message();
        message.what = 2;
        message.obj = new EditBoxMessage(str, str2, i, i2, i3, i4);
        this.mHandler.sendMessage(message);
    }
}
