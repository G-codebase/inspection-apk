package com.example.myapss.network;

import com.example.myapss.models.InspectionRequest;
import com.example.myapss.models.InspectionResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    @Multipart
    @POST("inspection/upload-photo")
    Call<ResponseBody> uploadPhoto(
            @Part MultipartBody.Part file,
            @Part("inspectionId") RequestBody inspectionId
    );

    @Multipart
    @POST("api/attachments/upload")
    Call<ResponseBody> uploadFile(
            @Part("inspectionId") String inspectionId,
            @Part("fieldName") String fieldName,
            @Part MultipartBody.Part file
    );

    @POST("inspection/create")
    Call<ResponseBody> createInspection(@Body InspectionRequest request);

    @GET("inspection/{id}")
    Call<InspectionResponse> getInspection(@Path("id") int id);
}