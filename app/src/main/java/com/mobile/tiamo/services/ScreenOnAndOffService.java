package com.mobile.tiamo.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalTime;

public class ScreenOnAndOffService extends Service {

    BroadcastReceiver mReceiver = null;
    String sleepingTime, wakingupTime;

    @Override
    public void onCreate() {
        AndroidThreeTen.init(getApplication());
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver,filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean screenOn = false;

        try{
            // Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java )
            screenOn = intent.getBooleanExtra("screen_state", false);

        }catch(Exception e){}
        sleepingTime = intent.getStringExtra("sleepingTime");
        wakingupTime = intent.getStringExtra("wakingupTime");
        LocalTime now = LocalTime.now();
        LocalTime sleep = LocalTime.parse(sleepingTime);
        LocalTime waking = LocalTime.parse(wakingupTime);
        if (!screenOn) {

            Log.d("TAG","Screen One");
        } else {
            if(now.compareTo(sleep)==1){
                // Start recording sleeping time

            }
            Log.d("TAG","Screen Off");
        }
        return startId;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("ScreenOnOff", "Service destroy");
        if(mReceiver!=null)
            unregisterReceiver(mReceiver);
    }
}
