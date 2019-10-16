package com.mobile.tiamo.activities;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.mcsoft.timerangepickerdialog.RangeTimePickerDialog;
import com.mobile.tiamo.MainActivity;
import com.mobile.tiamo.R;
import com.mobile.tiamo.dao.DailyRoutine;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.Schedule;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.LocalNotifications;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
  This activity is used to add new daily routine
  Ex: Working, Sleeping, etc
 **/
public class AddingRoutineActivity extends AppCompatActivity implements RangeTimePickerDialog.ISelectedTime {

    public static int CODE_RESULT = 3;
    private TiamoDatabase db;
    private EditText edTitle;
    private Button btnAddRoutine;
    private TextView timeStart,timeEnd, daySelected;
    private String[] listDays;
    private String[] listDaysAbb;
    private boolean[] checkedDays;
    private ArrayList<Integer> mItems = new ArrayList<>();
    private String specificDay="";
    private LocalNotifications localNotifications;
    private int isStart;

    /*
     Create the view of activity
     Simultaneously to initialize component.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isStart = getIntent().getIntExtra("IsStart",-1);
        if(isStart==-1){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        db = SQLiteDatabase.getTiamoDatabase(getApplicationContext());
        initComponent();
        localNotifications = new LocalNotifications(getApplicationContext());

        btnAddRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edTitle.getText() != null && !edTitle.getText().toString().trim().equals("")) {
                    if(mItems.size() == 0){
                        Toast.makeText(getApplicationContext(),"Please select days for routine", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(!timeStart.getText().toString().trim().equals("") && !timeEnd.getText().toString().equals("")){
                        if(daySelected.getText().toString().equals("All Day")){
                            for(int i = 0 ; i < 7;i++){
                                specificDay = specificDay + " " + listDaysAbb[i];
                            }
                            Log.d("SpecificDay",specificDay);
                            specificDay = specificDay.trim();
                        }
                        String timeS = timeStart.getText().toString();
                        String timeE = timeEnd.getText().toString();
                        String days = daySelected.getText().toString();
                        String title = edTitle.getText().toString();
                        AddingScheduleAsync addingScheduleAsync = new AddingScheduleAsync();
                        addingScheduleAsync.execute(new String[]{title,timeS,timeE,days});
                    }else{
                        Toast.makeText(getApplicationContext(),"Please set time for routines", Toast.LENGTH_LONG).show();
                        return;
                    }

                }else{
                    edTitle.setError("Title Not Null");
                }

            }
        });
    }

    /*
     If u don't know what this line is, you are the dump ass
     */
    @Override
    public void onSelectedTime(int hourStart, int minuteStart, int hourEnd, int minuteEnd) {
        timeStart.setText(hourStart +":"+minuteStart);
        timeEnd.setText(hourEnd +":"+minuteEnd);
    }

    /*
     Again, this one is using to add new routine activity
     */
    private class AddingScheduleAsync extends AsyncTask<String,Void,Long>{
        @Override
        protected Long doInBackground(String... strings) {
            String title = strings[0];
            String timeStart = strings[1];
            String timeEnd  = strings[2];
            String days = strings[3];

            Schedule schedule = new Schedule();
            schedule.setTimeStart(timeStart);
            schedule.setTimeEnd(timeEnd);
            schedule.setTitle(title);
            schedule.setOperationDay(days);
            schedule.setSpecificDay(specificDay);

            String dayAbb = DateUtilities.getCurrentDayInAbb();
            if(specificDay.contains(dayAbb)){
                DailyRoutine dailyRoutine = new DailyRoutine();
                dailyRoutine.setHours(schedule.getTimeStart() + " - " + schedule.getTimeEnd());
                dailyRoutine.setTitle(schedule.getTitle());
                dailyRoutine.setTimeStart(schedule.getTimeStart());
                dailyRoutine.setTimeEnd(schedule.getTimeEnd());
                dailyRoutine.setDayOperation(schedule.getSpecificDay());
                localNotifications.scheduleNotificationForNewRoutine(dailyRoutine);
            }

            DateFormat df = new SimpleDateFormat("hh:mm");
            try {
                Date d1 = df.parse(timeStart);
                Date d2 = df.parse(timeEnd);
                long diff = d2.getTime() - d1.getTime();
                String hours = new SimpleDateFormat("hh:mm").format(new Date(diff));
                schedule.setHours(hours);
                String currentDate = DateUtilities.getCurrentDateInString();
                schedule.setDayCreated(currentDate);
                long uid = db.scheduleDao().insert(schedule);
                return uid;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            if(aLong != null){
                if(isStart==-1){
                    Toast.makeText(getApplicationContext(),"Add Routine Successfully",Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    setResult(AboutUsActivity.RESULT_OK,returnIntent);
                    finish();
                }else{
                    Intent i = new Intent(AddingRoutineActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }
    }


    private void initComponent(){
        edTitle = (EditText) findViewById(R.id.ed_adding_routine_title);
        timeStart = (TextView) findViewById(R.id.adding_schedule_time_start);
        timeEnd = (TextView) findViewById(R.id.adding_schedule_time_end);
        daySelected = (TextView) findViewById(R.id.adding_schedule_day);
        btnAddRoutine = (Button) findViewById(R.id.adding_schedule_btn_add);

        listDays = getResources().getStringArray(R.array.day_of_week);
        listDaysAbb = getResources().getStringArray(R.array.day_of_week_abb);
        checkedDays = new boolean[listDays.length];
        for(int i = 0 ; i < 7;i++){
            checkedDays[i] = true;
            mItems.add(i);
        }
    }


    public void selectStartTime(View view){
        RangeTimePickerDialog dialog = new RangeTimePickerDialog();
        dialog.newInstance();
        dialog.setRadiusDialog(20); // Set radius of dialog (default is 50)
        dialog.setIs24HourView(false); // Indicates if the format should be 24 hours
        dialog.setColorBackgroundHeader(R.color.colorPrimary); // Set Color of Background header dialog
        dialog.setColorTextButton(R.color.white); // Set Text color of button
        dialog.setColorBackgroundTimePickerHeader(R.color.colorPrimaryDark);
        FragmentManager fragmentManager = getFragmentManager();
        dialog.show(fragmentManager, "");
    }

    public void selectDay(View view){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddingRoutineActivity.this);
        mBuilder.setTitle("Select Day");
        mBuilder.setMultiChoiceItems(listDays, checkedDays, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                if(isChecked){
                    mItems.add(position);
                }else{
                    mItems.remove((Integer.valueOf(position)));
                }
            }
        });

        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item = "";
                List s1=new ArrayList();
                s1.add(null);
                Log.d("Item Size", mItems.size()+"");
                mItems.removeAll(s1);
                Collections.sort(mItems);
                if(mItems.size() == 0){
                    daySelected.setText("All Day");
                    return;
                }
                if(mItems.size()==7){
                    daySelected.setText("All Day");
                    for(int i = 0 ; i < mItems.size();i++){
                        specificDay = specificDay + " " + listDaysAbb[mItems.get(i)];
                    }
                    specificDay = specificDay.trim();
                    return;
                }
                if(mItems.size()==2 && mItems.contains(5) && mItems.contains(6)){
                    daySelected.setText("Weekends");
                    for(int i = 0 ; i < mItems.size();i++){
                        specificDay = specificDay + " " + listDaysAbb[mItems.get(i)];
                    }
                    specificDay = specificDay.trim();
                    return;
                }
                if(mItems.size()==5 && mItems.contains(0) && mItems.contains(1) && mItems.contains(2) &&
                        mItems.contains(3) && mItems.contains(4)){
                    daySelected.setText("Weekdays");
                    for(int i = 0 ; i < mItems.size();i++){
                        specificDay = specificDay + " " + listDaysAbb[mItems.get(i)];
                    }
                    specificDay = specificDay.trim();
                    return;
                }

                // remove null value

                for(int i = 0 ; i < mItems.size();i++){
                    Log.d("Item",mItems.get(i)+"");
                    item = item + " "+ listDaysAbb[mItems.get(i)];
                    specificDay = specificDay + " " + listDaysAbb[mItems.get(i)];
                }
                item = item.trim();
                specificDay = specificDay.trim();
                daySelected.setText(item);
            }
        });

        mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for(int i = 0 ; i < checkedDays.length ; i++){
                    checkedDays[i] = false;
                    mItems.clear();
                    daySelected.setText("No Selected Days");
                }
            }
        });

        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
//        Intent returnIntent = new Intent();
//        setResult(CODE_RESULT,returnIntent);
        if(isStart==-1){
            finish();
        }else{
            Intent i = new Intent(AddingRoutineActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }

}
