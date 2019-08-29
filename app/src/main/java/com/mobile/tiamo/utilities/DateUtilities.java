package com.mobile.tiamo.utilities;

import java.text.DateFormat;
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

    public static Date getCurrentDateWithTime(){
        Date date =java.util.Calendar.getInstance().getTime();
        return date;
    }

    public static String getCurrentDayInAbb(){
        Date now = new Date();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
        return simpleDateformat.format(now);
    }

}
