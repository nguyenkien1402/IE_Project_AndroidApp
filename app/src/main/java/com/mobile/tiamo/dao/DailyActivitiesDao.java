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

    @Query("Select * from DailyActivities WHERE date = :date")
    List<DailyActivities> getDailyActivities(String date);

    @Insert
    void insertAll(List<DailyActivities> dailyActivities);



}
