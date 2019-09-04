package com.mobile.tiamo.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DailyActivitiesModel {

    @PrimaryKey(autoGenerate = true)
    private long uid;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "hour_participate")
    private double hourParticipate;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "day")
    private String day;

    @ColumnInfo(name = "time_start")
    private String timeStart;

    @ColumnInfo(name = "time_end")
    private String timeEnd;

    public DailyActivitiesModel() {
    }

    public DailyActivitiesModel(long uid, String title, double hourParticipate, String date, String day, String timeStart, String timeEnd) {
        this.uid = uid;
        this.title = title;
        this.hourParticipate = hourParticipate;
        this.date = date;
        this.day = day;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
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

    public double getHourParticipate() {
        return hourParticipate;
    }

    public void setHourParticipate(double hourParticipate) {
        this.hourParticipate = hourParticipate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }
}
