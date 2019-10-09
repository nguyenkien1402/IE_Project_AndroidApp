package com.mobile.tiamo.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 Activity Model, using to store information in SQLlite
 **/
@Entity
public class ActivitiesModel {

    @PrimaryKey(autoGenerate = true)
    private long uid;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "hours")
    private int hours;

    @ColumnInfo(name ="day_per_week")
    private int dayPerWeek;

    @ColumnInfo(name = "is_priority")
    private int isHighPriority;

    @ColumnInfo(name = "minutes")
    private int minutes;

    public ActivitiesModel(String title, int hours){
        this.title = title;
        this.hours = hours;
    }

    public ActivitiesModel(String title, int hours, int dayPerWeek) {
        this.title = title;
        this.hours = hours;
        this.dayPerWeek = dayPerWeek;
    }

    public ActivitiesModel() {
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getDayPerWeek() {
        return dayPerWeek;
    }

    public void setDayPerWeek(int dayPerWeek) {
        this.dayPerWeek = dayPerWeek;
    }

    public int getIsHighPriority() {
        return isHighPriority;
    }

    public void setIsHighPriority(int isHighPriority) {
        this.isHighPriority = isHighPriority;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
