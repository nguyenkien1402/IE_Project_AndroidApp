package com.mobile.tiamo.dao;

import android.content.Context;

import androidx.room.Room;

/**
 an static method to create the database
 **/
public class SQLiteDatabase {
    public static TiamoDatabase getTiamoDatabase(Context context){
        TiamoDatabase tiamoDatabase = Room.databaseBuilder(context, TiamoDatabase.class,
                                        "TiamoDatabase").fallbackToDestructiveMigration().build();
        return tiamoDatabase;
    }
}
