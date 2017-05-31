package com.example.przemek.egzaminel.Tools;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.przemek.egzaminel.Interfaces.OnShakeListener;

public class ShakeDetector implements SensorEventListener {


    private float shakeThresholdGravity = 2.7f;
    private int shakeSlopTimeMs = 1000;
    private int shakeCountResetTimeMs = 3000;

    private OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCounter;

    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mListener == null) return;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        //calculate force - will be near 1 if there is no movement
        float gForce = (float) Math.sqrt(gX*gX + gY*gY + gZ*gZ);

        if (gForce > shakeThresholdGravity) {
            final long now = System.currentTimeMillis();

            if (mShakeTimestamp + shakeSlopTimeMs > now) {
                return;
            }

            //after x sec of no shakes reset counter
            if (mShakeTimestamp + shakeCountResetTimeMs < now) {
                mShakeCounter = 0;
            }

            mShakeTimestamp = now;
            mShakeCounter++;

            mListener.onShake(mShakeCounter);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //ignored
    }

    public float getShakeThresholdGravity() {
        return shakeThresholdGravity;
    }

    public void setShakeThresholdGravity(float shakeThresholdGravity) {
        this.shakeThresholdGravity = shakeThresholdGravity;
    }

    public int getShakeSlopTimeMs() {
        return shakeSlopTimeMs;
    }

    public void setShakeSlopTimeMs(int shakeSlopTimeMs) {
        this.shakeSlopTimeMs = shakeSlopTimeMs;
    }

    public int getShakeCountResetTimeMs() {
        return shakeCountResetTimeMs;
    }

    public void setShakeCountResetTimeMs(int shakeCountResetTimeMs) {
        this.shakeCountResetTimeMs = shakeCountResetTimeMs;
    }
}
