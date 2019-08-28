package com.mobile.tiamo.adapters;

public class ScheduleModel {

    private String title;
    private String hours;
    private String days;

    public ScheduleModel() {
    }

    public ScheduleModel(String title, String hours, String days) {
        this.title = title;
        this.hours = hours;
        this.days = days;
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
}
