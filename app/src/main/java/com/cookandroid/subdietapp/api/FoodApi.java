package com.cookandroid.subdietapp.api;

import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.food.FoodAdd;
import com.cookandroid.subdietapp.model.food.FoodRes;
import com.cookandroid.subdietapp.model.food.TotalKcalRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface FoodApi {

        //    recordType
        //0 : 검색결과에서 추가
        //1 : 유저가 직접 추가


        //    mealTIme
        //0 : 아침
        //1 : 점심
        //2 : 저녁


    // 아침 섭취 칼로리 리스트 가져오기
    @GET("/foodRecord/breakfast")
    Call<FoodRes> getBreakfastKcal(@Header("Authorization") String token,
                                 @Query("date") String date,
                                 @Query("offset") int offset,
                                 @Query("limit") int limit);

    // 칼로리 직접 추가
    @POST("/foodrecord/user")
    Call<Res> addFoodDirect(@Header("Authorization") String token,
                        @Query("recordType") int recordType,
                        @Body FoodAdd foodAdd);

    // 칼로리 검색결과 리스트로 불러오기
    @GET("/food/search")
    Call<FoodRes> getSearchFood(@Header("Authorization") String token,
                                   @Query("keyword") String keyword,
                                   @Query("offset") int offset,
                                   @Query("limit") int limit);

    // 유저가 아침에 섭취한 총 합 칼로리
    @GET("/foodRecord/total/breakfast")
    Call<TotalKcalRes> getTotalBreakfastKcal(@Header("Authorization") String token,
                                             @Query("date") String date);





}
