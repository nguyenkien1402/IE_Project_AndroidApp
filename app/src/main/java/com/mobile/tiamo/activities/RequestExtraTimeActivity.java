package com.mobile.tiamo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.mobile.tiamo.R;
import com.mobile.tiamo.dao.DailyRoutine;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.services.ReminderNotificationEndAction;
import com.mobile.tiamo.services.ReminderNotificationStart;
import com.mobile.tiamo.utilities.DateUtilities;

import org.threeten.bp.LocalTime;

import java.util.Calendar;

public class RequestExtraTimeActivity extends AppCompatActivity {

    private Button btnSave;
    private TimePicker timePicker;
    private long uid;
    private String title;
    private TiamoDatabase db;
    private int notiId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_extra_time);
        btnSave = findViewById(R.id.btnSave_extra);
        timePicker = findViewById(R.id.timePicker_extratime);
        db = SQLiteDatabase.getTiamoDatabase(this);

        uid = getIntent().getLongExtra("uid",-1);
        title = getIntent().getStringExtra("title");
        notiId = getIntent().getIntExtra("notiId",-1);
        //Cancel ID
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(2);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save information and finish the activity
                ScheduleNewNotificationAsync scheduleNewNotificationAsync = new ScheduleNewNotificationAsync();
                scheduleNewNotificationAsync.execute();

            }
        });
    }

    private class ScheduleNewNotificationAsync extends AsyncTask<Void, Void, DailyRoutine>{
        @Override
        protected DailyRoutine doInBackground(Void... voids) {
            // get the data first
            DailyRoutine dailyRoutine = db.dailyActivitiesDao().getDailyActivityById(uid);
            scheduleNewNotification(dailyRoutine);
            return dailyRoutine;
        }

        @Override
        protected void onPostExecute(DailyRoutine aVoid) {
            super.onPostExecute(aVoid);
            if(aVoid != null){
                finish();
            }
        }
    }

    private void scheduleNewNotification(DailyRoutine dailyRoutine){
        final AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intentEnd   = new Intent(this, ReminderNotificationEndAction.class);
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        LocalTime timeToEnd = LocalTime.of(hour,minute);
//        timeToEnd = timeToEnd.minusMinutes(5);
        Log.d("TAG","hour: "+timeToEnd.getHour()+" minute:"+timeToEnd.getMinute());

        String currentDate = DateUtilities.getCurrentDateInString();
        int year = Integer.parseInt(currentDate.split("-")[2]);
        int day = Integer.parseInt(currentDate.split("-")[0]);
        int month = Integer.parseInt(currentDate.split("-")[1]);

        // Config for ending (code = 2000+i)
        intentEnd.putExtra("notiId",notiId);
        intentEnd.putExtra("title", "Notification");
        intentEnd.putExtra("uid",uid);
        Calendar e_calendar = Calendar.getInstance();
        e_calendar.set(Calendar.MONTH, month-1); // month = month - 1
        e_calendar.set(Calendar.YEAR, year);
        e_calendar.set(Calendar.DAY_OF_MONTH, day);
        e_calendar.set(Calendar.HOUR_OF_DAY, timeToEnd.getHour());
        e_calendar.set(Calendar.MINUTE, timeToEnd.getMinute());
        e_calendar.set(Calendar.SECOND,00);
        PendingIntent pendingIntentE = PendingIntent.getBroadcast(this, notiId-1, intentEnd, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, e_calendar.getTimeInMillis(), pendingIntentE);
    }
}
