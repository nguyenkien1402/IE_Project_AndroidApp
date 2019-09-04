package com.mobile.tiamo.adapters;

import androidx.room.ColumnInfo;

public class ActivityModelItem {
    private long uid;
    private String title;
    private int hours;
    private int dayPerWeek;
    private int isHighPriority;

    public ActivityModelItem() {
    }

    public ActivityModelItem(long uid, String title, int hours, int dayPerWeek, int isHighPriority) {
        this.uid = uid;
        this.title = title;
        this.hours = hours;
        this.dayPerWeek = dayPerWeek;
        this.isHighPriority = isHighPriority;
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
}
