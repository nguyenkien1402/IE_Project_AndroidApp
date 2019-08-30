package com.mobile.tiamo.activities;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mobile.tiamo.R;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.Schedule;
import com.mobile.tiamo.dao.Tasks;
import com.mobile.tiamo.dao.TiamoDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AddingScheduleActivity extends AppCompatActivity {

    TiamoDatabase db;
    Spinner spinner;
    List<Tasks> tasks;
    View popupInputDialogView = null;
    EditText popup_ed_title;
    Button btnCancel, btnAdd, btnAddSchedule;
    ArrayAdapter<String> adaper;
    TimePickerDialog timePickerDialog;
    TextView timeStart,timeEnd, daySelected;
    String[] listDays;
    String[] listDaysAbb;
    boolean[] checkedDays;
    ArrayList<Integer> mItems = new ArrayList<>();
    String currentTitle, specificDay="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = SQLiteDatabase.getTiamoDatabase(getApplicationContext());

        initComponent();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddingScheduleActivity.this);
                alertDialogBuilder.setCancelable(false);

                // Init popup dialog view and it's ui controls.
                initPopupViewControls();

                alertDialogBuilder.setView(popupInputDialogView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddTaskAsync addTaskAsync = new AddTaskAsync();
                        addTaskAsync.execute(popup_ed_title.getText().toString());
                        alertDialog.cancel();
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
            }
        });

        // Loading the tasks to spinner
        spinner = (Spinner) findViewById(R.id.adding_schedule_spiner);
        GetAllTaskAsync getAllTaskAsync = new GetAllTaskAsync();
        getAllTaskAsync.execute();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentTitle = tasks.get(position).getTitle();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeS = timeStart.getText().toString();
                String timeE = timeEnd.getText().toString();
                String days = daySelected.getText().toString();
                AddingScheduleAsync addingScheduleAsync = new AddingScheduleAsync();
                addingScheduleAsync.execute(new String[]{currentTitle,timeS,timeE,days});
            }
        });
    }

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

            DateFormat df = new SimpleDateFormat("hh:mm");
            try {
                Date d1 = df.parse(timeStart);
                Date d2 = df.parse(timeEnd);
                long diff = d2.getTime() - d1.getTime();
                String hours = new SimpleDateFormat("hh:mm").format(new Date(diff));
                schedule.setHours(hours);
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
                Toast.makeText(getApplicationContext(),"Add Schedule Successfully: "+aLong,Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initComponent(){
        timeStart = (TextView) findViewById(R.id.adding_schedule_time_start);
        timeEnd = (TextView) findViewById(R.id.adding_schedule_time_end);
        daySelected = (TextView) findViewById(R.id.adding_schedule_day);
        btnAddSchedule = (Button) findViewById(R.id.adding_schedule_btn_add);

        listDays = getResources().getStringArray(R.array.day_of_week);
        listDaysAbb = getResources().getStringArray(R.array.day_of_week_abb);
        checkedDays = new boolean[listDays.length];
    }

    private class GetAllTaskAsync extends AsyncTask<Void,Void,List<Tasks>>{
        @Override
        protected List<Tasks> doInBackground(Void... voids) {
            if(db.tasksDao().getAll() != null){
                tasks = db.tasksDao().getAll();
                return tasks;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Tasks> tasks) {
            if(tasks!=null){
                fillSpiner(tasks);
            }
            super.onPostExecute(tasks);
        }

        private void fillSpiner(List<Tasks> tasks) {
            List<String> t = new ArrayList<String>();
            for(int i = 0 ; i < tasks.size();i++){
                t.add(tasks.get(i).getTitle());
            }
            adaper = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,t);
            adaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adaper);
        }
    }

    public void initPopupViewControls(){
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());

        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.popup_input_task, null);

        popup_ed_title = (EditText) popupInputDialogView.findViewById(R.id.popup_ed_title);
        btnCancel = popupInputDialogView.findViewById(R.id.popup_btn_cancel);
        btnAdd = popupInputDialogView.findViewById(R.id.popup_btn_add);
    }

    private class AddTaskAsync extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String title = strings[0];
            Tasks task = new Tasks(title);
            tasks.add(task);
            long id = db.tasksDao().insert(task);
            return title;
        }

        @Override
        protected void onPostExecute(String aLong) {
            super.onPostExecute(aLong);
            adaper.add(aLong);
            adaper.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(),"Selected:"+aLong,Toast.LENGTH_SHORT).show();
        }
    }

    public void selectEndTime(View view){
        Toast.makeText(getApplicationContext(),"End",Toast.LENGTH_SHORT).show();
        final Calendar cldr = Calendar.getInstance();
        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);
        // time picker dialog
        timePickerDialog = new TimePickerDialog(AddingScheduleActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        timeEnd.setText(sHour + ":" + sMinute);
                    }
                }, hour, minutes, true);
        timePickerDialog.show();
    }

    public void selectStartTime(View view){
        Toast.makeText(getApplicationContext(),"Start",Toast.LENGTH_SHORT).show();
        final Calendar cldr = Calendar.getInstance();
        int hour = cldr.get(Calendar.HOUR_OF_DAY);
        int minutes = cldr.get(Calendar.MINUTE);
        // time picker dialog
        timePickerDialog = new TimePickerDialog(AddingScheduleActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        timeStart.setText(sHour + ":" + sMinute);
                    }
                }, hour, minutes, true);
        timePickerDialog.show();
    }

    public void selectDay(View view){
        Toast.makeText(getApplicationContext(),"day",Toast.LENGTH_SHORT).show();
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddingScheduleActivity.this);
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
                mItems.removeAll(s1);
                Collections.sort(mItems);
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
                    item = item + " "+ listDaysAbb[mItems.get(i)];
                    specificDay = specificDay + " " + listDaysAbb[mItems.get(i)];
//                    if(i != mItems.size()-1){
//                        item = item +",";
//                    }
                }
                item = item.trim();
                specificDay = specificDay.trim();
                daySelected.setText(item);
            }
        });

        mBuilder.setNegativeButton("Dimiss", new DialogInterface.OnClickListener() {
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
                    daySelected.setText(".....");
                }
            }
        });

        AlertDialog alertDialog = mBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        setResult(RESULT_OK,returnIntent);
        finish();
        return true;
    }

}
