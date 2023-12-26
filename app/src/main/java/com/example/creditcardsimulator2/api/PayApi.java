package com.example.creditcardsimulator2.api;


import com.example.creditcardsimulator2.model.Transaction;
import com.example.creditcardsimulator2.model.TransactionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PayApi {
    @POST("/api/v1/pay/model2")
    Call<TransactionResponse> payTransaction(@Body Transaction transaction);

    @POST("/api/v1/pay")
    Call<Integer> payTransaction();
}
