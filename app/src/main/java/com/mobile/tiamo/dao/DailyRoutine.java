package com.mobile.tiamo.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DailyRoutine {

    @PrimaryKey(autoGenerate = true)
    private long uid;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "schedule_id")
    private long scheduleId;

    @ColumnInfo(name = "is_done")
    private int isDone;

    @ColumnInfo(name = "hours")
    private String hours;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "time_start")
    private String timeStart;

    @ColumnInfo(name = "time_end")
    private String timeEnd;

    @ColumnInfo(name = "time_actually_end")
    private String timeActuallyEnd;

    @ColumnInfo(name = "dayOperation")
    private String dayOperation;

    @ColumnInfo(name = "isStorage")
    private int isStorage;

    public DailyRoutine() {
    }

    public DailyRoutine(long uid, String date, int scheduleId, int isDone, String hours, String title) {
        this.uid = uid;
        this.date = date;
        this.scheduleId = scheduleId;
        this.isDone = isDone;
        this.hours = hours;
        this.title = title;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getDay() {
        return date;
    }

    public void setDay(String day) {
        this.date = day;
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
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

    public String getTimeActuallyEnd() {
        return timeActuallyEnd;
    }

    public void setTimeActuallyEnd(String timeActuallyEnd) {
        this.timeActuallyEnd = timeActuallyEnd;
    }

    public int getIsStorage() {
        return isStorage;
    }

    public void setIsStorage(int isStorage) {
        this.isStorage = isStorage;
    }

    public String getDayOperation() {
        return dayOperation;
    }

    public void setDayOperation(String dayOperation) {
        this.dayOperation = dayOperation;
    }
}
