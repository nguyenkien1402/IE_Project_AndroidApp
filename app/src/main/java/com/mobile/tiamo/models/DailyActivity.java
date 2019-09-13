package com.mobile.tiamo.models;

import com.google.gson.annotations.SerializedName;

public class DailyActivity {
    @SerializedName("id")
    private Integer id;
    @SerializedName("userId")
    private Integer userId;
    @SerializedName("activityTitle")
    private String activityTitle;
    @SerializedName("activityContent")
    private String activityContent;
    @SerializedName("dateAchieve")
    private String dateAchieve;
    @SerializedName("numberOfHour")
    private int numberOfHour;
    @SerializedName("targetHour")
    private int targetHour;

    public DailyActivity() {
    }

    public DailyActivity(Integer id, Integer userId, String activityTitle, String activityContent, String dateAchieve, int numberOfHour, int targetHour) {
        this.id = id;
        this.userId = userId;
        this.activityTitle = activityTitle;
        this.activityContent = activityContent;
        this.dateAchieve = dateAchieve;
        this.numberOfHour = numberOfHour;
        this.targetHour = targetHour;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public String getDateAchieve() {
        return dateAchieve;
    }

    public void setDateAchieve(String dateAchieve) {
        this.dateAchieve = dateAchieve;
    }

    public int getNumberOfHour() {
        return numberOfHour;
    }

    public void setNumberOfHour(int numberOfHour) {
        this.numberOfHour = numberOfHour;
    }

    public int getTargetHour() {
        return targetHour;
    }

    public void setTargetHour(int targetHour) {
        this.targetHour = targetHour;
    }
}
