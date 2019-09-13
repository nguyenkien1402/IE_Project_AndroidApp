package com.mobile.tiamo.rest.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitService(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(RestServiceApiURL.XBRAIN_URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
