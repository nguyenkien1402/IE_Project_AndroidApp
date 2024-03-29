package com.mobile.tiamo.rest.services;

import com.mobile.tiamo.models.XbrainUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IXbrainUser {

    @GET(RestServiceApiURL.XBRAIN_USER_CREATE)
    Call<List<XbrainUser>> getAllUser();

    @POST(RestServiceApiURL.XBRAIN_USER_CREATE)
    Call<XbrainUser> register(@Body XbrainUser x);

}
