package com.mobile.tiamo.rest.services;

public class RestServiceApiURL {

    public static String IP_SERVER = "http://liberzy.azurewebsites.net/";
    public static String RECOMMENDATION_SERVER = "https://ie-project-ml.appspot.com";
    public static String XBRAIN_URL_BASE = IP_SERVER +"api/";
    public static final String XBRAIN_USER_CREATE = "XbrainUsers";
    public static final String XBRAIN_DAILYACTIVITY =  "DailyActivities";
    public static final String XBRAIN_DAILYROUTINE =  "DailyRoutines";
    public static final String XBRAIN_SLEEPINGTIME = "SleepingTimes";

    public static final String SEARCH_MOVIE_TITLE = "http://www.omdbapi.com/?apikey=b01ac3d3&s=";
    public static final String MOVIE_DESCRIPTION = "http://www.omdbapi.com/?apikey=b01ac3d3&i=";

    public static final String MOVIE_RECOMMENDATION = RECOMMENDATION_SERVER + "/api/hybrid_type/";
    public static final String MOVIE_RECOMMENDATION_NOTITLE = RECOMMENDATION_SERVER+ "/api/collaborative/";
    public static final String GET_ALL_MOVIES = RECOMMENDATION_SERVER + "/api/movies";

}
