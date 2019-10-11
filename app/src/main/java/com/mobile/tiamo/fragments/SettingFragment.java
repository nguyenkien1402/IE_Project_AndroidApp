package com.mobile.tiamo.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.tiamo.MainActivity;
import com.mobile.tiamo.R;
import com.mobile.tiamo.activities.AboutUsActivity;
import com.mobile.tiamo.activities.MovieRecommendationActivity;
import com.mobile.tiamo.activities.SearchMoviesActivity;
import com.mobile.tiamo.dao.ActivitiesModel;
import com.mobile.tiamo.dao.DailyActivityHobbyModel;
import com.mobile.tiamo.dao.DailyRoutine;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.Schedule;
import com.mobile.tiamo.dao.SleepingModel;
import com.mobile.tiamo.dao.StepsTakenModel;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.SavingDataSharePreference;

import org.threeten.bp.LocalTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * A setting fragment
 */
public class SettingFragment extends Fragment {

    TextView tvAboutUs, deleteAll, btnMovies;
    TiamoDatabase db;
    Random rand = new Random();
    List<String> pickUpChildrenDay = new ArrayList<String>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        MainActivity.textToolbar.setText(R.string.app_name);
        tvAboutUs = view.findViewById(R.id.setting_about_us);
        deleteAll = view.findViewById(R.id.deleteAll);
        btnMovies = view.findViewById(R.id.btnMovies);
        db = SQLiteDatabase.getTiamoDatabase(getActivity());
        pickUpChildrenDay.add("Mon");
        pickUpChildrenDay.add("Wed");
        pickUpChildrenDay.add("Fri");
        tvAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateData();
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DeleteAllAsync del = new DeleteAllAsync();
//                del.execute();
            }
        });

        btnMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchMoviesActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


    private void populateData(){
//        boolean isPopulate = SavingDataSharePreference.getDataBoolean(getActivity(), Messages.LOCAL_DATA,"isPopulate");
//        if(isPopulate==false){
            DeleteAllAsync deleteAllAsync = new DeleteAllAsync();
            deleteAllAsync.execute();
//        }
    }

    private class DeleteAllAsync extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            deleteAllDatabase();
            populateDailyRoutine();
            populateDailyActivity();
            populateSleepingData();
            populateStepData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SavingDataSharePreference.savingLocalData(getActivity(),Messages.LOCAL_DATA,"isPopulate",true);

//            PopulateDataAsync populateDataAync = new PopulateDataAsync();
//            populateDataAync.execute();
        }
    }


    private class PopulateDataAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
                // mean no data, insert data here
//            populateDailyRoutine();
//            populateDailyActivity();
//            populateSleepingData();
//            populateStepData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SavingDataSharePreference.savingLocalData(getActivity(),Messages.LOCAL_DATA,"isPopulate",true);

//            Intent intent = new Intent(getActivity(),AboutUsActivity.class);
//            startActivity(intent);
//            m.dismiss();
        }
    }

    private void deleteAllDatabase(){
        db.stepsTakenDao().deleteAll();
        db.sleepingModelDao().deleteAll();
        db.dailyActivityHobbyModelDao().deleteAll();
        db.activitiesModelDao().deleteAll();
        db.dailyActivitiesDao().deleteAll();
        db.scheduleDao().deleteAll();
        SavingDataSharePreference.savingLocalData(getActivity(),Messages.LOCAL_DATA,"isPopulate",false);
        SavingDataSharePreference.savingLocalData(getActivity(),Messages.LOCAL_DATA,"No-"+DateUtilities.getCurrentDateInString(),false);
    }

    private void populateDailyRoutine(){
        // Adding to Schedule first
        //1 . Working
        Schedule schedule = new Schedule();
        schedule.setTitle("Working");
        schedule.setTimeStart("9:00");
        schedule.setTimeEnd("17:00");
        schedule.setOperationDay("Weekdays");
        schedule.setSpecificDay("Mon Tue Wed Thu Fri");
        db.scheduleDao().insert(schedule);

        // 2. Sleeping
        schedule = new Schedule();
        schedule.setTitle("Sleeping");
        schedule.setTimeStart("23:45");
        schedule.setTimeEnd("06:00");
        schedule.setOperationDay("All Day");
        schedule.setSpecificDay("Mon Tue Wed Thu Fri Sat Sun");
        db.scheduleDao().insert(schedule);

        // 3. Pickup Children
        schedule = new Schedule();
        schedule.setTitle("Pickup Children");
        schedule.setTimeStart("17:00");
        schedule.setTimeEnd("18:00");
        schedule.setOperationDay("Mon Wed Fri");
        schedule.setSpecificDay("Mon Wed Fri");
        db.scheduleDao().insert(schedule);

        // Now going to add the Daily Routine
        List<DailyRoutine> dailyRoutines = new ArrayList<DailyRoutine>();
        try{
            String today = DateUtilities.getCurrentDateInString();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = dateFormat.parse(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            for(int i = 1 ; i <= 10 ; i++){
                calendar.add(Calendar.DAY_OF_YEAR,-1);
                Date previous = calendar.getTime();
                String date = dateFormat.format(previous);
                // adding for working first.
                String abb = DateUtilities.getWeekDayAbbFromDate(previous);
                if(!abb.equals("Sat") && !abb.equals("Sun")){
                    DailyRoutine dailyRoutine = new DailyRoutine();
                    dailyRoutine.setTitle("Working");
                    dailyRoutine.setIsDone(0);
                    int randomNum = rand.nextInt((110 - 0) + 1) + 0;
                    dailyRoutine.setHours("9:00 - 17:00");
                    dailyRoutine.setTimeStart("9:00");
                    String timeEnd;
                    if(randomNum > 60){
                        timeEnd = "18:" + (randomNum-60);
                    }else{
                        timeEnd = "17:" + randomNum;
                    }
                    dailyRoutine.setTimeEnd("17:00");
                    dailyRoutine.setTimeActuallyEnd(timeEnd);
                    dailyRoutine.setDate(date);
                    dailyRoutine.setDayOperation("Mon Tue Wed Thu Fri");
                    dailyRoutines.add(dailyRoutine);
                }

                // adding for sleeping
                DailyRoutine sleepingRoutine = new DailyRoutine();
                sleepingRoutine.setTitle("Sleeping");
                sleepingRoutine.setIsDone(0);
                sleepingRoutine.setDate(date);
                sleepingRoutine.setHours("23:45 - 06:00");
                sleepingRoutine.setTimeStart("23:45");
                sleepingRoutine.setTimeEnd("06:00");
                sleepingRoutine.setTimeActuallyEnd("06:00");
                sleepingRoutine.setDayOperation("Mon Tue Wed Thu Fri Sat Sun");
                dailyRoutines.add(sleepingRoutine);
                if(pickUpChildrenDay.contains(abb)){
                    // adding for pickup children
                    DailyRoutine pickupChildren = new DailyRoutine();
                    pickupChildren.setTitle("Pickup Children");
                    pickupChildren.setIsDone(0);
                    pickupChildren.setDate(date);
                    pickupChildren.setHours("17:00 - 18:00");
                    pickupChildren.setTimeStart("17:00");
                    pickupChildren.setTimeEnd("18:00");
                    pickupChildren.setTimeActuallyEnd("18:00");
                    pickupChildren.setDayOperation("Mon Wed Fri");
                    dailyRoutines.add(pickupChildren);
                }

            }
            db.dailyActivitiesDao().insertAll(dailyRoutines);


        }catch (Exception e){
            Log.d("TAG",e.getMessage());
        }


    }

    private void populateDailyActivity(){
        // First, populate activity
        List<ActivitiesModel> activitiesModels = new ArrayList<ActivitiesModel>();
        ActivitiesModel model = new ActivitiesModel();
        model.setTitle("Gym");
        model.setHours(8);
        activitiesModels.add(model);

        model = new ActivitiesModel();
        model.setTitle("Reading");
        model.setHours(10);
        activitiesModels.add(model);

        model = new ActivitiesModel();
        model.setTitle("Meditation");
        model.setHours(2);
        activitiesModels.add(model);

        model = new ActivitiesModel();
        model.setTitle("Cycling");
        model.setHours(3);
        activitiesModels.add(model);

        model = new ActivitiesModel();
        model.setTitle("Photography");
        model.setHours(5);
        activitiesModels.add(model);

        db.activitiesModelDao().insertAll(activitiesModels);

        // Now, for adding day frequently.
        List<DailyActivityHobbyModel> activityHobbyModels = new ArrayList<DailyActivityHobbyModel>();
        try {
            String today = DateUtilities.getCurrentDateInString();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = dateFormat.parse(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            for (int i = 1; i <= 10; i++) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                DailyActivityHobbyModel m = new DailyActivityHobbyModel();
                Date previous = calendar.getTime();
                String date = dateFormat.format(previous);
                String abb = DateUtilities.getWeekDayAbbFromDate(previous);
                if(abb.equals("Sat") || abb.equals("Sun")){
                    // Cycling
                    int randomNum = rand.nextInt((180 - 60) + 1) + 60;
                    if(randomNum > 120){
                        m = new DailyActivityHobbyModel();
                        m.setTitle("Cycling");
                        m.setDateCreated(date);
                        m.setHours(2);
                        m.setMinutes(randomNum-120);
                    }else{
                        m = new DailyActivityHobbyModel();
                        m.setTitle("Cycling");
                        m.setDateCreated(date);
                        m.setHours(1);
                        m.setMinutes(randomNum-60);
                    }
                    activityHobbyModels.add(m);

                    // adding photography
                    randomNum = rand.nextInt((240 - 120) + 1) + 120;
                    if(randomNum > 180){
                        m = new DailyActivityHobbyModel();
                        m.setTitle("Photography");
                        m.setDateCreated(date);
                        m.setHours(3);
                        m.setMinutes(randomNum-180);
                    }else{
                        m = new DailyActivityHobbyModel();
                        m.setTitle("Photography");
                        m.setDateCreated(date);
                        m.setHours(2);
                        m.setMinutes(randomNum-120);
                    }
                    activityHobbyModels.add(m);

                    // reading in the weekend
                    randomNum = rand.nextInt((120 - 60) + 1) + 30;
                    if(randomNum > 60){
                        m = new DailyActivityHobbyModel();
                        m.setTitle("Reading");
                        m.setDateCreated(date);
                        m.setHours(1);
                        m.setMinutes(randomNum-60);
                    }else{
                        m = new DailyActivityHobbyModel();
                        m.setTitle("Reading");
                        m.setDateCreated(date);
                        m.setMinutes(randomNum);
                    }
                    activityHobbyModels.add(m);

                }else{
                    // in the week day
                    // gym
                    int randomNum = rand.nextInt((120 - 60) + 1) + 45;
                    if(randomNum > 60){
                        m = new DailyActivityHobbyModel();
                        m.setTitle("Gym");
                        m.setDateCreated(date);
                        m.setHours(1);
                        m.setMinutes(randomNum-60);
                    }else{
                        m = new DailyActivityHobbyModel();
                        m.setTitle("Gym");
                        m.setDateCreated(date);
                        m.setHours(0);
                        m.setMinutes(randomNum);
                    }
                    activityHobbyModels.add(m);

                    // reading in the weekdays
                    randomNum = rand.nextInt((120 - 60) + 1) + 30;
                    if(randomNum > 60){
                        m = new DailyActivityHobbyModel();
                        m.setTitle("Reading");
                        m.setDateCreated(date);
                        m.setHours(1);
                        m.setMinutes(randomNum-60);
                    }else{
                        m = new DailyActivityHobbyModel();
                        m.setTitle("Reading");
                        m.setDateCreated(date);
                        m.setHours(0);
                        m.setMinutes(randomNum);
                    }
                    activityHobbyModels.add(m);

                    // meditation in the weekdays
                    randomNum = rand.nextInt((30 - 10) + 1) + 10;
                    m = new DailyActivityHobbyModel();
                    m.setTitle("Meditation");
                    m.setDateCreated(date);
                    m.setHours(0);
                    m.setMinutes(randomNum);
                    activityHobbyModels.add(m);
                }
            }
            db.dailyActivityHobbyModelDao().insertAll(activityHobbyModels);
        }catch (Exception e){
            Log.d("TAG",e.getMessage());
        }
    }

    private void populateSleepingData(){
        List<SleepingModel> sleepingModels;
        try {
            String today = DateUtilities.getCurrentDateInString();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = dateFormat.parse(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            for (int i = 1; i <= 10; i++) {
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                Date previous = calendar.getTime();
                String date = dateFormat.format(previous);
                sleepingModels = getSleepingModel(date);
                db.sleepingModelDao().insertAll(sleepingModels);
            }
        }catch (Exception e){
            Log.d("TAG",e.getMessage());
        }

    }

    private List<SleepingModel> getSleepingModel(String date) {
        // 1. Time start to sleep is 23:45
        // 2. Time to wakeup is 6 am in the morning.
        List<SleepingModel> list = new ArrayList<SleepingModel>();
        int randomWakeup = rand.nextInt((3-1)+1)+1; // randomWakeup = [1,2]

        SleepingModel s = new SleepingModel();
        s.setDate(date);
        s.setIsStorage(0);
        int randomTimeSleep = rand.nextInt((60 - 0) + 1) + 0;
        if(randomTimeSleep > 15){
            String m="";
            if((randomTimeSleep - 15)<9){
                m = "0" + (randomTimeSleep-15);
            }else{
                m = (randomTimeSleep-15)+"";
            }
            s.setTime("00:"+m);
        }else{
            s.setTime("23:"+(45+randomTimeSleep));
        }
        for(int i = 0 ; i < randomWakeup ; i++){
            // random number time when wakeup
            int randomTimeAwake = rand.nextInt((120-60)+1)+60;
            LocalTime now = LocalTime.parse(s.getTime());
            now = now.plusMinutes(randomTimeAwake);
            s.setWakeupTime(now.toString());
            list.add(s);


            // Random to go to sleep again
            int randomSleep = rand.nextInt((45-20)+1)+20;
            s = new SleepingModel();
            s.setDate(date);
            s.setIsStorage(0);
            s.setTime(now.toString());
            now = now.plusMinutes(randomSleep);
            if(now.compareTo(LocalTime.of(6,30))==1){
                Log.d("TAG","Populate Break");
                break;
            }


        }

        //add the real wakeup time
        int randomRealWakeUp = rand.nextInt((45-10)+1)+10;
        String wakeup = LocalTime.parse("06:00").plusMinutes(randomRealWakeUp).toString();
        s.setWakeupTime(wakeup);
        list.add(s);
        return list;
    }

    private void populateStepData(){

        List<StepsTakenModel> stepsTakenModels = new ArrayList<StepsTakenModel>();
        try{
            String today = DateUtilities.getCurrentDateInString();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = dateFormat.parse(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            for (int i = 1; i <= 10; i++) {
                StepsTakenModel m = new StepsTakenModel();
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                Date previous = calendar.getTime();
                String date = dateFormat.format(previous);
                int random = rand.nextInt((5200 - 500) + 1) + 500;
                m.setDate(date);
                m.setSteps(random);
                stepsTakenModels.add(m);
            }
            db.stepsTakenDao().insertAll(stepsTakenModels);
        }catch (Exception e){
            Log.d("TAG",e.getMessage());
        }
    }
}
