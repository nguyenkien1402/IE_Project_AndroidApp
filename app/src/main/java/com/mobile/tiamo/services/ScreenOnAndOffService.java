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
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.SleepingModel;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.SavingDataSharePreference;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalTime;

public class ScreenOnAndOffService extends Service {

    BroadcastReceiver mReceiver = null;
    String sleepingTime, wakingupTime;
    Context context;
    TiamoDatabase db;

    @Override
    public void onCreate() {
        AndroidThreeTen.init(getApplication());
        super.onCreate();
        context = this;
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(Messages.ID_NOTIFICATION_WITHOUT_ACTION);
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver,filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        db = SQLiteDatabase.getTiamoDatabase(context);

        boolean screenOn = false;
        String currentDate;
        try{
            // Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java )
            screenOn = intent.getBooleanExtra("screen_state", false);

        }catch(Exception e){}
        sleepingTime = intent.getStringExtra("SleepingTime");
        wakingupTime = intent.getStringExtra("WakingupTime");
        LocalTime sleep = LocalTime.parse(sleepingTime);
        LocalTime midnight = LocalTime.of(23,59);
        if(sleep.compareTo(midnight) == 1){
            // Mean, the sleeping time bigger than the midnight, so the current day will be from the system
            currentDate = DateUtilities.getCurrentDateInString();
        }else{
            // Mean, the sleeping time is before the midnight, so current day will be add to one.
            currentDate = DateUtilities.getTheNextDateInString();
        }

        if (!screenOn) {
            Log.d("TAG","Screen One");
            // If the last time record is less then 30 minutes, then update the last record with the new time
            // else if more than 30 minutes, save the new one.
            if(db.sleepingModelDao().getSleepingByDate(currentDate).size() > 0){
                SleepingModel sleepingModel = db.sleepingModelDao().getTheNewest(currentDate);
                LocalTime now = LocalTime.now();
                LocalTime previous = LocalTime.parse(sleepingTime);
                if(Duration.between(now,previous).toMinutes() >= 30){
                    SavingDataSharePreference.savingLocalData(context,Messages.LOCAL_DATA,"ISLEEPING",true);
                }else{
                    SavingDataSharePreference.savingLocalData(context,Messages.LOCAL_DATA,"ISLEEPING",false);
                }
            }

        } else {
            // Check if there are some records into SQL file
            // If there are already some record in the SQL
            if(db.sleepingModelDao().getSleepingByDate(currentDate).size() > 0){
                // mean, there are already some records about sleeping
                // if isSleeping == true, mean, need to save a new record
                // otherwise, get the previous record and save the new time
                boolean isSleeping = SavingDataSharePreference.getDataBoolean(context,Messages.LOCAL_DATA,"ISSLEEPING");
                if(isSleeping){
                    SleepingModel s = new SleepingModel();
                    s.setDate(currentDate);
                    s.setIsStorage(0);
                    s.setTime(LocalTime.now().getHour()+":"+LocalTime.now().getMinute());
                    db.sleepingModelDao().insert(s);
                }else{
                    SleepingModel s = db.sleepingModelDao().getTheNewest(currentDate);
                    s.setTime(LocalTime.now().getHour()+":"+LocalTime.now().getMinute());
                    db.sleepingModelDao().update(s);
                }
            }else{
                // mean, no records yet, so install then
                SleepingModel s = new SleepingModel();
                s.setDate(currentDate);
                s.setIsStorage(0);
                s.setTime(LocalTime.now().getHour()+":"+LocalTime.now().getMinute());
                db.sleepingModelDao().insert(s);

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
