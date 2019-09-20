package com.mobile.tiamo.utilities;

import com.mobile.tiamo.R;

import java.text.DecimalFormat;
import java.util.HashMap;
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
            default:
                id = R.drawable.default_activity;
                break;
        }
        return id;
    }
}
