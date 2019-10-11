package com.mobile.tiamo.rest.services;

import android.util.Log;

import com.google.gson.Gson;
import com.mobile.tiamo.adapters.MovieItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieService {

    private static String TAG = "MovieService";

    public static JSONObject getRecommendationMovie(int userId, String title, String types){
        String url = RestServiceApiURL.MOVIE_RECOMMENDATION+userId+"/"+title+"?types="+types;
        Log.d(TAG,"URL:"+url);
        JSONObject result = null;
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES).readTimeout(5, TimeUnit.MINUTES).build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type","application/json")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                Log.d(TAG,"Go on");
                result = new JSONObject(response.body().string());
                Log.d(TAG,"Wel wel wel");
            }
        }catch (Exception e){
            Log.d(TAG,"Error:"+e.toString());
        }
        return result;
    }

    public static List<MovieItem> searchMovieByTitle(String title, int page){
        List<MovieItem> movieItems = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = RestServiceApiURL.SEARCH_MOVIE_TITLE + title;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type","application/json")
                .build();
        try{
            Response response  = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                movieItems = new ArrayList<MovieItem>();
                JSONArray array = new JSONObject(response.body().string()).getJSONArray("Search");
                for(int i = 0 ; i < array.length();i++){
                    MovieItem item = new Gson().fromJson(array.getJSONObject(i).toString(),MovieItem.class);
                    movieItems.add(item);
                }
            }
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return movieItems;
    }

    public static JSONObject getMovieById(String imdbId){
        JSONObject result = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        String url = RestServiceApiURL.MOVIE_DESCRIPTION + imdbId;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type","application/json")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(response.code() == 200){
                result = new JSONObject(response.body().string());
            }
        }catch (Exception e){
            Log.d(TAG,e.getMessage());
        }
        return result;
    }

}
