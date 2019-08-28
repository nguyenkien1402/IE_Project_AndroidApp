package com.mobile.tiamo.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Schedule {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name="title")
    private String title;

    @ColumnInfo(name="time_start")
    private String timeStart;

    @ColumnInfo(name="time_end")
    private String timeEnd;

    @ColumnInfo(name="hours")
    private String hours;

    @ColumnInfo(name="operation_day")
    private String operationDay;

    public Schedule() {
    }

    public Schedule(int uid, String title, String timeStart, String timeEnd, String hours, String operationDay) {
        this.uid = uid;
        this.title = title;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.hours = hours;
        this.operationDay = operationDay;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getOperationDay() {
        return operationDay;
    }

    public void setOperationDay(String operationDay) {
        this.operationDay = operationDay;
    }
}
