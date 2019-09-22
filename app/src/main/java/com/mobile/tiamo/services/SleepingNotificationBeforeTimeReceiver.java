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
import com.mobile.tiamo.utilities.SavingDataSharePreference;

import org.threeten.bp.LocalTime;

public class SleepingNotificationBeforeTimeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String sleepingTime = SavingDataSharePreference.getDataString(context,Messages.LOCAL_DATA, Messages.SLEEPING_TIME);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.liberzy_logo4))
                .setContentTitle("Sleeping Time: "+sleepingTime)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(NotificationMessages.BEFORE_SLEEPING)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);
        notificationManager.notify(Messages.ID_NOTIFICATION_WITHOUT_ACTION, notificationBuilder.build());
    }
}
