package com.mobile.tiamo.utilities;

import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.DashboardSleepingItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherUtilities {
    public static Map<String,String> dictonaryMapDayToAbb(){
        Map<String, String> dictionary = new HashMap<String, String>();
        dictionary.put("Monday","Mon");
        dictionary.put("Tuesday","Tue");
        dictionary.put("Wednesday","Wed");
        dictionary.put("Thursday","Thu");
        dictionary.put("Friday","Fri");
        dictionary.put("Saturday","Sat");
        dictionary.put("Sunday","Sun");
        return dictionary;
    }

    public static float floatHour(int hour, int minute){
        DecimalFormat f = new DecimalFormat("##.00");
        float k = (float) minute/60 + hour;
        return Float.parseFloat(f.format(k));
    }

    public static int getIcon(String title){
        int id = 0;
        switch (title){
            case "Gym":
                id = R.drawable.gym_24;
                break;
            case "Reading":
                id = R.drawable.reading_24;
                break;
            case "Running":
                id = R.drawable.running_man_24;
                break;
            case "Hiking":
                id = R.drawable.hiking_24;
                break;
            case "General Exercising":
                id = R.drawable.general_exercise_24;
                break;
            case "Meditation":
                id = R.drawable.yoga_24;
                break;
            case "Learn Cooking":
                id = R.drawable.cooking_24;
                break;
            case "Drawing":
                id = R.drawable.drawing_art_24;
                break;
            case "Yoga":
                id = R.drawable.yoga_24;
                break;
            case "Writing":
                id = R.drawable.writing_24;
                break;
            case "Dancing":
                id = R.drawable.dancing_24;
                break;
            case "Learn Magic":
                id = R.drawable.magic_24;
                break;
            case "Origami":
                id = R.drawable.origami_24;
                break;
            case "Visit Local Museums":
                id = R.drawable.museum_24;
                break;
            case "Photography":
                id = R.drawable.photography_24;
                break;
            case "Cycling":
                id = R.drawable.cycling_24;
                break;
            case "Baking":
                id = R.drawable.baking_24;
                break;
            case "Pottery":
                id = R.drawable.pottery_24;
                break;
            case "Sleeping":
                id = R.drawable.sleeping_32;
                break;
            case "Working":
                id = R.drawable.working_32;
                break;
            default:
                id = R.drawable.default_daily_routine;
                break;
        }
        return id;
    }


    public static String suggestion(String title){
        String s = "Setup time for this activity";
        switch (title){
            case "Gym":
                s = "We recommend 8 hours per week, 1.5 hours per day";
                break;
            case "Reading":
                s = "Depend, however 1 to 2 hours per day is good";
                break;
            case "Meditation":
                s = "Meditation 15 - 30 minutes per days help you reduce stress";
                break;
            case "Cycling":
                s = "2 hours per day and 2 day per week";
                break;
            case "Running":
                s = "Running 30 minutes for each day reduce tension and stress";
                break;
            case "Pottery":
                s = "If it is your hobbies, do as much as you can and enjoy it";
                break;
            case "Baking":
                s = "If it is your hobbies, do as much as you can and enjoy it";
                break;
            case "Photography":
                s = "If it is your hobbies, do as much as you can and enjoy it";
                break;
            case "Drawing":
                s = "If it is your hobbies, do as much as you can and enjoy it";
                break;
            case "Origami":
                s = "If it is your hobbies, do as much as you can and enjoy it";
                break;
            case "Dancing":
                s = "If it is your hobbies, do as much as you can and enjoy it";
                break;
            case "Yoga":
                s = "1 hour per day and 4 day per week";
                break;
            default:
                break;
        }
        return s;
    }


    public static List<DashboardSleepingItem> getSleepings(){
        List<DashboardSleepingItem> items = new ArrayList<DashboardSleepingItem>();
        DashboardSleepingItem item = new DashboardSleepingItem();
        item.setDay("21-09-2019");
        item.setInBed("1:15 AM");
        item.setWakeUp("6:15 AM");
        item.setAvg("5.5");
        items.add(item);

        item = new DashboardSleepingItem();
        item.setDay("20-09-2019");
        item.setInBed("2:15 AM");
        item.setWakeUp("6:56 AM");
        item.setAvg("4.5");
        items.add(item);

        item = new DashboardSleepingItem();
        item.setDay("19-09-2019");
        item.setInBed("0:15 AM");
        item.setWakeUp("6:35 AM");
        item.setAvg("6.5");
        items.add(item);

        item = new DashboardSleepingItem();
        item.setDay("18-09-2019");
        item.setInBed("1:50 AM");
        item.setWakeUp("7:15 AM");
        item.setAvg("6.15");
        items.add(item);

        item = new DashboardSleepingItem();
        item.setDay("17-09-2019");
        item.setInBed("2:15 AM");
        item.setWakeUp("8:25 AM");
        item.setAvg("6.1");
        items.add(item);

        item = new DashboardSleepingItem();
        item.setDay("16-09-2019");
        item.setInBed("2:45 AM");
        item.setWakeUp("7:00 AM");
        item.setAvg("4.5");
        items.add(item);
        return items;
    }
}
