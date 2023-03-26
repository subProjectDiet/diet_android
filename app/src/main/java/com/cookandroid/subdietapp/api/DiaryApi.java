package com.cookandroid.subdietapp.api;

import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.diary.Diary;
import com.cookandroid.subdietapp.model.diary.DiaryRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface DiaryApi {

    // 몸무게 입력
    @POST("/diary")
    Call<Res> addDiaryWeight(@Header("Authorization") String token,
                        @Body Diary diary);

    // 몸무게 수정
    @PUT("/diary")
    Call<Res> updateDiaryWeight(@Header("Authorization") String token,
                             @Body Diary diary);

    // 유저가 특정날에 입력한 몸무게 데이터 가져오기
    @GET("diary")
    Call<DiaryRes> setDiaryWeight(@Header("Authorization") String token,
                                  @Query("date") String date);




}
