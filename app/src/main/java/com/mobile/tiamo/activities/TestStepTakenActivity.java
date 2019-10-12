package com.mobile.tiamo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.mobile.tiamo.R;
import com.mobile.tiamo.services.StepCounterService;

public class TestStepTakenActivity extends AppCompatActivity{

    private boolean isSensorPresent = false;
    private TextView mStepsSinceReboot;
    private int numStep = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_step_taken);
        mStepsSinceReboot = findViewById(R.id.steptaken);

        Intent service = new Intent(this, StepCounterService.class);
        startService(service);

    }




}
