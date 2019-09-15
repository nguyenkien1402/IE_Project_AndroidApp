package com.mobile.tiamo.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;

import com.mobile.tiamo.dao.DailyActivityHobbyModel;
import com.mobile.tiamo.dao.DailyRoutine;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.SleepingModel;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.models.DailyActivity;
import com.mobile.tiamo.models.SleepingTime;
import com.mobile.tiamo.rest.services.IDailyActivity;
import com.mobile.tiamo.rest.services.IDailyRoutine;
import com.mobile.tiamo.rest.services.ISleepingTime;
import com.mobile.tiamo.rest.services.IXbrainUser;
import com.mobile.tiamo.rest.services.RetrofitService;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.OtherUtilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateDatabaseToServer extends IntentService {

    TiamoDatabase db;
    IDailyRoutine iDailyRoutine;
    IDailyActivity iDailyActivity;
    ISleepingTime iSleepingTime;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public UpdateDatabaseToServer() {
        super("UpdateDatabaseToServer");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("ServiceDatabase","Start Update Database Service");
        db = SQLiteDatabase.getTiamoDatabase(getApplicationContext());
        iDailyRoutine = RetrofitService.getRetrofitService().create(IDailyRoutine.class);
        iDailyActivity = RetrofitService.getRetrofitService().create(IDailyActivity.class);
        iSleepingTime = RetrofitService.getRetrofitService().create(ISleepingTime.class);
        CheckingAndUpdatingRoutineToDatabaseAsync update = new CheckingAndUpdatingRoutineToDatabaseAsync();
        update.execute();

        CheckingAndUpdatingActivityToDatabaseAsync activity= new CheckingAndUpdatingActivityToDatabaseAsync();
        activity.execute();

        CheckingAndUpdatingSleepingToDatabaseAsync sleep = new CheckingAndUpdatingSleepingToDatabaseAsync();
        sleep.execute();
    }

    private class CheckingAndUpdatingSleepingToDatabaseAsync extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            List<SleepingModel> sleepingModels = db.sleepingModelDao().getSleepingUnStorage();
            for(int i = 0 ; i < sleepingModels.size() ; i++){
                final SleepingTime sleepingTime = new SleepingTime();
                sleepingTime.setDateAchieve(DateUtilities.convertDateFormat(sleepingModels.get(i).getDate()));
                sleepingTime.setSleepingTime(sleepingModels.get(i).getTime());
                Call<SleepingTime> call = iSleepingTime.insert(1,sleepingTime);
                call.enqueue(new Callback<SleepingTime>() {
                    @Override
                    public void onResponse(Call<SleepingTime> call, Response<SleepingTime> response) {
                        Log.d("TAG","Update Sleeping time to database");
                    }

                    @Override
                    public void onFailure(Call<SleepingTime> call, Throwable t) {

                    }
                });
                db.sleepingModelDao().updateStorageSleeping();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private class CheckingAndUpdatingActivityToDatabaseAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            List<DailyActivityHobbyModel> dailyActivityHobbyModels =  db.dailyActivityHobbyModelDao().getDailyActivityHobbyUnStorage();
            for(int i = 0 ; i < dailyActivityHobbyModels.size() ; i++){
                DailyActivityHobbyModel d = dailyActivityHobbyModels.get(i);
                DailyActivity da = new DailyActivity();
                da.setActivityTitle(d.getTitle());
                da.setDateAchieve(DateUtilities.convertDateFormat(d.getDateCreated()));
                da.setNumberOfHour(d.getHours());
                da.setTargetHour(OtherUtilities.floatHour(d.getHours(),d.getMinutes()));
                Call<DailyActivity> call = iDailyActivity.insert(1,da);
                call.enqueue(new Callback<DailyActivity>() {
                    @Override
                    public void onResponse(Call<DailyActivity> call, Response<DailyActivity> response) {
                        Log.d("TAG","Insert Success");
                        // Update database

                    }

                    @Override
                    public void onFailure(Call<DailyActivity> call, Throwable t) {

                    }
                });
                db.dailyActivityHobbyModelDao().updateStorage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void dailyActivityHobbyModels) {
            super.onPostExecute(dailyActivityHobbyModels);

        }
    }

    private class CheckingAndUpdatingRoutineToDatabaseAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Get all the data which doesn't have the flag update
            // Get the routine data first.
            List<DailyRoutine> list = db.dailyActivitiesDao().getDailyRoutineUnUpdate();
            for(int i = 0 ; i < list.size() ; i++){
                DailyRoutine d = list.get(i);
                com.mobile.tiamo.models.DailyRoutine da = new com.mobile.tiamo.models.DailyRoutine();
                da.setRoutineTitle(d.getTitle());
                da.setPlanStartTime(d.getTimeStart());
                da.setDateAchieve(DateUtilities.convertDateFormat(list.get(i).getDate()));
                da.setDayOperation(d.getDayOperation());
                da.setPlanEndTime(d.getTimeEnd());
                da.setActualStartTime(d.getTimeStart());
                da.setActualEndTime(d.getTimeActuallyEnd());
                Call<com.mobile.tiamo.models.DailyRoutine> call = iDailyRoutine.insert(1,da);
                call.enqueue(new Callback<com.mobile.tiamo.models.DailyRoutine>() {
                    @Override
                    public void onResponse(Call<com.mobile.tiamo.models.DailyRoutine> call, Response<com.mobile.tiamo.models.DailyRoutine> response) {
                        Log.d("TAG","Insert Successfully");
                        // Update to database

                    }

                    @Override
                    public void onFailure(Call<com.mobile.tiamo.models.DailyRoutine> call, Throwable t) {

                    }
                });
            }
            db.dailyActivitiesDao().updateStorage();
            return null;
        }

        @Override
        protected void onPostExecute(Void list) {
            super.onPostExecute(list);

        }
    }


}
