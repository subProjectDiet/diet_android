package com.cookandroid.subdietapp.api;

import com.cookandroid.subdietapp.model.posting.PostingInfoRes;
import com.cookandroid.subdietapp.model.posting.PostingRes;
import com.cookandroid.subdietapp.model.posting.RecommendRes;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostingApi extends Serializable {


    // 포스팅 인기순 최신순으로 정렬해서 가져오기
    @GET("/posting/like/list")
    Call<PostingRes> getPosting(@Header("Authorization") String token,
                                     @Query("order") String order,
                                     @Query("offset") int offset,
                                     @Query("limit") int limit);

    // 추천 포스팅 가져오기
    @GET("/recommend")
    Call<RecommendRes> getRecommend(@Header("Authorization") String token);


    // 포스팅 정보 하나 가져오기
    @GET("/posting/{postingId}")
    Call<PostingInfoRes> getPostingInfo(@Header("Authorization") String token,
                                        @Path("postingId") int postingId);





}
