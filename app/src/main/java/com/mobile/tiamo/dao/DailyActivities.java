package com.mobile.tiamo.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DailyActivities {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "schedule_id")
    private int scheduleId;

    @ColumnInfo(name = "is_done")
    private int isDone;

    @ColumnInfo(name = "hours")
    private String hours;

    @ColumnInfo(name = "title")
    private String title;

    public DailyActivities() {
    }

    public DailyActivities(int uid, String date, int scheduleId, int isDone, String hours, String title) {
        this.uid = uid;
        this.date = date;
        this.scheduleId = scheduleId;
        this.isDone = isDone;
        this.hours = hours;
        this.title = title;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDay() {
        return date;
    }

    public void setDay(String day) {
        this.date = day;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
