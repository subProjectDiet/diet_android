package com.cookandroid.subdietapp.api;

import com.cookandroid.subdietapp.model.food.VisionRes;

import java.io.Serializable;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface VsionApi extends Serializable {
    @Multipart
    @POST("/vision")
    Call<VisionRes> visionResult(@Part MultipartBody.Part photoFile);

}
