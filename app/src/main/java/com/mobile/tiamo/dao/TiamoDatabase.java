package com.mobile.tiamo.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Tasks.class,Schedule.class, DailyRoutine.class, ActivitiesModel.class, DailyActivitiesModel.class}
        , version = 21
        , exportSchema = false)
public abstract class TiamoDatabase extends RoomDatabase {
    public abstract DailyRoutineDao dailyActivitiesDao();
    public abstract ScheduleDao scheduleDao();
    public abstract TasksDao tasksDao();
    public abstract ActivitiesModelDao activitiesModelDao();
    public abstract DailyActivitiesModelDao dailyActivitiesModelDao();

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
