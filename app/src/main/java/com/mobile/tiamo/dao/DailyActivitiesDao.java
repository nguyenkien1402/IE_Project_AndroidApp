package com.mobile.tiamo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DailyActivitiesDao {

    @Query("Select * From DailyActivities")
    List<DailyActivities> getAll();

    @Insert
    long insert(DailyActivities dailyActivities);
}
