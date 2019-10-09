package com.mobile.tiamo.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
  a step taken model, using to store the data to SQLlite
 **/
@Entity
public class StepsTakenModel {

    @PrimaryKey(autoGenerate = true)
    private long uid;

    @ColumnInfo(name = "step")
    private int steps;

    @ColumnInfo(name = "date")
    private String date;

    public StepsTakenModel() {
    }

    public StepsTakenModel(long uid, int steps, String date) {
        this.uid = uid;
        this.steps = steps;
        this.date = date;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
