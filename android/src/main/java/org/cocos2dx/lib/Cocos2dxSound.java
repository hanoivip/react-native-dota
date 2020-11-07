package org.cocos2dx.lib;

import android.content.Context;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

public class Cocos2dxSound {
    private static final int INVALID_SOUND_ID = -1;
    private static final int INVALID_STREAM_ID = -1;
    private static final int MAX_SIMULTANEOUS_STREAMS_DEFAULT = 5;
    private static final int SOUND_PRIORITY = 1;
    private static final int SOUND_QUALITY = 5;
    private static final float SOUND_RATE = 1.0f;
    private static final String TAG = "Cocos2dxSound";
    private final Context mContext;
    private final ArrayList<SoundInfoForLoadedCompleted> mEffecToPlayWhenLoadedArray;
    private float mLeftVolume;
    private final HashMap<String, Integer> mPathSoundIDMap;
    private final HashMap<String, ArrayList<Integer>> mPathStreamIDsMap;
    private float mRightVolume;
    private Semaphore mSemaphore;
    private SoundPool mSoundPool;
    private int mStreamIdSyn;

    public class OnLoadCompletedListener implements OnLoadCompleteListener {
        public void onLoadComplete(SoundPool soundPool, int i, int i2) {
            if (i2 == 0) {
                Iterator it = Cocos2dxSound.this.mEffecToPlayWhenLoadedArray.iterator();
                while (it.hasNext()) {
                    SoundInfoForLoadedCompleted soundInfoForLoadedCompleted = (SoundInfoForLoadedCompleted) it.next();
                    if (i == soundInfoForLoadedCompleted.soundID) {
                        Cocos2dxSound.this.mStreamIdSyn = Cocos2dxSound.this.doPlayEffect(soundInfoForLoadedCompleted.path, soundInfoForLoadedCompleted.soundID, soundInfoForLoadedCompleted.isLoop);
                        Cocos2dxSound.this.mEffecToPlayWhenLoadedArray.remove(soundInfoForLoadedCompleted);
                        break;
                    }
                }
            }
            Cocos2dxSound.this.mStreamIdSyn = Cocos2dxSound.INVALID_STREAM_ID;
            Cocos2dxSound.this.mSemaphore.release();
        }
    }

    public class SoundInfoForLoadedCompleted {
        public boolean isLoop;
        public String path;
        public int soundID;

        public SoundInfoForLoadedCompleted(String str, int i, boolean z) {
            this.path = str;
            this.soundID = i;
            this.isLoop = z;
        }
    }

    public Cocos2dxSound(Context context) {
        this.mPathStreamIDsMap = new HashMap();
        this.mPathSoundIDMap = new HashMap();
        this.mEffecToPlayWhenLoadedArray = new ArrayList();
        this.mContext = context;
        initData();
    }

    private int doPlayEffect(String str, int i, boolean z) {
        int play = this.mSoundPool.play(i, this.mLeftVolume, this.mRightVolume, SOUND_PRIORITY, z ? INVALID_STREAM_ID : 0, SOUND_RATE);
        ArrayList arrayList = (ArrayList) this.mPathStreamIDsMap.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.mPathStreamIDsMap.put(str, arrayList);
        }
        arrayList.add(Integer.valueOf(play));
        return play;
    }

    private void initData() {
        this.mSoundPool = new SoundPool(SOUND_QUALITY, 3, SOUND_QUALITY);
        this.mSoundPool.setOnLoadCompleteListener(new OnLoadCompletedListener());
        this.mLeftVolume = 0.5f;
        this.mRightVolume = 0.5f;
        this.mSemaphore = new Semaphore(0, true);
    }

    public int createSoundIDFromAsset(String str) {
        int load;
        try {
            load = str.startsWith("/") ? this.mSoundPool.load(str, 0) : this.mSoundPool.load(this.mContext.getAssets().openFd(str), 0);
        } catch (Throwable e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
            load = INVALID_STREAM_ID;
        }
        return load == 0 ? INVALID_STREAM_ID : load;
    }

    public void end() {
        this.mSoundPool.release();
        this.mPathStreamIDsMap.clear();
        this.mPathSoundIDMap.clear();
        this.mEffecToPlayWhenLoadedArray.clear();
        this.mLeftVolume = 0.5f;
        this.mRightVolume = 0.5f;
        initData();
    }

    public void gcEffects() {
        for (String str : this.mPathSoundIDMap.keySet()) {
            ArrayList arrayList = (ArrayList) this.mPathStreamIDsMap.get(str);
            if (arrayList != null) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    this.mSoundPool.stop(((Integer) it.next()).intValue());
                }
            }
            this.mPathStreamIDsMap.remove(str);
            Integer num = (Integer) this.mPathSoundIDMap.get(str);
            if (num != null) {
                this.mSoundPool.unload(num.intValue());
            }
        }
        this.mPathSoundIDMap.clear();
    }

    public float getEffectsVolume() {
        return (this.mLeftVolume + this.mRightVolume) / 2.0f;
    }

    public void pauseAllEffects() {
        this.mSoundPool.autoPause();
    }

    public void pauseEffect(int i) {
        this.mSoundPool.pause(i);
    }

    public int playEffect(String str, boolean z) {
        int doPlayEffect;
        Log.d(TAG, "File loaded: " + str);
        Integer num = (Integer) this.mPathSoundIDMap.get(str);
        if (num != null) {
            doPlayEffect = doPlayEffect(str, num.intValue(), z);
        } else {
            num = Integer.valueOf(preloadEffect(str));
            if (num.intValue() != INVALID_STREAM_ID) {
                synchronized (this.mSoundPool) {
                    this.mEffecToPlayWhenLoadedArray.add(new SoundInfoForLoadedCompleted(str, num.intValue(), z));
                    try {
                        this.mSemaphore.acquire();
                        doPlayEffect = this.mStreamIdSyn;
                    } catch (Exception e) {
                        return INVALID_STREAM_ID;
                    }
                }
            }
            doPlayEffect = INVALID_STREAM_ID;
        }
        return doPlayEffect;
    }

    public int preloadEffect(String str) {
        Integer num = (Integer) this.mPathSoundIDMap.get(str);
        if (num == null) {
            num = Integer.valueOf(createSoundIDFromAsset(str));
            if (num.intValue() != INVALID_STREAM_ID) {
                this.mPathSoundIDMap.put(str, num);
            }
        }
        return num.intValue();
    }

    public void resumeAllEffects() {
        if (!this.mPathStreamIDsMap.isEmpty()) {
            for (Entry value : this.mPathStreamIDsMap.entrySet()) {
                Iterator it = ((ArrayList) value.getValue()).iterator();
                while (it.hasNext()) {
                    this.mSoundPool.resume(((Integer) it.next()).intValue());
                }
            }
        }
    }

    public void resumeEffect(int i) {
        this.mSoundPool.resume(i);
    }

    public void setEffectsVolume(float f) {
        float f2 = SOUND_RATE;
        float f3 = 0.0f;
        if (f >= 0.0f) {
            f3 = f;
        }
        if (f3 <= SOUND_RATE) {
            f2 = f3;
        }
        this.mRightVolume = f2;
        this.mLeftVolume = f2;
        if (!this.mPathStreamIDsMap.isEmpty()) {
            for (Entry value : this.mPathStreamIDsMap.entrySet()) {
                Iterator it = ((ArrayList) value.getValue()).iterator();
                while (it.hasNext()) {
                    this.mSoundPool.setVolume(((Integer) it.next()).intValue(), this.mLeftVolume, this.mRightVolume);
                }
            }
        }
    }

    public void stopAllEffects() {
        if (!this.mPathStreamIDsMap.isEmpty()) {
            for (Entry value : this.mPathStreamIDsMap.entrySet()) {
                Iterator it = ((ArrayList) value.getValue()).iterator();
                while (it.hasNext()) {
                    this.mSoundPool.stop(((Integer) it.next()).intValue());
                }
            }
        }
        this.mPathStreamIDsMap.clear();
    }

    public void stopEffect(int i) {
        this.mSoundPool.stop(i);
        for (String str : this.mPathStreamIDsMap.keySet()) {
            if (((ArrayList) this.mPathStreamIDsMap.get(str)).contains(Integer.valueOf(i))) {
                ((ArrayList) this.mPathStreamIDsMap.get(str)).remove(((ArrayList) this.mPathStreamIDsMap.get(str)).indexOf(Integer.valueOf(i)));
                return;
            }
        }
    }

    public void unloadEffect(String str) {
        ArrayList arrayList = (ArrayList) this.mPathStreamIDsMap.get(str);
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                this.mSoundPool.stop(((Integer) it.next()).intValue());
            }
        }
        this.mPathStreamIDsMap.remove(str);
        Integer num = (Integer) this.mPathSoundIDMap.get(str);
        if (num != null) {
            this.mSoundPool.unload(num.intValue());
            this.mPathSoundIDMap.remove(str);
        }
    }
}
