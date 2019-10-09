package com.mobile.tiamo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
/**
 Activity Model DAO interface, using to manipulate the data
 **/
@Dao
public interface ActivitiesModelDao {

    @Query("Select * From ActivitiesModel")
    List<ActivitiesModel> getAll();

    @Insert
    long insert(ActivitiesModel activitiesModel);

    @Insert
    void insertAll(List<ActivitiesModel> activitiesModels);

    @Update()
    void update(ActivitiesModel activitiesModels);

    @Query("DELETE FROM ActivitiesModel")
    void deleteAll();

}
