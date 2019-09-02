package com.mobile.tiamo.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SavingDataSharePreference {

    public static void savingLocalData(Context context,String db, String key, String value){
        SharedPreferences preferences = context.getSharedPreferences(db, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static void savingLocalData(Context context,String db, String key, int value){
        SharedPreferences preferences = context.getSharedPreferences(db, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    public static void savingLocalData(Context context,String db, String key, boolean value){
        SharedPreferences preferences = context.getSharedPreferences(db, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }


    public static String getDataString(Context context, String db, String key){
        SharedPreferences preferences = context.getSharedPreferences(db,Context.MODE_PRIVATE);
        return preferences.getString(key,"");
    }

    public static int getDataInt(Context context, String db, String key){
        SharedPreferences preferences = context.getSharedPreferences(db,Context.MODE_PRIVATE);
        return preferences.getInt(key,-1);
    }

    public static boolean getDataBoolean(Context context, String db, String key){
        SharedPreferences preferences = context.getSharedPreferences(db,Context.MODE_PRIVATE);
        return preferences.getBoolean(key,false);
    }
}
