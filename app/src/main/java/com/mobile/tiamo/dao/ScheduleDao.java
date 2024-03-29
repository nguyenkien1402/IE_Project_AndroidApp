package com.mobile.tiamo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 ScheduleDao DAO interface, using to manipulate the data
 **/
@Dao
public interface ScheduleDao {

    @Query("Select * from schedule")
    List<Schedule> getAll();

    @Insert
    long insert(Schedule schedule);

    @Insert
    void insertAll(List<Schedule> schedules);

    @Query("SELECT * FROM schedule where specific_day LIKE :day")
    List<Schedule> getScheduleByDay(String day);

    @Query("SELECT * FROM schedule where uid = :uid")
    Schedule getScheduleById(int uid);

    @Query("SELECT * FROM schedule WHERE day_created = :date")
    List<Schedule> getListAddByDate(String date);

    @Query("DELETE FROM Schedule")
    void deleteAll();
}
