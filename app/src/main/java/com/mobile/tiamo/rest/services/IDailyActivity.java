package com.mobile.tiamo.rest.services;

import com.mobile.tiamo.models.DailyActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IDailyActivity {

    @GET(RestServiceApiURL.XBRAIN_DAILYACTIVITY +"/byuser")
    Call<List<DailyActivity>> getActivityByUserIdAndDate(@Query("id") int id, @Query("date") String date);

    @POST(RestServiceApiURL.XBRAIN_DAILYACTIVITY)
    Call<DailyActivity> insert(@Query("userId") int userId,@Body DailyActivity da);
}
