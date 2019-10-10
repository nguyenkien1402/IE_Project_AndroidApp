package com.mobile.tiamo.questionaires;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mobile.tiamo.MainActivity;
import com.mobile.tiamo.R;
import com.mobile.tiamo.activities.AddingRoutineActivity;
import com.mobile.tiamo.dao.ActivitiesModel;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.Schedule;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.services.ScreenOnAndOffService;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.OtherUtilities;
import com.mobile.tiamo.utilities.SavingDataSharePreference;

import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.List;

public class FifthQuestionFragment extends Fragment {

    TiamoDatabase db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_question_5,container,false);

        Button btnAddingRoutine = (Button) view.findViewById(R.id.adding_more_routine);
        Button btnGooApp = (Button) view.findViewById(R.id.go_to_app);
        db = SQLiteDatabase.getTiamoDatabase(getContext());
        String sleepingTime = SecondQuestionFragment.Companion.getSleepTime();
        String wakeupTime = SecondQuestionFragment.Companion.getWakeupTime();
        // save this data in to SharePreferences
        SavingDataSharePreference.savingLocalData(getContext(),Messages.LOCAL_DATA,Messages.SLEEPING_TIME,sleepingTime);
        SavingDataSharePreference.savingLocalData(getContext(),Messages.LOCAL_DATA,Messages.WAKINGUP_TIME,wakeupTime);
        // adding working time and sleeping time to the schedule
        btnAddingRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddingScheduleAsync addingScheduleAsync = new AddingScheduleAsync();
                addingScheduleAsync.execute(0);

            }
        });

        btnGooApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddingScheduleAsync addingScheduleAsync = new AddingScheduleAsync();
                addingScheduleAsync.execute(1);
                // Save the data to Sharepreferences first

            }
        });

        // Start service to store the sleeping data.

        return view;
    }



    private class AddingScheduleAsync extends AsyncTask<Integer, Void, Integer>{

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Updating");
            dialog.show();
        }

        @Override
        protected Integer doInBackground(Integer... voids) {
            String workingStartTime = FirstQuestionJavaFragment.timeRangeStart;
            String workingEndTime = FirstQuestionJavaFragment.timeRangeEnd;
            List<String> workingDay = FirstQuestionJavaFragment.days;
            String operationDay = getOperationDay(workingDay);
            String specificDay = getSpecificDay(workingDay);
            Schedule schedule = new Schedule();
            schedule.setDayCreated(DateUtilities.getCurrentDateInString());
            schedule.setOperationDay(operationDay);
            schedule.setSpecificDay(specificDay);
            schedule.setTitle("Working");
            schedule.setTimeStart(workingStartTime);
            schedule.setTimeEnd(workingEndTime);
            schedule.setTimeStart(workingStartTime);
            db.scheduleDao().insert(schedule);

            // Adding the Sleeping time
            schedule = new Schedule();
            String sleepingTime = SecondQuestionFragment.Companion.getSleepTime();
            String wakeupTime = SecondQuestionFragment.Companion.getWakeupTime();
            schedule.setDayCreated(DateUtilities.getCurrentDateInString());
            schedule.setTimeStart(sleepingTime);
            schedule.setTimeEnd(wakeupTime);
            schedule.setTitle("Sleeping");
            schedule.setOperationDay("All Day");
            schedule.setSpecificDay("Mon Tue Wed Thu Fri Sat Sun");
            db.scheduleDao().insert(schedule);

            // Adding the daily activity
            // First, get the gym time
            List<ActivitiesModel> activitiesModels = new ArrayList<ActivitiesModel>();
            // Adding hobby activity want to do more
            if(ThirdQuestionFragment.activitiesModels.size() > 0){
                activitiesModels.addAll(ThirdQuestionFragment.activitiesModels);
            }
            // Now, add to SQL
            db.activitiesModelDao().insertAll(activitiesModels);

            return voids[0];
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if(aVoid == 0){
                SavingDataSharePreference.savingLocalData(getContext(), Messages.LOCAL_DATA,"flagAnswer",1);
                Intent i = new Intent(getActivity(), AddingRoutineActivity.class);

                getActivity().finish();
                startActivity(i);
            }else{
                SavingDataSharePreference.savingLocalData(getContext(), Messages.LOCAL_DATA,"flagAnswer",1);
                Intent i = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(i);
            }
        }
    }

    public String getSpecificDay(List<String> workingDay){
        String specificDay  = "";
        for(int i = 0 ; i < workingDay.size() ; i++){
            specificDay = specificDay + OtherUtilities.dictonaryMapDayToAbb().get(workingDay.get(i)) + " ";
        }
        return specificDay;
    }
    public String getOperationDay(List<String> workingDay){
        String operationDay  = "";
        if(workingDay.size() == 7){
            operationDay = "All Day";
        }else if(workingDay.contains("Monday") && workingDay.contains("Tuesday") && workingDay.contains("Wednesday") &&
                workingDay.contains("Thursday") && workingDay.contains("Friday") && workingDay.size() == 5) {
            operationDay = "Weekdays";
        }else if(workingDay.contains("Saturday") && workingDay.contains("Sunday")){
            operationDay = "Weekends";
        }else{
            for(int i = 0 ; i < workingDay.size() ; i++){
                operationDay = operationDay + OtherUtilities.dictonaryMapDayToAbb().get(workingDay.get(i)) + " ";
            }
        }
        operationDay = operationDay.trim();
        return operationDay;
    }



    public static FifthQuestionFragment newInstance(String text){
        FifthQuestionFragment f = new FifthQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg",text);
        f.setArguments(bundle);
        return f;
    }
}
