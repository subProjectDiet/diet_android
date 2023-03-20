package com.cookandroid.subdietapp.api;

import com.cookandroid.subdietapp.model.User;
import com.cookandroid.subdietapp.model.UserRes;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi extends Serializable {

    @POST("/user/register")
    Call<UserRes> register(@Body User user); // UserRes에 리턴하라 // Body에는 user 객체가 들어간다
}
