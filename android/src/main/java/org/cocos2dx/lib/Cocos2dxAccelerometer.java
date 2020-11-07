package org.cocos2dx.lib;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build.VERSION;
import android.view.WindowManager;

public class Cocos2dxAccelerometer implements SensorEventListener {
    private static final String TAG;
    private final Sensor mAccelerometer;
    private final Context mContext;
    private final int mNaturalOrientation;
    private final SensorManager mSensorManager;

    static {
        TAG = Cocos2dxAccelerometer.class.getSimpleName();
    }

    public Cocos2dxAccelerometer(Context context) {
        this.mContext = context;
        this.mSensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(1);
        this.mNaturalOrientation = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay().getOrientation();
    }

    public static native void onSensorChanged(float f, float f2, float f3, long j);

    public void disable() {
        this.mSensorManager.unregisterListener(this);
    }

    public void enable() {
        this.mSensorManager.registerListener(this, this.mAccelerometer, 1);
    }

    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == 1) {
            float f = sensorEvent.values[0];
            float f2 = sensorEvent.values[1];
            float f3 = sensorEvent.values[2];
            int i = this.mContext.getResources().getConfiguration().orientation;
            if (i == 2 && this.mNaturalOrientation != 0) {
                f2 = -f2;
            } else if (i != 1 || this.mNaturalOrientation == 0) {
                float f4 = f2;
                f2 = f;
                f = f4;
            } else {
                f = -f;
            }
            Cocos2dxGLSurfaceView.queueAccelerometer(f2, f, f3, sensorEvent.timestamp);
        }
    }

    public void setInterval(float f) {
        if (VERSION.SDK_INT < 11) {
            this.mSensorManager.registerListener(this, this.mAccelerometer, 1);
        } else {
            this.mSensorManager.registerListener(this, this.mAccelerometer, (int) (100000.0f * f));
        }
    }
}
