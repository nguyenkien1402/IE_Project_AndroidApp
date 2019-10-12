package com.mobile.tiamo.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.StepsTakenModel;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.fragments.DashboardViewStepCounterFragment;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.SavingDataSharePreference;

import java.util.Date;

public class StepsCounterService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mStepDetectorSensor;
    private int numSteps = 0;
    private TiamoDatabase db;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        db = SQLiteDatabase.getTiamoDatabase(this);
        mSensorManager = (SensorManager)
                this.getSystemService(Context.SENSOR_SERVICE);
        mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(mStepDetectorSensor != null){
            mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        numSteps++;
        Log.d("STEPTAKEN",numSteps+"");
        if(numSteps % 10 ==0){
            if(SavingDataSharePreference.getDataInt(this,Messages.LOCAL_DATA_STEP, DateUtilities.getCurrentDateInString()) != -1){
                int current = SavingDataSharePreference.getDataInt(this,Messages.LOCAL_DATA_STEP, DateUtilities.getCurrentDateInString());
                current = current + numSteps;
                SavingDataSharePreference.savingLocalData(this, Messages.LOCAL_DATA_STEP, DateUtilities.getCurrentDateInString(),current);
            }else{
                SavingDataSharePreference.savingLocalData(this, Messages.LOCAL_DATA_STEP, DateUtilities.getCurrentDateInString(),numSteps);
            }
            String currentDate = DateUtilities.getCurrentDateInString();
            if(db.stepsTakenDao().getStepTakenByDate(currentDate) != null){
                StepsTakenModel model = db.stepsTakenDao().getStepTakenByDate(currentDate);
                model.setSteps(model.getSteps() + numSteps);
                db.stepsTakenDao().update(model);
            }else{
                StepsTakenModel model = new StepsTakenModel();
                model.setDate(currentDate);
                model.setSteps(10);
                db.stepsTakenDao().insert(model);
            }
            numSteps = 0;
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
