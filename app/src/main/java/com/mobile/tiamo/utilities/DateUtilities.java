package com.mobile.tiamo.utilities;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtilities {

    public static String getCurrentDateInString(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static String convertDateFormat(String date){
        try {
            Date d1 = new SimpleDateFormat("dd-MM-yyyy").parse("13-09-2019");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String str = format.format(d1);
            return str;
        }catch (Exception e){
            e.getMessage();
        }
        return null;
    }

    public static Date getCurrentDateWithTime(){
        Date date =java.util.Calendar.getInstance().getTime();
        return date;
    }

    public static String getCurrentDayInAbb(){
        Date now = new Date();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
        return simpleDateformat.format(now);
    }

    public static String getDayInAbbBySelectedDate(String date){
        try{
            SimpleDateFormat simpleDateformat = new SimpleDateFormat("dd-MM-yyyy");
            Date d = simpleDateformat.parse(date);
            SimpleDateFormat parse = new SimpleDateFormat("E");
            return parse.format(d);
        }catch (ParseException e){
            Log.d("DateUtility",e.getMessage());
        }
        return null;
    }
    public static Date stringToDate(String date){
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date d = simpleDateFormat.parse(date);
            return d;
        }catch (ParseException e){
            Log.d("DateUtitlity",e.getMessage());
        }
        return null;

    }

}
