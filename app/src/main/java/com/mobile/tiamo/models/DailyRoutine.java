package com.mobile.tiamo.models;

import com.google.gson.annotations.SerializedName;

public class DailyRoutine {

    @SerializedName("id")
    private Integer id;
    @SerializedName("userId")
    private Integer userId;
    @SerializedName("routineTitle")
    private String routineTitle;
    @SerializedName("routineContent")
    private String routineContent;
    @SerializedName("dayOperation")
    private String dayOperation;
    @SerializedName("dateAchieve")
    private String dateAchieve;
    @SerializedName("planStartTime")
    private String planStartTime;
    @SerializedName("planEndTime")
    private String planEndTime;
    @SerializedName("actualStartTime")
    private String actualStartTime;
    @SerializedName("actualEndTime")
    private String actualEndTime;

    public DailyRoutine() {
    }

    public DailyRoutine(Integer id, Integer userId, String routineTitle, String routineContent, String dayOperation, String dateAchieve, String planStartTime, String planEndTime, String actualStartTime, String actualEndTime) {
        this.id = id;
        this.userId = userId;
        this.routineTitle = routineTitle;
        this.routineContent = routineContent;
        this.dayOperation = dayOperation;
        this.dateAchieve = dateAchieve;
        this.planStartTime = planStartTime;
        this.planEndTime = planEndTime;
        this.actualStartTime = actualStartTime;
        this.actualEndTime = actualEndTime;
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

    public String getRoutineTitle() {
        return routineTitle;
    }

    public void setRoutineTitle(String routineTitle) {
        this.routineTitle = routineTitle;
    }

    public String getRoutineContent() {
        return routineContent;
    }

    public void setRoutineContent(String routineContent) {
        this.routineContent = routineContent;
    }

    public String getDayOperation() {
        return dayOperation;
    }

    public void setDayOperation(String dayOperation) {
        this.dayOperation = dayOperation;
    }

    public String getDateAchieve() {
        return dateAchieve;
    }

    public void setDateAchieve(String dateAchieve) {
        this.dateAchieve = dateAchieve;
    }

    public String getPlanStartTime() {
        return planStartTime;
    }

    public void setPlanStartTime(String planStartTime) {
        this.planStartTime = planStartTime;
    }

    public String getPlanEndTime() {
        return planEndTime;
    }

    public void setPlanEndTime(String planEndTime) {
        this.planEndTime = planEndTime;
    }

    public String getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(String actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public String getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(String actualEndTime) {
        this.actualEndTime = actualEndTime;
    }
}
