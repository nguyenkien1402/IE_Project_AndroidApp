package com.mobile.tiamo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 DailyRoutineDao DAO interface, using to manipulate the data
 **/
@Dao
public interface DailyRoutineDao {

    @Query("Select * From DailyRoutine")
    List<DailyRoutine> getAll();

    @Insert
    long insert(DailyRoutine dailyRoutine);

    @Query("Select * from DailyRoutine WHERE date = :date")
    List<DailyRoutine> getDailyActivities(String date);

    @Insert
    void insertAll(List<DailyRoutine> dailyActivities);

    @Query("SELECT * FROM DailyRoutine WHERE title = :txt")
    DailyRoutine getDailyActivityByTitle(String txt);

    @Query("SELECT * FROM DailyRoutine WHERE schedule_id = :scheduleId")
    DailyRoutine checkIfAlreadyExistDailyActivity(long scheduleId);

    @Update()
    void update(DailyRoutine dailyRoutine);

    @Query("SELECT * FROM DailyRoutine WHERE uid = :uid")
    DailyRoutine getDailyActivityById(long uid);

    @Query("UPDATE DailyRoutine SET is_done = :isDone WHERE uid = :uid")
    void updateIsDone(long uid, int isDone);


    @Query("SELECT * FROM DailyRoutine  WHERE isStorage = 0")
    List<DailyRoutine> getDailyRoutineUnUpdate();

    @Query("UPDATE DailyRoutine SET isStorage = 1 WHERE isStorage = 0")
    void updateStorage();

    @Query("DELETE FROM DailyRoutine")
    void deleteAll();


}
