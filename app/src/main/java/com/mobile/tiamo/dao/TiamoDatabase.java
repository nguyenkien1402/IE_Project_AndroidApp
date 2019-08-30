package com.mobile.tiamo.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Tasks.class,Schedule.class,DailyActivities.class}, version = 9, exportSchema = false)
public abstract class TiamoDatabase extends RoomDatabase {
    public abstract DailyActivitiesDao dailyActivitiesDao();
    public abstract ScheduleDao scheduleDao();
    public abstract TasksDao tasksDao();

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
