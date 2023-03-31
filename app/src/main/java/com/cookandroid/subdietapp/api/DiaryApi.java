package com.cookandroid.subdietapp.api;

import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.diary.Diary;
import com.cookandroid.subdietapp.model.diary.DiaryExerciseBurnRes;
import com.cookandroid.subdietapp.model.diary.DiaryMonthRes;
import com.cookandroid.subdietapp.model.diary.DiaryRes;
import com.cookandroid.subdietapp.model.diary.FoodEatDataRes;
import com.cookandroid.subdietapp.model.diary.TotalKcalRes;
import com.cookandroid.subdietapp.model.diary.UserTargetGetRes;
import com.cookandroid.subdietapp.model.exercise.ExerciseTotalkcalRes;

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

    //유저가 특정날 몸무게-칼로리-운동 가저오는API
    @GET("/diary/month")
    Call<DiaryMonthRes> getDiaryMonth(@Header("Authorization") String token,
                                      @Query("date") String date);


    // 유저의 특정날 섭취 칼로리 가져오는 API
    @GET("/foodRecord/total/kcal")
    Call<TotalKcalRes> getTotalKcal (@Header("Authorization") String token,
                                      @Query("date") String date);

    // 특정날 소모한 칼로리 합 가져오기(운동 칼로리 데이터)
    @GET("/exercise/date")
    Call<DiaryExerciseBurnRes> getExerciseBurnTotalKcal (@Header("Authorization") String token,
                                                         @Query("date") String date);


    // 특정날 섭취한 탄단지 칼로리 데이터 가져오기
    @GET("/foodRecord/total/kcal/data")
    Call<FoodEatDataRes> getFoodEatData (@Header("Authorization") String token,
                                                   @Query("date") String date);

    // 유저의 목표 탄단지 데이터 가져오기
    @GET("/user/target/data")
    Call<UserTargetGetRes> getUserTargetGet (@Header("Authorization") String token);



    // 특정날 운동한 칼로리 데이터 총합 가져오기
    @GET("/exercise/date")
    Call<ExerciseTotalkcalRes> getExerciseTotalKcal (@Header("Authorization") String token,
                                                     @Query("date") String date);


    // 데이터 분석 결과 나타내기

}
