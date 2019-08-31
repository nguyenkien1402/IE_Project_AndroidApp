package com.mobile.tiamo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("SELECT * FROM DAILYACTIVITIES WHERE title = :txt")
    DailyActivities getDailyActivityByTitle(String txt);

    @Query("SELECT * FROM DailyActivities WHERE schedule_id = :scheduleId")
    DailyActivities checkIfAlreadyExistDailyActivity(long scheduleId);

    @Update()
    void update(DailyActivities dailyActivities);

    @Query("SELECT * FROM DailyActivities WHERE uid = :uid")
    DailyActivities getDailyActivityById(long uid);


}
