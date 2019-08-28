package com.mobile.tiamo.dao;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Tasks {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name="title")
    public String title;

    public Tasks() {
    }

    public Tasks(String title) {
        this.title = title;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
