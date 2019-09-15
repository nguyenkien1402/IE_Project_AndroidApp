package com.mobile.tiamo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SleepingModelDao {

    @Query("SELECT * FROM SleepingModel")
    List<SleepingModel> getAll();

    @Query("SELECT * FROM SleepingModel WHERE date = :date")
    List<SleepingModel> getSleepingByDate(String date);

    @Insert
    long insert(SleepingModel sleepingModel);

    @Update
    void update(SleepingModel sleepingModel);

    @Query("SELECT * FROM SleepingModel WHERE date =:date ORDER BY id DESC LIMIT 1")
    SleepingModel getTheNewest(String date);

    @Query("SELECT * FROM SleepingModel WHERE isStorage = 0")
    List<SleepingModel> getSleepingUnStorage();

    @Query("UPDATE SleepingModel SET isStorage = 1 WHERE isStorage = 0")
    void updateStorageSleeping();

}
