package com.mobile.tiamo.dao;

import android.content.Context;

import androidx.room.Room;

public class SQLiteDatabase {
    public static TiamoDatabase getTiamoDatabase(Context context){
        TiamoDatabase tiamoDatabase = Room.databaseBuilder(context, TiamoDatabase.class,
                                        "TiamoDatabase").fallbackToDestructiveMigration().build();
        return tiamoDatabase;
    }
}
