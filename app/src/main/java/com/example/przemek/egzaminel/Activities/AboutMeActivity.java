package com.example.przemek.egzaminel.Activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.przemek.egzaminel.Interfaces.OnShakeListener;
import com.example.przemek.egzaminel.R;
import com.example.przemek.egzaminel.Tools.ShakeDetector;
import com.example.przemek.egzaminel.Tools.Tools;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutMeActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        initShakeDetector();
        ButterKnife.bind(this);
    }

    @OnClick(R.id.about_me_email)
    public void onEmailClick() {
        Tools.openEmailIntent(getApplicationContext(), R.string.about_me_email);
    }


    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    private void initShakeDetector() {

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setShakeSlopTimeMs(2000);
        mShakeDetector.setOnShakeListener(new OnShakeListener() {
            @Override
            public void onShake(int count) {
                Tools.makeLongToast(getApplicationContext(), getString(R.string.about_me_shaking));
            }
        });
    }
}
