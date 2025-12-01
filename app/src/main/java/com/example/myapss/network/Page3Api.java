package com.example.myapss.network;

import com.example.myapss.models.Page3Response;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface Page3Api {

    @Multipart
    @POST("inspection/page3/{inspectionId}")
    Call<Page3Response> submitFinalInspection(
            @Path("inspectionId") long inspectionId,
            @Part("json") RequestBody jsonBody,
            @PartMap Map<String, List<MultipartBody.Part>> files
    );
}