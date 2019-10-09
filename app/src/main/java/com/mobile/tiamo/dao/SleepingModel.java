package com.mobile.tiamo.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 SleepingModel, using to store information in SQLlite
 */
@Entity
public class SleepingModel {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "wakeupTime")
    private String wakeupTime;

    @ColumnInfo(name = "isStorage")
    private int isStorage;

    public SleepingModel() {
    }

    public SleepingModel(long id, String date, String time, int isStorage) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.isStorage = isStorage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsStorage() {
        return isStorage;
    }

    public void setIsStorage(int isStorage) {
        this.isStorage = isStorage;
    }

    public String getWakeupTime() {
        return wakeupTime;
    }

    public void setWakeupTime(String wakeupTime) {
        this.wakeupTime = wakeupTime;
    }
}

