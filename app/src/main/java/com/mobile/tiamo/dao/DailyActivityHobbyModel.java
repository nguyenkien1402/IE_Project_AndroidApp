package com.mobile.tiamo.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DailyActivityHobbyModel {

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

    @ColumnInfo(name = "day_created")
    private String dateCreated;

    @ColumnInfo(name = "isStorage")
    private int isStorage;

    @ColumnInfo(name = "target")
    private float targetHour;


    public DailyActivityHobbyModel() {
    }

    public DailyActivityHobbyModel(long uid, String title, int hours, int dayPerWeek, int isHighPriority, int minutes, String dateCreated) {
        this.uid = uid;
        this.title = title;
        this.hours = hours;
        this.dayPerWeek = dayPerWeek;
        this.isHighPriority = isHighPriority;
        this.minutes = minutes;
        this.dateCreated = dateCreated;
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getIsStorage() {
        return isStorage;
    }

    public void setIsStorage(int isStorage) {
        this.isStorage = isStorage;
    }

    public float getTargetHour() {
        return targetHour;
    }

    public void setTargetHour(float targetHour) {
        this.targetHour = targetHour;
    }
}
