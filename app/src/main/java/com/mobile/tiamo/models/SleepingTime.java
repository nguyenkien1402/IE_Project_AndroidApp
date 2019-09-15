package com.mobile.tiamo.models;


import com.google.gson.annotations.SerializedName;

public class SleepingTime {
    @SerializedName("id")
    private int id;
    @SerializedName("userId")
    private int userId;
    @SerializedName("dateAchieve")
    private String dateAchieve;
    @SerializedName("sleepingTime")
    private String sleepingTime;

    public SleepingTime() {
    }

    public SleepingTime(int id, int userId, String dateAchieve, String sleepingTime) {
        this.id = id;
        this.userId = userId;
        this.dateAchieve = dateAchieve;
        this.sleepingTime = sleepingTime;
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
        return sleepingTime;
    }

    public void setSleepingTime(String sleepingTime) {
        this.sleepingTime = sleepingTime;
    }
}
