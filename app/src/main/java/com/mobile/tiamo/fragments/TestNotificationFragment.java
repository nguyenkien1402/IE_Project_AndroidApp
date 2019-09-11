package com.mobile.tiamo.fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.mobile.tiamo.R;
import com.mobile.tiamo.activities.AddingRoutineActivity;
import com.mobile.tiamo.activities.RequestExtraTimeActivity;
import com.mobile.tiamo.dao.DailyRoutine;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.services.NotificationActionBroadcastReceiver;
import com.mobile.tiamo.services.ReminderNotificationEndAction;
import com.mobile.tiamo.services.ReminderNotificationStart;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.NotificationMessages;

import org.threeten.bp.LocalTime;

import java.util.Calendar;
import java.util.List;

public class TestNotificationFragment extends Fragment {

    View view;
    List<DailyRoutine> list;
    TiamoDatabase db;
    Intent intentStart, intentEnd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AndroidThreeTen.init(getActivity());
        view = inflater.inflate(R.layout.fragment_dashboard, container,false);
        db = SQLiteDatabase.getTiamoDatabase(getContext());

        Button btn = (Button) view.findViewById(R.id.btnScheduleNotification);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testActionEnd();
            }
        });

        return view;
    }

    public void testActionEnd(){
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);
            //Configure Notification Channel
            notificationChannel.setDescription("Tiamo End Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent notifyIntent = new Intent(getActivity().getApplicationContext(), RequestExtraTimeActivity.class);
        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(),
                0,notifyIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity().getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Working")
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(NotificationMessages.WORKING_END_MESSAGE)
                .setContentIntent(notifyPendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(R.drawable.icon_notification_yes,"Yes",null)
                .addAction(R.drawable.icon_notification_dislike, "No", notifyPendingIntent);
        notificationManager.notify(Messages.ID_NOTIFICATION_WITH_ACTION, notificationBuilder.build());
    }

    public void testAction(){
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(getActivity().getApplicationContext(), AddingRoutineActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext().getApplicationContext(), 0, intent,0);

        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);
            //Configure Notification Channel
            notificationChannel.setDescription("Game Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent yesReceive = new Intent(getActivity().getApplicationContext(), NotificationActionBroadcastReceiver.class);
        yesReceive.setAction("YES_ACTION");
        yesReceive.putExtra("uid",2);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(getActivity().getApplicationContext(),0, yesReceive, PendingIntent.FLAG_CANCEL_CURRENT);
//
        Intent noReceive = new Intent(getActivity().getApplicationContext(), NotificationActionBroadcastReceiver.class);
        noReceive.setAction("NO_ACTION");
        noReceive.putExtra("uid",2);
        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(getActivity().getApplicationContext(),0, noReceive, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Test notification")
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText("This mean content to test notification")
                .setContentIntent(pendingIntent)
                .addAction(R.mipmap.ic_launcher,"Yes",pendingIntentYes)
                .addAction(R.mipmap.ic_launcher, "No", pendingIntentNo)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);


        notificationManager.notify(2, notificationBuilder.build());
    }
    public void multiplyAlerts(){
        final AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity().getApplicationContext(), ReminderNotificationStart.class);
        for(int i = 0; i < 3; i++){
            intent.putExtra("data",i+1);
            Calendar t_calendar = Calendar.getInstance();
            t_calendar.set(Calendar.MONTH, Calendar.AUGUST);
            t_calendar.set(Calendar.YEAR, 2019);
            t_calendar.set(Calendar.DAY_OF_MONTH, 30);
            t_calendar.set(Calendar.HOUR_OF_DAY, 17);
            t_calendar.set(Calendar.MINUTE, 25+ i);
            t_calendar.set(Calendar.SECOND, 00);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), i, intent, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.set(AlarmManager.RTC_WAKEUP, t_calendar.getTimeInMillis(),pendingIntent);
            Log.d("MainActivity","Have Alarm manager");
        }
    }

    private class GetListDailyActivityAsync extends AsyncTask<Void, Void, List<DailyRoutine>> {
        @Override
        protected List<DailyRoutine> doInBackground(Void... voids) {
            String currentDate = DateUtilities.getCurrentDateInString();
            list = db.dailyActivitiesDao().getDailyActivities(currentDate);
            return list;
        }

        @Override
        protected void onPostExecute(List<DailyRoutine> result) {
            super.onPostExecute(result);
            if(result.size() > 0){
                scheduleNotification(result);
            }else{

            }
        }

        private void scheduleNotification(List<DailyRoutine> result) {
            final AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            intentStart = new Intent(getActivity().getApplicationContext(), ReminderNotificationStart.class);
            intentEnd   = new Intent(getActivity().getApplicationContext(), ReminderNotificationEndAction.class);
            for(int i = 0 ; i < result.size() ; i++){
                DailyRoutine dailyRoutine = result.get(i);
                String timeStart = dailyRoutine.getTimeStart();
                String timeEnd = dailyRoutine.getTimeEnd();
                String currentDate = DateUtilities.getCurrentDateInString();

                int year = Integer.parseInt(currentDate.split("-")[2]);
                int day = Integer.parseInt(currentDate.split("-")[0]);
                int month = Integer.parseInt(currentDate.split("-")[1]);

                // Easy to send them notification based on the exact time
                int hourStart = Integer.parseInt(timeStart.split(":")[0]);
                int minuteStart = Integer.parseInt(timeStart.split(":")[1]);
                int hourEnd = Integer.parseInt(timeEnd.split(":")[0]);
                int minuteEnd = Integer.parseInt(timeEnd.split(":")[1]);

                // check if it is already pass the time start.
                LocalTime now = LocalTime.now();
                LocalTime time = LocalTime.of(hourStart, minuteStart);
                time = time.plusMinutes(5);

                // if now.compareTo(time) == 1 mean the current time bigger than the starting time,
                // so, wont send notification anymore
                if(now.compareTo(time) != 1){
                    // Mean, the time now does not pass the starting time for 15 minutes
                    // Send notification for starting purpose
                    // Config for starting( code = 1000 + i )
                    intentStart.putExtra("notiId",1000+i+1);
                    intentStart.putExtra("title", dailyRoutine.getTitle());
                    intentStart.putExtra("uid",result.get(i).getUid());
                    Calendar s_calendar = Calendar.getInstance();
                    s_calendar.set(Calendar.MONTH, month-1); // month = month - 1
                    s_calendar.set(Calendar.YEAR, year);
                    s_calendar.set(Calendar.DAY_OF_MONTH, day);
                    s_calendar.set(Calendar.HOUR_OF_DAY, hourStart);
                    s_calendar.set(Calendar.MINUTE, minuteStart);
                    s_calendar.set(Calendar.SECOND, 00);
                    PendingIntent pendingIntentS = PendingIntent.getBroadcast(getActivity(), 1000+i, intentStart, PendingIntent.FLAG_ONE_SHOT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, s_calendar.getTimeInMillis(),pendingIntentS);
                }

                // Of course, config for the ending time will be the same.
                LocalTime timeToEnd = LocalTime.of(hourStart, minuteStart);
                // Config for ending (code = 2000+i)
                if(now.compareTo(timeToEnd) != 1){
                    intentEnd.putExtra("notiId",2000+i+1);
                    intentEnd.putExtra("title", dailyRoutine.getTitle());
                    intentEnd.putExtra("uid",result.get(i).getUid());
                    Calendar e_calendar = Calendar.getInstance();
                    e_calendar.set(Calendar.MONTH, month-1); // month = month - 1
                    e_calendar.set(Calendar.YEAR, year);
                    e_calendar.set(Calendar.DAY_OF_MONTH, day);
                    e_calendar.set(Calendar.HOUR_OF_DAY, hourEnd);
                    e_calendar.set(Calendar.MINUTE, minuteEnd);
                    e_calendar.set(Calendar.SECOND,00);
                    PendingIntent pendingIntentE = PendingIntent.getBroadcast(getActivity(), 2000+i, intentEnd, PendingIntent.FLAG_ONE_SHOT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, e_calendar.getTimeInMillis(), pendingIntentE);
                }
            }
        }
    }
}