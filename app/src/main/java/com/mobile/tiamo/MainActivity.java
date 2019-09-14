package com.mobile.tiamo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mobile.tiamo.fragments.DashboardFragment;
import com.mobile.tiamo.fragments.HomeFragment;
import com.mobile.tiamo.fragments.SettingFragment;
import com.mobile.tiamo.fragments.TestNotificationFragment;
import com.mobile.tiamo.questionaires.SecondQuestionFragment;
import com.mobile.tiamo.services.ScreenOnAndOffService;
import com.mobile.tiamo.services.UpdateDatabaseToServer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;

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
                    fragment = new DashboardFragment();
                    break;
                case R.id.navigation_notifications:
                    fragment = new SettingFragment();
                    break;
                case R.id.test_notifications:
                    fragment = new TestNotificationFragment();
                    break;
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Run service in the mid-night.
        // Service to inform saving the data. Let's do this one first
        serviceUpdateDatabase();
        // Service to inform tracking sleep
//        startSleepTrackingService();

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
        startService(service);

    }


    /*
     * Service for tracking sleep.
     * This service will be run at the time people set for sleeping
     */
    private void startSleepTrackingService(){
        // Save the current time
        Intent serviceIntent = new Intent(this, ScreenOnAndOffService.class);
        startService(serviceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
