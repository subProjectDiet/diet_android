package com.cookandroid.subdietapp.api;

import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.posting.PostingInfo;
import com.cookandroid.subdietapp.model.posting.PostingInfoRes;
import com.cookandroid.subdietapp.model.posting.PostingRes;
import com.cookandroid.subdietapp.model.posting.RecommendRes;

import java.io.Serializable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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

    // 포스팅 작성
    @Multipart
    @POST("/posting")
    Call<Res> addPosting(@Header("Authorization") String token,
                        @Part MultipartBody.Part photo,
                        @Part("content")RequestBody content);

    // 포스팅 수정
    @PUT("/posting/edit/{postingId}")
    Call<Res> updatePosting(@Header("Authorization") String token,
                         @Path("postingId") int postingId,
                         @Body PostingInfo postingInfo);

    // 포스팅 삭제
    @DELETE("/posting/edit/{postingId}")
    Call<Res> deletePosting (@Header("Authorization") String token,
                         @Path("postingId") int postingId);

//    // 댓글 작성
//    @POST("/posting/coment/{postingId}")
//    Call<Res> addComent(@Header("Authorization") String token,
//                        @Path("postingId") int postingId,
//                        @Body Coment coment);
//
//    // 댓글 리스트 가져오기
//    @GET("/posting/coment/{postingId}")
//    Call<ComentRes> getComent(@Header("Authorization") String token,
//                              @Path("postingId") int postingId,
//                              @Query("offset") int offset,
//                              @Query("limit") int limit);

    // 태그로 검색한 포스팅 가져오기
    @GET("/posting/tag")
    Call<PostingRes> getTagPosting(@Header("Authorization") String token,
                                   @Query("offset") int offset,
                                   @Query("limit") int limit,
                                   @Query("Name") String Name);

    // 내가 좋아요한 게시글 가져오기
    @GET("/posting/like/me")
    Call<PostingRes> getMyLikePosting(@Header("Authorization") String token,
                                   @Query("offset") int offset,
                                   @Query("limit") int limit);

    // 내가 작성한 게시글 가져오기
    @GET("/posting/me")
    Call<PostingRes> getMyPosting(@Header("Authorization") String token,
                                      @Query("offset") int offset,
                                      @Query("limit") int limit);


}
