package com.mobile.tiamo.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;



import java.util.List;

@Dao
public interface TasksDao {

    @Query("SELECT * FROM tasks")
    List<Tasks> getAll();

    @Insert
    long insert(Tasks task);

}
