package com.mobile.tiamo.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.StepsTakenModel;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.fragments.DashboardViewStepCounterFragment;
import com.mobile.tiamo.utilities.DateUtilities;

public class StepCounterService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor countSensor;
    private TiamoDatabase db;
    private int numSteps;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        numSteps = 0;
        Log.d("STEPTAKEN","Init Date");
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            mSensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);
        }else{

        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("STEP",(int)event.values[0]+"");
//        numSteps = numSteps +1;
//        Log.d("STEPTAKEN",numSteps+"");
//        DashboardViewStepCounterFragment.stepsTaken = numSteps;
//        if(numSteps % 100 ==0){
//            String currentDate = DateUtilities.getCurrentDateInString();
//            if(db.stepsTakenDao().getStepTakenByDate(currentDate) != null){
//                StepsTakenModel model = db.stepsTakenDao().getStepTakenByDate(currentDate);
//                model.setSteps(model.getSteps() + numSteps);
//                db.stepsTakenDao().update(model);
//            }else{
//                numSteps = 0;
//                StepsTakenModel model = new StepsTakenModel();
//                model.setDate(currentDate);
//                model.setSteps(50);
//                db.stepsTakenDao().insert(model);
//            }
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
