package com.cookandroid.subdietapp.api;


import com.cookandroid.subdietapp.model.Exercise;
import com.cookandroid.subdietapp.model.ExerciseRes;
import com.cookandroid.subdietapp.model.ExerciseSave;
import com.cookandroid.subdietapp.model.Res;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ExerciseApi extends Serializable {


    //하루한 운동 리스트
    @GET("/exercise/datelist")
    Call<ExerciseRes> dailyExercise(@Header("Authorization") String token,
                                    @Query("date") String date);

    //운동 검색시 리스트
    @GET("/exercise/search")
    Call<ExerciseRes> searchExercise(@Header("Authorization") String token,
                                     @Query("keyword") String keyword,
                                     @Query("offset") int offset,
                                     @Query("limit") int limit);

    //운동 검색 선택
    // 결과값이 object로와서 {} 바로 exercise로 연결
    @GET("/exercise/search/select/{exerciseId}")
    Call<Exercise> searchExerciseSelect(@Header("Authorization") String token,
                                        @Path("exerciseId")int exerciseId,
                                        @Query("date") String date);


    @POST("/exercise/user")
    Call<Res> ExerciseUserDirect(@Header("Authorization") String token,
                                @Body ExerciseSave exerciseSave,
                                @Query("recordType")  int recordType);




}
