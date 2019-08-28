package com.mobile.tiamo.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DailyActivities {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "day")
    private String day;

    @ColumnInfo(name = "schedule_id")
    private int scheduleId;

    public DailyActivities() {
    }

    public DailyActivities(int uid, String day, int scheduleId) {
        this.uid = uid;
        this.day = day;
        this.scheduleId = scheduleId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }
}
