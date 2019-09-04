package com.mobile.tiamo.utilities;

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
}
