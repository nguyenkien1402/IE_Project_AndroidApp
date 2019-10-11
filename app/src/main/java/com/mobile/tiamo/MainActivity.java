package com.mobile.tiamo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.mobile.tiamo.fragments.DashboardViewPagerFragment;
import com.mobile.tiamo.fragments.HomeFragment;
import com.mobile.tiamo.fragments.SettingFragment;
import com.mobile.tiamo.services.ScreenOnAndOffService;
import com.mobile.tiamo.services.SleepingNotificationBeforeTimeReceiver;
import com.mobile.tiamo.services.UpdateDatabaseToServer;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.SavingDataSharePreference;
import com.mobile.tiamo.services.StepCounterService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.threeten.bp.LocalTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    public static TextView textToolbar;

//    private StepDetector simpleStepDetector;
//    private SensorManager sensorManager;
//    private Sensor accel;
    private int numSteps = 0;
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


//        testSomething();

//        Intent mStepsIntent = new Intent(getApplicationContext(), StepCounterService.class);
//        startService(mStepsIntent);

        // Run service check the step
//        runStepCounterService();

        // Run service in the mid-night.
        // Service to inform saving the data. Let's do this one first
//        serviceUpdateDatabase();

        // Service to inform tracking sleep
//        startSleepTrackingService();

    }

//    private void testSomething(){
//        try {
//            String today = DateUtilities.getCurrentDateInString();
//            Log.d("Test",today + ":"+DateUtilities.getDayInAbbBySelectedDate(today));
//            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//            Date currentDate = dateFormat.parse(today);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(currentDate);
//            calendar.add(Calendar.DAY_OF_YEAR,-9);
//            Date datebefore = calendar.getTime();
//            String dateBeforeStr = dateFormat.format(datebefore);
//            Log.d("Test",dateBeforeStr +":"+DateUtilities.getDayInAbbBySelectedDate(dateBeforeStr));
//        }catch (Exception e){
//            Log.d("TAG",e.getMessage());
//        }
//    }

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

//    private void runStepCounterService(){
//        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        simpleStepDetector = new StepDetector();
//        simpleStepDetector.registerListener(this);
//
//        sensorManager.registerListener(MainActivity.this,accel, SensorManager.SENSOR_DELAY_FASTEST);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//            simpleStepDetector.updateAccel(
//                    event.timestamp, event.values[0], event.values[1], event.values[2]
//            );
//        }
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//
//    @Override
//    public void step(long timeNs) {
//        numSteps++;
////        if(numSteps %100 == 0){
////            int currentStep = SavingDataSharePreference.getDataInt(this,Messages.LOCAL_DATA_STEP,DateUtilities.getCurrentDateInString());
////            currentStep = currentStep + numSteps;
////            SavingDataSharePreference.savingLocalData(this,Messages.LOCAL_DATA_STEP,DateUtilities.getCurrentDateInString(),currentStep);
////        }
//
//    }
}
