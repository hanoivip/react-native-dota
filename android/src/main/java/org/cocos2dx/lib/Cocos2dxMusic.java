package org.cocos2dx.lib;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;
import java.io.FileInputStream;

public class Cocos2dxMusic {
    private static final String TAG;
    private MediaPlayer mBackgroundMediaPlayer;
    private final Context mContext;
    private String mCurrentPath;
    private boolean mIsLoop;
    private float mLeftVolume;
    private boolean mManualPaused;
    private boolean mPaused;
    private float mRightVolume;

    static {
        TAG = Cocos2dxMusic.class.getSimpleName();
    }

    public Cocos2dxMusic(Context context) {
        this.mIsLoop = false;
        this.mManualPaused = false;
        this.mContext = context;
        initData();
    }

    private MediaPlayer createMediaplayer(String str) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(3);
        try {
            if (str.startsWith("/")) {
                FileInputStream fileInputStream = new FileInputStream(str);
                mediaPlayer.setDataSource(fileInputStream.getFD());
                fileInputStream.close();
            } else {
                AssetFileDescriptor openFd = this.mContext.getAssets().openFd(str);
                mediaPlayer.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
            }
            mediaPlayer.prepare();
            mediaPlayer.setVolume(this.mLeftVolume, this.mRightVolume);
            return mediaPlayer;
        } catch (Throwable e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
            return null;
        }
    }

    private void initData() {
        this.mLeftVolume = 0.5f;
        this.mRightVolume = 0.5f;
        this.mBackgroundMediaPlayer = null;
        this.mPaused = false;
        this.mCurrentPath = null;
    }

    public void end() {
        if (this.mBackgroundMediaPlayer != null) {
            this.mBackgroundMediaPlayer.release();
        }
        initData();
    }

    public float getBackgroundVolume() {
        return this.mBackgroundMediaPlayer != null ? (this.mLeftVolume + this.mRightVolume) / 2.0f : 0.0f;
    }

    public boolean isBackgroundMusicPlaying() {
        return this.mBackgroundMediaPlayer == null ? false : this.mBackgroundMediaPlayer.isPlaying();
    }

    public void pauseBackgroundMusic() {
        if (this.mBackgroundMediaPlayer != null && this.mBackgroundMediaPlayer.isPlaying()) {
            this.mBackgroundMediaPlayer.pause();
            this.mPaused = true;
        }
    }

    public void playBackgroundMusic(String str, boolean z) {
        Log.d(TAG, "File loaded: " + str);
        if (this.mCurrentPath == null) {
            this.mBackgroundMediaPlayer = createMediaplayer(str);
            this.mCurrentPath = str;
        } else if (!this.mCurrentPath.equals(str)) {
            if (this.mBackgroundMediaPlayer != null) {
                this.mBackgroundMediaPlayer.release();
            }
            this.mBackgroundMediaPlayer = createMediaplayer(str);
            this.mCurrentPath = str;
        }
        if (this.mBackgroundMediaPlayer == null) {
            Log.e(TAG, "playBackgroundMusic: background media player is null");
            return;
        }
        try {
            if (this.mPaused) {
                this.mBackgroundMediaPlayer.seekTo(0);
                this.mBackgroundMediaPlayer.setLooping(z);
                this.mBackgroundMediaPlayer.start();
            } else if (this.mBackgroundMediaPlayer.isPlaying()) {
                this.mBackgroundMediaPlayer.seekTo(0);
                this.mBackgroundMediaPlayer.setLooping(z);
            } else {
                this.mBackgroundMediaPlayer.setLooping(z);
                this.mBackgroundMediaPlayer.start();
                this.mPaused = false;
                this.mIsLoop = z;
            }
        } catch (Exception e) {
            Log.e(TAG, "playBackgroundMusic: error state");
        }
    }

    public void preloadBackgroundMusic(String str) {
        if (this.mCurrentPath == null || !this.mCurrentPath.equals(str)) {
            if (this.mBackgroundMediaPlayer != null) {
                this.mBackgroundMediaPlayer.release();
            }
            this.mBackgroundMediaPlayer = createMediaplayer(str);
            this.mCurrentPath = str;
        }
    }

    public void resumeBackgroundMusic() {
        if (this.mBackgroundMediaPlayer != null && this.mPaused) {
            this.mBackgroundMediaPlayer.start();
            this.mPaused = false;
        }
    }

    public void rewindBackgroundMusic() {
        if (this.mBackgroundMediaPlayer != null) {
            playBackgroundMusic(this.mCurrentPath, this.mIsLoop);
        }
    }

    public void setBackgroundVolume(float f) {
        float f2 = 1.0f;
        float f3 = 0.0f;
        if (f >= 0.0f) {
            f3 = f;
        }
        if (f3 <= 1.0f) {
            f2 = f3;
        }
        this.mRightVolume = f2;
        this.mLeftVolume = f2;
        if (this.mBackgroundMediaPlayer != null) {
            this.mBackgroundMediaPlayer.setVolume(this.mLeftVolume, this.mRightVolume);
        }
    }

    public void stopBackgroundMusic() {
        if (this.mBackgroundMediaPlayer != null) {
            this.mBackgroundMediaPlayer.release();
            this.mBackgroundMediaPlayer = createMediaplayer(this.mCurrentPath);
            this.mPaused = false;
        }
    }
}
