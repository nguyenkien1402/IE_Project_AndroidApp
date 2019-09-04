package com.mobile.tiamo.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ActivitiesModel {

    @PrimaryKey(autoGenerate = true)
    private long uid;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "hours")
    private int hours;

    public ActivitiesModel(long uid, String title, int hours) {
        this.title = title;
        this.hours = hours;
        this.uid = uid;
    }

    public ActivitiesModel(String title, int hours) {
        this.title = title;
        this.hours = hours;
    }

    public ActivitiesModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
