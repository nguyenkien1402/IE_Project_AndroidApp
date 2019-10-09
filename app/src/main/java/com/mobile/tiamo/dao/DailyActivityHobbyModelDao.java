package com.mobile.tiamo.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 DailyActivityHobbyModelDao DAO interface, using to manipulate the data
 **/
@Dao
public interface DailyActivityHobbyModelDao {

    @Query("SELECT * FROM DailyActivityHobbyModel")
    List<DailyActivityHobbyModel> getAll();

    @Insert
    void insert(DailyActivityHobbyModel dailyActivityHobbyModel);

    @Insert
    void insertAll(List<DailyActivityHobbyModel> dailyActivityHobbyModels);

    @Query("SELECT * FROM DailyActivityHobbyModel WHERE uid = :uid")
    DailyActivityHobbyModel getById(long uid);

    @Query("SELECT * FROM DailyActivityHobbyModel WHERE day_created = :day")
    List<DailyActivityHobbyModel> getDailyActivityHobbyByDate(String day);

    @Query("SELECT * FROM DailyActivityHobbyModel WHERE isStorage = 0")
    List<DailyActivityHobbyModel> getDailyActivityHobbyUnStorage();

    @Update
    void update(DailyActivityHobbyModel dailyActivityHobbyModel);

    @Query("SELECT * FROM DailyActivityHobbyModel WHERE title = :title AND day_created = :date")
    List<DailyActivityHobbyModel> checkIfExists(String title,String date);

    @Query("UPDATE DailyActivityHobbyModel SET isStorage = 1 WHERE isStorage = 0")
    void updateStorage();

    @Query("DELETE FROM DailyActivityHobbyModel")
    void deleteAll();
}
