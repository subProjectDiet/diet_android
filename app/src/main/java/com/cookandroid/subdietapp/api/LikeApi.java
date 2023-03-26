package com.cookandroid.subdietapp.api;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LikeApi {
    // 포스팅 좋아요
    @POST("/posting/{postingId}/like")
    Call<Void> postLike(@Header("Authorization") String token,
                        @Path("postingId") int postingId);
    // 포스팅 좋아요 취소
    @DELETE("/posting/{postingId}/like")
    Call<Void> deleteLike(@Header("Authorization") String token,
                          @Path("postingId") int postingId);


}
