package com.mobile.tiamo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.mobile.tiamo.fragments.DashboardFragment;
import com.mobile.tiamo.fragments.DashboardViewPagerFragment;
import com.mobile.tiamo.fragments.HomeFragment;
import com.mobile.tiamo.fragments.SettingFragment;
import com.mobile.tiamo.fragments.TestNotificationFragment;
import com.mobile.tiamo.questionaires.SecondQuestionFragment;
import com.mobile.tiamo.services.ScreenOnAndOffService;
import com.mobile.tiamo.services.SleepingNotificationBeforeTimeReceiver;
import com.mobile.tiamo.services.UpdateDatabaseToServer;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.SavingDataSharePreference;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.TemporalField;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private Toolbar toolbar;
    public static TextView textToolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_dashboard:
                    fragment = new DashboardViewPagerFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new SettingFragment();
                    break;
//                case R.id.test_notifications:
//                    fragment = new TestNotificationFragment();
//                    break;
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidThreeTen.init(this);
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar =  (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        textToolbar = (TextView) findViewById(R.id.toolbar_title);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new HomeFragment()).commit();


        testSomething();

        // Run service in the mid-night.
        // Service to inform saving the data. Let's do this one first
//        serviceUpdateDatabase();
        // Service to inform tracking sleep
//        startSleepTrackingService();

    }

    private void testSomething(){
        try {
            String today = DateUtilities.getCurrentDateInString();
            Log.d("Test",today + ":"+DateUtilities.getDayInAbbBySelectedDate(today));
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = dateFormat.parse(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR,-9);
            Date datebefore = calendar.getTime();
            String dateBeforeStr = dateFormat.format(datebefore);
            Log.d("Test",dateBeforeStr +":"+DateUtilities.getDayInAbbBySelectedDate(dateBeforeStr));
        }catch (Exception e){
            Log.d("TAG",e.getMessage());
        }
    }

    /*
     * Service for updating database at the midnight
     * This service will be run at 23:59 every night
     */
    private void serviceUpdateDatabase(){
        // First, get the data
        Intent service = new Intent(this, UpdateDatabaseToServer.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(this,1,service,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 55);
        calendar.set(Calendar.SECOND, 00);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    /*
     * Service for tracking sleep.
     * This service will be run at the time people set for sleeping
     */
    private void startSleepTrackingService(){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        // Get the current time sleeping time first
        String sleepingTime = SavingDataSharePreference.getDataString(this, Messages.LOCAL_DATA,Messages.SLEEPING_TIME);
        LocalTime time = LocalTime.parse(sleepingTime);
        LocalTime timeBefore = time.minusMinutes(30);
        // Send notification 30 minute before sleeping time.
        Intent intentBefore = new Intent(this, SleepingNotificationBeforeTimeReceiver.class);
        PendingIntent piSleepBefore = PendingIntent.getBroadcast(this,2,intentBefore,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendarSleepBefore = Calendar.getInstance();
        calendarSleepBefore.set(Calendar.HOUR_OF_DAY, timeBefore.getHour());
        calendarSleepBefore.set(Calendar.MINUTE, timeBefore.getMinute());
        calendarSleepBefore.set(Calendar.SECOND, 00);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendarSleepBefore.getTimeInMillis(), AlarmManager.INTERVAL_DAY, piSleepBefore);

        // Send notification and start tracking sleep at sleeping time
        int hour = LocalTime.parse(sleepingTime).getHour();
        int minute = LocalTime.parse(sleepingTime).getMinute();
        Intent trackingSleeping = new Intent(this, ScreenOnAndOffService.class);
        trackingSleeping.putExtra("SleepingTime",sleepingTime);
        trackingSleeping.putExtra("WakingupTime",SavingDataSharePreference.getDataString(this,Messages.LOCAL_DATA,Messages.WAKINGUP_TIME));

        PendingIntent piTracking = PendingIntent.getService(this,3,trackingSleeping,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar calendarSleep = Calendar.getInstance();
        calendarSleep.set(Calendar.HOUR_OF_DAY, hour);
        calendarSleep.set(Calendar.MINUTE, minute);
        calendarSleep.set(Calendar.SECOND,00);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendarSleep.getTimeInMillis(), AlarmManager.INTERVAL_DAY, piTracking);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
