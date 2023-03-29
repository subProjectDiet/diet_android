package com.cookandroid.subdietapp.api;

import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.food.FoodAdd;
import com.cookandroid.subdietapp.model.food.FoodOneRes;
import com.cookandroid.subdietapp.model.food.FoodRes;
import com.cookandroid.subdietapp.model.food.TotalKcalRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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

    // 음식 정보 가져오기 (검색결과에서 클릭한 음식 데이터)
    @GET("/food/{foodId}")
    Call<FoodOneRes> getFoodInfo(@Header("Authorization") String token,
                                 @Path("foodId") int foodId);

    // 검색결과에서 클릭한 음식 데이터를 추가
    @POST("/foodrecord")
    Call<Res> addFoodSearch (@Header("Authorization") String token,
                            @Query("recordType") int recordType,
                            @Body FoodAdd foodAdd);



    // 유저가 아침에 섭취한 총 합 칼로리
    @GET("/foodRecord/total/breakfast")
    Call<TotalKcalRes> getTotalBreakfastKcal(@Header("Authorization") String token,
                                             @Query("date") String date);

    // 유저가 추가한 칼로리 수정
    @PUT("/foodrecord/{foodRecordId}")
    Call<Res> updateFoodRecord (@Header("Authorization") String token,
                            @Path("foodRecordId") int foodRecordId,
                            @Body FoodAdd foodAdd);


    // 유저가 추가한 칼로리 삭제
    @DELETE("/foodrecord/{foodRecordId}")
    Call<Res> deleteFoodRecord (@Header("Authorization") String token,
                                 @Path("foodRecordId") int foodRecordId);


    // 점심에 섭취한 총 칼로리 합 가져오기
    @GET("/foodRecord/total/lunch")
    Call<TotalKcalRes> getTotalLunchKcal(@Header("Authorization") String token,
                                             @Query("date") String date);


    // 점심 섭취 칼로리 리스트 가져오기
    @GET("/foodRecord/lunch")
    Call<FoodRes> getLunchKcal(@Header("Authorization") String token,
                                   @Query("date") String date,
                                   @Query("offset") int offset,
                                   @Query("limit") int limit);

    // 저녁 섭취 칼로리 리스트 가져오기
    @GET("/foodRecord/dinner")
    Call<FoodRes> getDinnerKcal(@Header("Authorization") String token,
                               @Query("date") String date,
                               @Query("offset") int offset,
                               @Query("limit") int limit);

    // 저녁에 섭취한 총 칼로리 합 가져오기
    @GET("/foodRecord/total/dinner")
    Call<TotalKcalRes> getTotalDinnerKcal(@Header("Authorization") String token,
                                         @Query("date") String date);


}
