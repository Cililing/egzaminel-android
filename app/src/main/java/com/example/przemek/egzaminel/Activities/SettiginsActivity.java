package com.example.przemek.egzaminel.Activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.przemek.egzaminel.Interfaces.OnShakeListener;
import com.example.przemek.egzaminel.R;
import com.example.przemek.egzaminel.Tools.ShakeDetector;
import com.example.przemek.egzaminel.Tools.Tools;

import butterknife.OnClick;

public class SettiginsActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settigins);

        initShakeDetector();
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

    @OnClick(R.id.settigins_email)
    public void onEmailClick() {
        Tools.openEmailIntent(getApplicationContext(), R.string.about_me_app_email);
    }

    private void initShakeDetector() {

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new OnShakeListener() {
            @Override
            public void onShake(int count) {

                //switch to activity about me
                Intent intent = new Intent(getApplicationContext(), AboutMeActivity.class);
                startActivity(intent);
            }
        });
    }
}
