package org.cocos2dx.lib;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import java.io.IOException;

public class Cocos2dxVideoView extends SurfaceView implements OnCompletionListener, OnErrorListener, OnInfoListener, Callback, OnTouchListener {
    private static final String TAG = "VideoView";
    public static Cocos2dxActivity activityInstance;
    private static long videoFinishLimit;
    private static long videoStartTime;
    private AssetFileDescriptor fd;
    int luaOnFinishCallback;
    private MediaPlayer mPlayer;
    int posttion;

    /* renamed from: org.cocos2dx.lib.Cocos2dxVideoView.1 */
    class C15681 implements Runnable {
        final /* synthetic */ Cocos2dxActivity val$instance;

        C15681(Cocos2dxActivity cocos2dxActivity) {
            this.val$instance = cocos2dxActivity;
        }

        public void run() {
            try {
                ((ViewGroup) this.val$instance.getWindow().getDecorView()).removeView(Cocos2dxVideoView.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: org.cocos2dx.lib.Cocos2dxVideoView.2 */
    static final class C15692 implements Runnable {
        final /* synthetic */ Cocos2dxActivity val$instance;
        final /* synthetic */ String val$name;

        C15692(Cocos2dxActivity cocos2dxActivity, String str) {
            this.val$instance = cocos2dxActivity;
            this.val$name = str;
        }

        public void run() {
        	Cocos2dxVideoView cocos2dxVideoView = new Cocos2dxVideoView(this.val$instance);
            try {
                cocos2dxVideoView.setVideo(this.val$instance.getAssets().openFd(this.val$name));
                ((ViewGroup) this.val$instance.getWindow().getDecorView()).addView(cocos2dxVideoView);
                cocos2dxVideoView.setZOrderMediaOverlay(true);
            } catch (IOException e) {
                e.printStackTrace();
                cocos2dxVideoView.onVideoFinish();
            }
        }
    }

    static {
        videoStartTime = 0;
        videoFinishLimit = 5000;
    }

    public Cocos2dxVideoView(Activity activity) {
        super(activity);
        getHolder().addCallback(this);
        setOnTouchListener(this);
    }

    public static void playVideo(String str) {
        Cocos2dxActivity cocos2dxActivity = activityInstance;
        videoStartTime = System.currentTimeMillis();
        if (cocos2dxActivity != null) {
            cocos2dxActivity.runOnUiThread(new C15692(cocos2dxActivity, str));
        }
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.i(TAG, "onCompletion");
        onVideoFinish();
    }

    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        System.out.println("play error:" + i + "," + i2);
        return false;
    }

    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
        return true;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            long currentTimeMillis = System.currentTimeMillis();
            System.out.println("key down: nowSecns=" + currentTimeMillis + ",limitSecns" + (videoStartTime + videoFinishLimit));
            if (currentTimeMillis > videoStartTime + videoFinishLimit) {
                onVideoFinish();
            }
        }
        return true;
    }

    public void onVideoFinish() {
        Log.i(TAG, "onVideoFinish");
        try {
            if (this.mPlayer != null) {
                this.mPlayer.stop();
                this.mPlayer.release();
                this.mPlayer = null;
            }
            if (this.fd != null) {
                this.fd.close();
                this.fd = null;
            }
        } catch (Exception e) {
            Log.i(TAG, "onVideoFinish error");
            e.printStackTrace();
        }
        try {
            Cocos2dxActivity cocos2dxActivity = activityInstance;
            cocos2dxActivity.runOnUiThread(new C15681(cocos2dxActivity));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void setVideo(AssetFileDescriptor assetFileDescriptor) {
        this.fd = assetFileDescriptor;
        try {
            this.mPlayer = new MediaPlayer();
            this.mPlayer.setScreenOnWhilePlaying(true);
            this.mPlayer.setOnCompletionListener(this);
            this.mPlayer.setOnErrorListener(this);
            this.mPlayer.setOnInfoListener(this);
            this.mPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            this.mPlayer.prepare();
            this.posttion = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            int width = getWidth();
            int height = getHeight();
            int videoWidth = this.mPlayer.getVideoWidth();
            int videoHeight = this.mPlayer.getVideoHeight();
            float max = Math.max(((float) videoWidth) / ((float) width), ((float) videoHeight) / ((float) height));
            surfaceHolder.setFixedSize((int) Math.ceil((double) (((float) videoWidth) / max)), (int) Math.ceil((double) (((float) videoHeight) / max)));
            this.mPlayer.setDisplay(surfaceHolder);
            this.mPlayer.seekTo(this.posttion);
            this.mPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
            onVideoFinish();
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        try {
            Log.i(TAG, "surfaceDestroyed");
            if (this.mPlayer != null) {
                this.posttion = this.mPlayer.getCurrentPosition();
                this.mPlayer.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
