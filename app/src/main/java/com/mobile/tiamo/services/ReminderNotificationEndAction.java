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
import com.mobile.tiamo.activities.RequestExtraTimeActivity;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.NotificationMessages;

public class ReminderNotificationEndAction extends BroadcastReceiver {
    private String notificationText = "Liberzy";
    private String notificationTitle = "It is about to end";


    // Config this receive for open an activity.
    @Override
    public void onReceive(Context context, Intent intent) {
        int notiId = intent.getIntExtra("notiId",0);
        String title = intent.getStringExtra("title");
        String content = title.equals("Working") ? NotificationMessages.WORKING_END_MESSAGE : title+" is about to end, have you finished yet?";
        long uid = intent.getIntExtra("uid",-1);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);
            //Configure Notification Channel
            notificationChannel.setDescription("Liberzy End Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent yesReceive = new Intent(context, NotificationActionBroadcastReceiver.class);
        yesReceive.setAction("YES_ACTION");
        yesReceive.putExtra("uid",uid);
        yesReceive.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(context,notiId, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent notifyIntent = new Intent(context, RequestExtraTimeActivity.class);
        // Set the Activity to start in a new, empty task
        notifyIntent.putExtra("uid",uid);
        notifyIntent.putExtra("title",title);
        yesReceive.putExtra("notiId",notiId);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context,0,notifyIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.liberzy_logo4))
                .setContentTitle(notificationText + ":" +title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(content)
                .setContentIntent(notifyPendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(R.drawable.icon_notification_yes,"Yes",pendingIntentYes)
                .addAction(R.drawable.icon_notification_dislike, "No", notifyPendingIntent);
        notificationManager.notify(Messages.ID_NOTIFICATION_WITH_ACTION, notificationBuilder.build());
    }
}
