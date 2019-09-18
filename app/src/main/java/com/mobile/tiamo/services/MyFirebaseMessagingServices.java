package com.mobile.tiamo.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mobile.tiamo.R;
import com.mobile.tiamo.activities.NotificationActivity;
import com.mobile.tiamo.utilities.Config;


import java.util.Map;


public class MyFirebaseMessagingServices extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMS";
    private String message = "";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData() != null){
            getDateForNotification(remoteMessage);
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG,"Token: "+s);
    }

    private void sendNotification(){

        Log.d(TAG,"Send notification");

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent , 0);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            notificationChannel.setDescription("G");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.liberzy_logo4)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Config.content))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.liberzy_logo4))
                    .setContentTitle(Config.title)
                    .setAutoCancel(true)
                    .setSound(defaultSound)
                    .setContentText(Config.content)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_MAX);

            notificationManager.notify(1, notificationBuilder.build());
    }

    private void getDateForNotification(final RemoteMessage remoteMessage) {
        Log.d("TAG","Try");
        try{
            Map<String, String> data = remoteMessage.getData();
            Config.title = data.get("title");
            Config.content = data.get("content");
            Config.imageUrl = data.get("imageUrl");
            Config.gameUrl = data.get("gameUrl");
            sendNotification();
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

    }
}
