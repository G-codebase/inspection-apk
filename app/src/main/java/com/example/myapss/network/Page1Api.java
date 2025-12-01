package com.example.myapss.network;

import com.example.myapss.models.Page1Request;
import com.example.myapss.models.Page1Response;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Page1Api {

    @POST("api/page1/save")
    Call<Page1Response> savePage1(@Body Page1Request request);
}