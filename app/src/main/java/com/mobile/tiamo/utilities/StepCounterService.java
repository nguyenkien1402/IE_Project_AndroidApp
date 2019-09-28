package com.mobile.tiamo.utilities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.StepsTakenModel;
import com.mobile.tiamo.dao.TiamoDatabase;

public class StepCounterService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mStepDetectorSensor;
    private TiamoDatabase db;
    private int numSteps;

    @Override
    public void onCreate() {
        super.onCreate();
        numSteps = 0;
        mSensorManager = (SensorManager)
                this.getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) != null)
        {
            mStepDetectorSensor =
                    mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
            db = SQLiteDatabase.getTiamoDatabase(this);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        numSteps = numSteps +1;
        if(numSteps % 100 ==0){
            String currentDate = DateUtilities.getCurrentDateInString();
            if(db.stepsTakenDao().getStepTakenByDate(currentDate) != null){
                StepsTakenModel model = db.stepsTakenDao().getStepTakenByDate(currentDate);
                model.setSteps(model.getSteps() + numSteps);
                db.stepsTakenDao().update(model);
            }else{
                numSteps = 0;
                StepsTakenModel model = new StepsTakenModel();
                model.setDate(currentDate);
                model.setSteps(50);
                db.stepsTakenDao().insert(model);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
