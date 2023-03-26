package com.cookandroid.subdietapp.api;

import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.user.User;
import com.cookandroid.subdietapp.model.user.UserInfo;
import com.cookandroid.subdietapp.model.user.UserRes;
import com.cookandroid.subdietapp.model.user.UserTarget;

import java.io.Serializable;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserApi extends Serializable {

    // 회원가입
    @POST("/user/register")
    Call<UserRes> register(@Body User user); // UserRes에 리턴하라 // Body에는 user 객체가 들어간다

    // 유저 추가 정보
    @POST("/user/info")
    Call<Res> registerInfo(@Header("Authorization") String token,
                      @Body UserInfo userInfo);

    // 유저 목표 정보
    @POST("/user/target")
    Call<Res> registerTarget(@Header("Authorization") String token,
                             @Body UserTarget userTarget);
    // 로그아웃
    @POST("/user/logout")
    Call<Res> logout(@Header("Authorization") String token);


    // 닉네임 수정
    @PUT("/user/edit/nickname")
    Call<Res> editNickname(@Header("Authorization") String token,
                           @Body Map<String, String> nicknameMap);

}
