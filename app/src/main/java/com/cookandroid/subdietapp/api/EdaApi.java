package com.cookandroid.subdietapp.api;

import com.cookandroid.subdietapp.model.eda.AvgDataRes;
import com.cookandroid.subdietapp.model.eda.BurnKcalRes;
import com.cookandroid.subdietapp.model.eda.EatFoodKcalRes;
import com.cookandroid.subdietapp.model.eda.EatManydayRes;
import com.cookandroid.subdietapp.model.eda.EdaDayRes;
import com.cookandroid.subdietapp.model.eda.ExerciseManydayRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface EdaApi {

    // 가장 많이 먹은 음식 및 칼로리
    @GET("/eda/eat/foodkcal")
    Call<EatFoodKcalRes> getEatFoodKcal (@Header("Authorization") String token,
                                         @Query("date") String date);

    // 가장 많이 먹은 날
    @GET("/eda/eat/manyday")
    Call<EatManydayRes> getEatManyDay (@Header("Authorization") String token,
                                       @Query("date") String date);

    // 운동을 가장 많이 한 날
    @GET("/eda/exercise/manyday")
    Call<ExerciseManydayRes> getExerciseManyDay (@Header("Authorization") String token,
                                                 @Query("date") String date);


    // 데이터 평균값 가져오기
    @GET("/eda/avg/data")
    Call<AvgDataRes> getAvgKcal (@Header("Authorization") String token,
                                 @Query("date") String date);


    // 이번달 감량 데이터 가져오기
    @GET("/eda/burn/weight")
    Call<BurnKcalRes> getBurnData (@Header("Authorization") String token,
                                   @Query("date") String date);


    // 차트 관련
    // 일간 도표 데이터
    @GET("/eda/day")
    Call<EdaDayRes> getEdaDay (@Header("Authorization") String token,
                               @Query("date") String date);

}
