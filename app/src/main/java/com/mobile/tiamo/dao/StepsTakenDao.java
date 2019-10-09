package com.mobile.tiamo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/*
  a steotakens dao, using to manipulate the stepstaken model
 */
@Dao
public interface StepsTakenDao {

    @Insert
    long insert(StepsTakenModel stepsTakenModel);

    @Insert
    void insertAll(List<StepsTakenModel> list);

    @Query("SELECT * FROM StepsTakenModel")
    List<StepsTakenModel> getAll();

    @Query("SELECT * FROM StepsTakenModel WHERE date = :date")
    StepsTakenModel getStepTakenByDate(String date);

    @Query("DELETE FROM StepsTakenModel")
    void deleteAll();

    @Query("SELECT * FROM StepsTakenModel WHERE date BETWEEN :date1 and :date2")
    List<StepsTakenModel> getStepsTakenInrange(String date1, String date2);

    @Update
    void update(StepsTakenModel model);
}