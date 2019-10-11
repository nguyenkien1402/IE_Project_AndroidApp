package com.mobile.tiamo.utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.mobile.tiamo.dao.DailyRoutine;
import com.mobile.tiamo.services.ReminderNotificationEndAction;
import com.mobile.tiamo.services.ReminderNotificationStart;

import org.threeten.bp.LocalTime;

import java.util.Calendar;
import java.util.List;

public class LocalNotifications {

    private Context context;
    private NotificationCompat.Builder notificationBuilder;

    public LocalNotifications(Context context){
        this.context = context;
    }

    public void schduleLocalNotificationByDate(String date){
    }

    public void scheduleNotification(List<DailyRoutine> result) {
        String currentDate = DateUtilities.getCurrentDateInString();
        if(SavingDataSharePreference.getDataBoolean(context,Messages.LOCAL_DATA,currentDate)==false){
            Log.d("Notification","Size of list:"+result.size()+"");
            Intent intentStart, intentEnd;
            final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            intentStart = new Intent(context.getApplicationContext(), ReminderNotificationStart.class);
            intentEnd   = new Intent(context.getApplicationContext(), ReminderNotificationEndAction.class);
            for(int i = 0 ; i < result.size() ; i++){
                DailyRoutine dailyRoutine = result.get(i);
                String timeStart = dailyRoutine.getTimeStart();
                String timeEnd = dailyRoutine.getTimeEnd();

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
//            time = time.minusMinutes(5);

                // if now.compareTo(time) == 1 mean the current time bigger than the starting time,
                // so, wont send notification anymore
                // Mean, the time now does not pass the starting time for 5 minutes
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
                PendingIntent pendingIntentS = PendingIntent.getBroadcast(context, 1000+i, intentStart, PendingIntent.FLAG_ONE_SHOT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, s_calendar.getTimeInMillis(),pendingIntentS);

                // Of course, config for the ending time will be the same.
                LocalTime timeToEnd = LocalTime.of(hourEnd, minuteEnd);
//            timeToEnd = timeToEnd.minusMinutes(5);
                // Config for ending (code = 2000+i)
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
                PendingIntent pendingIntentE = PendingIntent.getBroadcast(context, 2000+i, intentEnd, PendingIntent.FLAG_ONE_SHOT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, e_calendar.getTimeInMillis(), pendingIntentE);
            }
            SavingDataSharePreference.savingLocalData(context,Messages.LOCAL_DATA,currentDate,true);
        }else{
        }

    }


    public void scheduleNotificationForNewRoutine(DailyRoutine dailyRoutine) {
        String currentDate = DateUtilities.getCurrentDateInString();
        Intent intentStart, intentEnd;
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intentStart = new Intent(context.getApplicationContext(), ReminderNotificationStart.class);
        intentEnd   = new Intent(context.getApplicationContext(), ReminderNotificationEndAction.class);
        String timeStart = dailyRoutine.getTimeStart();
        String timeEnd = dailyRoutine.getTimeEnd();
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
         // if now.compareTo(time) == 1 mean the current time bigger than the starting time,
        // so, wont send notification anymore
        // Mean, the time now does not pass the starting time for 5 minutes
        // Send notification for starting purpose
        // Config for starting( code = 1000 + i )
        intentStart.putExtra("notiId",1000+1);
        intentStart.putExtra("title", dailyRoutine.getTitle());
        intentStart.putExtra("uid",dailyRoutine.getUid());
        Calendar s_calendar = Calendar.getInstance();
        s_calendar.set(Calendar.MONTH, month-1); // month = month - 1
        s_calendar.set(Calendar.YEAR, year);
        s_calendar.set(Calendar.DAY_OF_MONTH, day);
        s_calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
        s_calendar.set(Calendar.MINUTE, time.getMinute());
        s_calendar.set(Calendar.SECOND, 00);
        PendingIntent pendingIntentS = PendingIntent.getBroadcast(context, 1000, intentStart, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, s_calendar.getTimeInMillis(),pendingIntentS);
        // Of course, config for the ending time will be the same.
        LocalTime timeToEnd = LocalTime.of(hourEnd, minuteEnd);
//           timeToEnd = timeToEnd.minusMinutes(5);
                // Config for ending (code = 2000+i)
        intentEnd.putExtra("notiId",2000+1);
        intentEnd.putExtra("title", dailyRoutine.getTitle());
        intentEnd.putExtra("uid",dailyRoutine.getUid());
        Calendar e_calendar = Calendar.getInstance();
        e_calendar.set(Calendar.MONTH, month-1); // month = month - 1
        e_calendar.set(Calendar.YEAR, year);
        e_calendar.set(Calendar.DAY_OF_MONTH, day);
        e_calendar.set(Calendar.HOUR_OF_DAY, timeToEnd.getHour());
        e_calendar.set(Calendar.MINUTE, timeToEnd.getMinute());
        e_calendar.set(Calendar.SECOND,00);
        PendingIntent pendingIntentE = PendingIntent.getBroadcast(context, 2000, intentEnd, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, e_calendar.getTimeInMillis(), pendingIntentE);
    }

}
