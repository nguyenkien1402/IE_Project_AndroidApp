package com.mobile.tiamo.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.mobile.tiamo.R;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.NotificationMessages;

public class ReminderNotificationStart extends BroadcastReceiver {
    private String notificationText = "Liberzy";
    private String notificationTitle = "Do you miss me?";

    @Override
    public void onReceive(Context context, Intent intent) {
        int notiId = intent.getIntExtra("notiId",-1);
        String title = intent.getStringExtra("title");
        int uid = intent.getIntExtra("uid",-1);
        String content = title.equals("Working") ? NotificationMessages.WORKING_START_MESSAGE : "Don't miss your time to start";
//        title = title.equals("Working") ? title + " time starts soon" : title;
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);
            //Configure Notification Channel
            notificationChannel.setDescription("Liberzy Start Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.liberzy_logo4))
                .setContentTitle(notificationText + ":" + title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(content)
//                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);
        notificationManager.notify(Messages.ID_NOTIFICATION_WITHOUT_ACTION, notificationBuilder.build());
    }
}
