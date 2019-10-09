package com.mobile.tiamo.adapters;

/**
 Class for dashboard sleeping adapter
 This is item to populate the list
 **/
public class DashboardSleepingItem {
    private String day;
    private String inBed;
    private String wakeUp;
    private String avg;

    public DashboardSleepingItem() {
    }

    public DashboardSleepingItem(String day, String inBed, String wakeUp, String avg) {
        this.day = day;
        this.inBed = inBed;
        this.wakeUp = wakeUp;
        this.avg = avg;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getInBed() {
        return inBed;
    }

    public void setInBed(String inBed) {
        this.inBed = inBed;
    }

    public String getWakeUp() {
        return wakeUp;
    }

    public void setWakeUp(String wakeUp) {
        this.wakeUp = wakeUp;
    }

    public String getAvg() {
        return avg;
    }

    public void setAvg(String avg) {
        this.avg = avg;
    }
}
