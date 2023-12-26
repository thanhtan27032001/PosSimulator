package com.example.creditcardsimulator2.api_instance;

import com.example.creditcardsimulator2.api.ApiUrl;
import com.example.creditcardsimulator2.api.PayApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PayApiInstance {
    private static PayApi instance;
    public static PayApi getInstance(){
        if (instance == null){
            instance = new Retrofit.Builder()
                    .baseUrl(ApiUrl.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(PayApi.class);
        }
        return instance;
    }
}
