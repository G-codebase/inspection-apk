package com.example.myapss.network;

import com.example.myapss.models.LoginRequest;
import com.example.myapss.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApi {

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}