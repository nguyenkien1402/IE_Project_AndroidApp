package com.mobile.tiamo.models;


import com.google.gson.annotations.SerializedName;

public class SleepingTime {
    @SerializedName("id")
    private int id;
    @SerializedName("userId")
    private int userId;
    @SerializedName("dateAchieve")
    private String dateAchieve;
    @SerializedName("sleepTime")
    private String sleepTime;
    @SerializedName("wakingupTime")
    private String wakingupTime;

    public SleepingTime() {
    }

    public SleepingTime(int id, int userId, String dateAchieve, String sleepTime) {
        this.id = id;
        this.userId = userId;
        this.dateAchieve = dateAchieve;
        this.sleepTime = sleepTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDateAchieve() {
        return dateAchieve;
    }

    public void setDateAchieve(String dateAchieve) {
        this.dateAchieve = dateAchieve;
    }

    public String getSleepingTime() {
        return sleepTime;
    }

    public void setSleepingTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }

    public String getWakingupTime() {
        return wakingupTime;
    }

    public void setWakingupTime(String wakingupTime) {
        this.wakingupTime = wakingupTime;
    }
}
