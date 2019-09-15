package com.mobile.tiamo.rest.services;

import com.mobile.tiamo.models.SleepingTime;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ISleepingTime {

    @POST(RestServiceApiURL.XBRAIN_SLEEPINGTIME)
    Call<SleepingTime> insert(@Query("userId") int userId, @Body SleepingTime sleepingTime);
}
