package com.mobile.tiamo.adapters;

public class DailyActivityItem {
    private String title;
    private String hours;
    private String days;
    private int isDone;

    public DailyActivityItem(){

    }

    public DailyActivityItem(String title, String hours, String days, int isDone) {
        this.title = title;
        this.hours = hours;
        this.days = days;
        this.isDone = isDone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }
}
