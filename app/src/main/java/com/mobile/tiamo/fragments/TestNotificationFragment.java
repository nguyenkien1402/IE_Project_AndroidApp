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
import android.widget.Toast;

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
import com.mobile.tiamo.models.DailyActivity;
import com.mobile.tiamo.models.XbrainUser;
import com.mobile.tiamo.rest.services.IDailyActivity;
import com.mobile.tiamo.rest.services.IDailyRoutine;
import com.mobile.tiamo.rest.services.IXbrainUser;
import com.mobile.tiamo.rest.services.RetrofitService;
import com.mobile.tiamo.services.NotificationActionBroadcastReceiver;
import com.mobile.tiamo.services.ReminderNotificationEndAction;
import com.mobile.tiamo.services.ReminderNotificationStart;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.NotificationMessages;

import org.threeten.bp.LocalTime;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestNotificationFragment extends Fragment {

    private View view;
    private List<DailyRoutine> list;
    private TiamoDatabase db;
    private Intent intentStart, intentEnd;
    private Button btn, btn2, btn3, btn4, btn5, btn6;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AndroidThreeTen.init(getActivity());
        view = inflater.inflate(R.layout.fragment_test_schedule_notification, container,false);
        db = SQLiteDatabase.getTiamoDatabase(getContext());


        btn = (Button) view.findViewById(R.id.btnScheduleNotification);
        btn2 = (Button) view.findViewById(R.id.btnAddUser);
        btn3 = (Button) view.findViewById(R.id.btnGetUser);
        btn4 = (Button) view.findViewById(R.id.btnGetDailyActivity);
        btn5 = (Button) view.findViewById(R.id.btnInsertSingleObject);
        btn6 = (Button) view.findViewById(R.id.btnInsertMultipleObject);

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IXbrainUser iXbrainUser = RetrofitService.getRetrofitService().create(IXbrainUser.class);
                Call<List<XbrainUser>> call = iXbrainUser.getAllUser();
                call.enqueue(new Callback<List<XbrainUser>>() {
                    @Override
                    public void onResponse(Call<List<XbrainUser>> call, Response<List<XbrainUser>> response) {
                        Log.d("USER",response.body().get(0).getUserName());
                    }

                    @Override
                    public void onFailure(Call<List<XbrainUser>> call, Throwable t) {
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IXbrainUser iXbrainUser = RetrofitService.getRetrofitService().create(IXbrainUser.class);
                XbrainUser x = new XbrainUser();
                x.setDeviceId("jdg23oi093hfiu2394234");
                x.setEmail("kiennt@gmail.com");
                x.setFirstName("Q");
                x.setLastName("Y");
                x.setHassPassword("234h234923498230949234");
                x.setUserName("qs");
                Call<XbrainUser> call = iXbrainUser.register(x);
                call.enqueue(new Callback<XbrainUser>() {
                    @Override
                    public void onResponse(Call<XbrainUser> call, Response<XbrainUser> response) {
                        if(response.body() != null){
                            Log.d("User",response.body().getUserName());
                        }
                    }

                    @Override
                    public void onFailure(Call<XbrainUser> call, Throwable t) {

                    }
                });
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IDailyActivity iDailyActivity = RetrofitService.getRetrofitService().create(IDailyActivity.class);
                Call<List<DailyActivity>> call = iDailyActivity.getActivityByUserIdAndDate(1,"2019-09-13");
                call.enqueue(new Callback<List<DailyActivity>>() {
                    @Override
                    public void onResponse(Call<List<DailyActivity>> call, Response<List<DailyActivity>> response) {
                        Log.d("UserDailyActivity",response.body().get(0).getActivityContent());
                    }

                    @Override
                    public void onFailure(Call<List<DailyActivity>> call, Throwable t) {

                    }
                });
            }
        });

        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckingAndUpdatingDatabaseAsync update = new CheckingAndUpdatingDatabaseAsync();
                update.execute();
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testNotiActionEnd();
            }
        });

        return view;
    }
    private class CheckingAndUpdatingDatabaseAsync extends AsyncTask<Void, Void, List<DailyRoutine>> {
        @Override
        protected List<DailyRoutine> doInBackground(Void... voids) {
            String currentDay = DateUtilities.getCurrentDateInString();
            // Get all the data which doesn't have the flag update
            // Get the routine data first.
            List<DailyRoutine> dailyRoutines = db.dailyActivitiesDao().getDailyRoutineUnUpdate();
            return dailyRoutines;
        }

        @Override
        protected void onPostExecute(List<DailyRoutine> list) {
            super.onPostExecute(list);

            IDailyRoutine iDailyRoutine = RetrofitService.getRetrofitService().create(IDailyRoutine.class);

            String currentDay = DateUtilities.getCurrentDateInString();
            Log.d("TAG",list.size() + " - " +list.get(0).getTimeStart());

            for(int i = 0 ; i < list.size() ; i++){
                com.mobile.tiamo.dao.DailyRoutine d = list.get(i);
                com.mobile.tiamo.models.DailyRoutine da = new com.mobile.tiamo.models.DailyRoutine();
                da.setRoutineTitle(d.getTitle());
                da.setPlanStartTime(d.getTimeStart());
                da.setDateAchieve(DateUtilities.convertDateFormat(currentDay));
                da.setDayOperation(d.getDayOperation());
                da.setPlanEndTime(d.getTimeEnd());
                da.setActualStartTime(d.getTimeStart());
                da.setActualEndTime(d.getTimeActuallyEnd());
                Call<com.mobile.tiamo.models.DailyRoutine> call = iDailyRoutine.insert(1,da);
                call.enqueue(new Callback<com.mobile.tiamo.models.DailyRoutine>() {
                    @Override
                    public void onResponse(Call<com.mobile.tiamo.models.DailyRoutine> call, Response<com.mobile.tiamo.models.DailyRoutine> response) {
                        Log.d("TAG","Insert Successfully");
                    }
                    @Override
                    public void onFailure(Call<com.mobile.tiamo.models.DailyRoutine> call, Throwable t) {
                        Log.d("TAG","Insert Failed");
                    }
                });

            }

        }
    }

    public void testNotiActionEnd(){
//        int notiId = intent.getIntExtra("notiId",0);
//        String task = intent.getStringExtra("title");
//        long uid = intent.getIntExtra("uid",-1);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0);
        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
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

        Intent yesReceive = new Intent(getActivity().getApplicationContext(), NotificationActionBroadcastReceiver.class);
        yesReceive.setAction("YES_ACTION");
        yesReceive.putExtra("uid",100);
        yesReceive.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(getActivity().getApplicationContext(),
                1001, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent notifyIntent = new Intent(getActivity().getApplicationContext(), RequestExtraTimeActivity.class);
        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(),0,
                notifyIntent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity().getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("This is title")
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(NotificationMessages.WORKING_END_MESSAGE)
                .setContentIntent(notifyPendingIntent)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .addAction(R.drawable.icon_notification_yes,"Yes",pendingIntentYes)
                .addAction(R.drawable.icon_notification_dislike, "No", notifyPendingIntent);
        notificationManager.notify(Messages.ID_NOTIFICATION_WITH_ACTION, notificationBuilder.build());

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
                time = time.minusMinutes(5);

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
                    s_calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
                    s_calendar.set(Calendar.MINUTE, time.getMinute());
                    s_calendar.set(Calendar.SECOND, 00);
                    PendingIntent pendingIntentS = PendingIntent.getBroadcast(getActivity(), 1000+i, intentStart, PendingIntent.FLAG_ONE_SHOT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, s_calendar.getTimeInMillis(),pendingIntentS);
                }

                // Of course, config for the ending time will be the same.
                LocalTime timeToEnd = LocalTime.of(hourEnd, minuteEnd);
                timeToEnd = timeToEnd.minusMinutes(5);
                // Config for ending (code = 2000+i)
                if(now.compareTo(timeToEnd) != 1){
                    intentEnd.putExtra("notiId",2000+i+1);
                    intentEnd.putExtra("title", dailyRoutine.getTitle());
                    intentEnd.putExtra("uid",result.get(i).getUid());
                    Calendar e_calendar = Calendar.getInstance();
                    e_calendar.set(Calendar.MONTH, month-1); // month = month - 1
                    e_calendar.set(Calendar.YEAR, year);
                    e_calendar.set(Calendar.DAY_OF_MONTH, day);
                    e_calendar.set(Calendar.HOUR_OF_DAY, timeToEnd.getHour());
                    e_calendar.set(Calendar.MINUTE, timeToEnd.getMinute());
                    e_calendar.set(Calendar.SECOND,00);
                    PendingIntent pendingIntentE = PendingIntent.getBroadcast(getActivity(), 2000+i, intentEnd, PendingIntent.FLAG_ONE_SHOT);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, e_calendar.getTimeInMillis(), pendingIntentE);
                }
            }
        }
    }
}
