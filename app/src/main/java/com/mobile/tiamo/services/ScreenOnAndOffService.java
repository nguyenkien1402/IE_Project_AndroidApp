package com.mobile.tiamo.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.mobile.tiamo.R;
import com.mobile.tiamo.activities.StopSleepTrackingActivity;
import com.mobile.tiamo.utilities.Messages;

import org.threeten.bp.LocalTime;

public class ScreenOnAndOffService extends Service {

    BroadcastReceiver mReceiver = null;
    String sleepingTime, wakingupTime;
    Context context;

    @Override
    public void onCreate() {
        AndroidThreeTen.init(getApplication());
        super.onCreate();
        context = this;
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver,filter);
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(Messages.ID_NOTIFICATION_WITHOUT_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean screenOn = false;

        try{
            // Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java )
            screenOn = intent.getBooleanExtra("screen_state", false);

        }catch(Exception e){}
        sleepingTime = intent.getStringExtra("SleepingTime");
        wakingupTime = intent.getStringExtra("WakingupTime");
        LocalTime now = LocalTime.now();
        LocalTime sleep = LocalTime.parse(sleepingTime);

        if (!screenOn) {
            Log.d("TAG","Screen One");
        } else {
            if(now.compareTo(sleep)==1){
            }
            Log.d("TAG","Screen Off");
        }
        StartForground();
        if(intent.getAction().equals("STOPSERVICE")){
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    private void StartForground(){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);
            //Configure Notification Channel
            notificationChannel.setDescription("Tiamo Start Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent stopingService = new Intent(this, StopSleepTrackingActivity.class);
        PendingIntent pendingStop = PendingIntent.getActivity(this,0,stopingService,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Sleeping Time " + sleepingTime)
                .setAutoCancel(true)
                .setOngoing(false)
                .setVibrate(null)
                .setSound(null)
                .setContentIntent(pendingStop)
                .setContentText("Have a good night")
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);
        startForeground(101,  notificationBuilder.build());
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
