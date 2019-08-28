package com.mobile.tiamo.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Schedule {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name="time_start")
    private String timeStart;

    @ColumnInfo(name="time_end")
    private String timeEnd;

    @ColumnInfo(name="hours")
    private double hours;

    @ColumnInfo(name="operation_day")
    private String operationDay;

    public Schedule() {
    }

    public Schedule(int uid, String timeStart, String timeEnd, double hours, String operationDay) {
        this.uid = uid;
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

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public String getOperationDay() {
        return operationDay;
    }

    public void setOperationDay(String operationDay) {
        this.operationDay = operationDay;
    }
}
