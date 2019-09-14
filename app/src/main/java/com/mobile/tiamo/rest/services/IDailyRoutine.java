package com.mobile.tiamo.rest.services;

import com.mobile.tiamo.models.DailyRoutine;
import com.mobile.tiamo.rest.services.RestServiceApiURL;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IDailyRoutine {

    @GET(RestServiceApiURL.XBRAIN_DAILYROUTINE)
    Call<List<DailyRoutine>> getAll();

    @POST(RestServiceApiURL.XBRAIN_DAILYROUTINE)
    Call<DailyRoutine> insert(@Query("userId") int userId, @Body DailyRoutine dr);


}
