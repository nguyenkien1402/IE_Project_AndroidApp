package com.mobile.tiamo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 SleepingModelDao DAO interface, using to manipulate the data
 **/
@Dao
public interface SleepingModelDao {

    @Query("SELECT * FROM SleepingModel")
    List<SleepingModel> getAll();

    @Query("SELECT * FROM SleepingModel WHERE date = :date")
    List<SleepingModel> getSleepingByDate(String date);

    @Insert
    long insert(SleepingModel sleepingModel);

    @Insert
    void insertAll(List<SleepingModel> sleepingModels);

    @Update
    void update(SleepingModel sleepingModel);

    @Query("SELECT * FROM SleepingModel WHERE date =:date ORDER BY id DESC LIMIT 1")
    SleepingModel getTheNewest(String date);

    @Query("SELECT * FROM SleepingModel WHERE isStorage = 0")
    List<SleepingModel> getSleepingUnStorage();

    @Query("UPDATE SleepingModel SET isStorage = 1 WHERE isStorage = 0")
    void updateStorageSleeping();

    @Query("DELETE FROM SleepingModel")
    void deleteAll();

    @Query("SELECT * FROM sleepingmodel WHERE date BETWEEN :date1 AND :date2")
    List<SleepingModel> getLastTenDay(String date1, String date2);

}
