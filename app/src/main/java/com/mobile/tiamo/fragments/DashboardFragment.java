package com.mobile.tiamo.fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.mobile.tiamo.R;
import com.mobile.tiamo.activities.AddingRoutineActivity;
import com.mobile.tiamo.adapters.ActivityModelItem;
import com.mobile.tiamo.adapters.DailyRoutineItem;
import com.mobile.tiamo.adapters.DashboardActivityAdapter;
import com.mobile.tiamo.dao.ActivitiesModel;
import com.mobile.tiamo.dao.DailyRoutine;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.Schedule;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.services.NotificationActionBroadcastReceiver;
import com.mobile.tiamo.services.ReminderNotificationEndAction;
import com.mobile.tiamo.services.ReminderNotificationStart;
import com.mobile.tiamo.utilities.DateUtilities;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardFragment extends Fragment {

    View view;
    List<DailyRoutine> list;
    TiamoDatabase db;
    Intent intentStart, intentEnd;
    List<ActivityModelItem> activityModelItems = null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container,false);

        db = SQLiteDatabase.getTiamoDatabase(getContext());
        ListView dListView = (ListView) view.findViewById(R.id.listView_dashboard);

        progressbar();

        DashboardActivityAdapter dashboardAdapter = new DashboardActivityAdapter(getContext(), R.layout.adapter_dashboard, activityModelItems);
        dListView.setAdapter(dashboardAdapter);

        GetListDailyActivityAsync getListDailyActivityAsync = new GetListDailyActivityAsync();
        getListDailyActivityAsync.execute();
/*        Button btn = (Button) view.findViewById(R.id.btnScheduleNotification);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                multiplyAlerts();
                testAction();
            }
        });*/
        return view;
    }

    public void progressbar(){
        RoundCornerProgressBar progress1 = view.findViewById(R.id.dashboard_progress);
        progress1.setProgressColor(Color.parseColor("#ed3b27"));
        progress1.setProgressBackgroundColor(Color.parseColor("#808080"));
        progress1.setMax(70);
        progress1.setProgress(30);

        /*IconRoundCornerProgressBar progress2 =  (IconRoundCornerProgressBar)view.findViewById(R.id.progress_2);
        progress2.setIconImageResource(R.drawable.adding_ed_time_64);
        progress2.setProgressColor(Color.parseColor("#ed3b27"));
        progress2.setProgressBackgroundColor(Color.parseColor("#808080"));
        progress2.setIconBackgroundColor(Color.parseColor("#ed3b27"));
        progress2.setMax(70);
        progress2.setProgress(30);*/
    }

    public void getActivityList(){
        activityModelItems = new ArrayList<ActivityModelItem>();
        // get Activity
        if(db.activitiesModelDao().getAll().size() > 0) {
            List<ActivitiesModel> scheduleList = db.activitiesModelDao().getAll();
            for (int i = 0; i < scheduleList.size(); i++) {
                ActivityModelItem model = new ActivityModelItem();
                model.setUid(scheduleList.get(i).getUid());
                model.setTitle(scheduleList.get(i).getTitle());
                model.setHours(scheduleList.get(i).getHours());
                model.setMinutes(scheduleList.get(i).getMinutes());
                activityModelItems.add(model);
            }
        }
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

    private class GetListDailyActivityAsync extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            getActivityList();
            /*String currentDate = DateUtilities.getCurrentDateInString();
            list = db.dailyActivitiesDao().getDailyActivities(currentDate);
            return list;*/
            return null;
        }

        /*@Override
        protected void onPostExecute(List<DailyRoutine> result) {
            super.onPostExecute(result);
            if(result.size() > 0){
              //  scheduleNotification(result);
            }else{

            }
        }*/

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
                // Config for starting ( code = 1000 + i )
                intentStart.putExtra("notiId",1000+i+1);
                intentStart.putExtra("task", dailyRoutine.getTitle());
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

                // Config for ending (code = 2000+i)
                intentEnd.putExtra("notiId",2000+i+1);
                intentEnd.putExtra("task", dailyRoutine.getTitle());
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
