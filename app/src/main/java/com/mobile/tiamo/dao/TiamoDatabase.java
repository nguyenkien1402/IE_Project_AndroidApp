package com.mobile.tiamo.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Schedule.class, DailyRoutine.class, ActivitiesModel.class,
        DailyActivityHobbyModel.class, SleepingModel.class, StepsTakenModel.class}
        , version = 2
        , exportSchema = false)
public abstract class TiamoDatabase extends RoomDatabase {
    public abstract DailyRoutineDao dailyActivitiesDao();
    public abstract ScheduleDao scheduleDao();
    public abstract ActivitiesModelDao activitiesModelDao();
    public abstract DailyActivityHobbyModelDao dailyActivityHobbyModelDao();
    public abstract SleepingModelDao sleepingModelDao();
    public abstract StepsTakenDao stepsTakenDao();

    private static volatile TiamoDatabase INSTANCE;
    static TiamoDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (TiamoDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TiamoDatabase.class, "tiamo_database").build();
                }
            }
        }
        return INSTANCE;
    }

}
