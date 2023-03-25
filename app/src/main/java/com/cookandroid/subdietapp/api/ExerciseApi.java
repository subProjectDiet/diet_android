package com.cookandroid.subdietapp.api;


import com.cookandroid.subdietapp.model.exercise.ExerciseRecord;
import com.cookandroid.subdietapp.model.exercise.ExerciseRes;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.exercise.ExerciseTotalkcal;
import com.cookandroid.subdietapp.model.exercise.ExerciseTotalkcalRes;
import com.cookandroid.subdietapp.model.exercise.ExerciserRecordRes;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ExerciseApi extends Serializable {


    //하루한 운동 리스트
    @GET("/exercise/datelist")
    Call<ExerciserRecordRes> dailyExercise(@Header("Authorization") String token,
                                           @Query("date") String date,
                                           @Query("offset") int offset,
                                           @Query("limit") int limit);

    //운동 검색시 리스트
    @GET("/exercise/search")
    Call<ExerciseRes> searchExercise(@Header("Authorization") String token,
                                     @Query("keyword") String keyword,
                                     @Query("offset") int offset,
                                     @Query("limit") int limit);

    //운동 검색 선택
    @GET("/exercise/search/select/{exerciseId}")
    Call<ExerciseRes> searchExerciseSelect(@Header("Authorization") String token,
                                                    @Path("exerciseId")int exerciseId,
                                                    @Query("date") String date);

    //(테이블 + 직접) 운동 칼로리 입력하기
    @POST("/exercise/user")
    Call<Res> ExerciseUserDirect(@Header("Authorization") String token,
                                @Body ExerciseRecord exerciseSave,
                                @Query("recordType")  int recordType);


    //(테이블 + 직접) 운동 칼로리 수정하기
    @PUT("/exercise/user/{exerciseRecordId}")
    Call<Res> ExerciseUserModify(@Header("Authorization") String token,
                                 @Body ExerciseRecord exerciseRecord,
                                 @Path("exerciseRecordId") int exerciseRecordId,
                                 @Query("recordType")  int recordType);

    //데이터 삭제
    @DELETE("exercise/{exerciseRecordId}")
    Call<Res> ExerciseUserDelete(@Header("Authorization") String token,
                                 @Path("exerciseRecordId") int exerciseRecordId);

    //오늘 뺀 토탈칼로리
    @GET("/exercise/date")
    Call<ExerciseTotalkcalRes> ExerciseTotalkcal(@Header("Authorization") String token,
                                                 @Query("date") String date);


}
