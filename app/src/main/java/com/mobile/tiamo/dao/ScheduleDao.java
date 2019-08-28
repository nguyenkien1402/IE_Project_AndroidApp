package com.mobile.tiamo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Query("Select * from schedule")
    List<Schedule> getAll();

    @Insert
    long insert(Schedule schedule);

}
