package com.cookandroid.subdietapp.model.posting;

import java.io.Serializable;

public class PostingInfo implements Serializable {

//   "postingId": 6,
//           "userId": 13,
//           "nickName": "나나",
//           "createdAt": "2023-03-13T14:28:15",
//           "imgurl": "http://diet-health-app.s3.ap-northeast-2.amazonaws.com/2023-03-14T21_10_37.815109.jpg",
//           "likeCnt": 1,
//           "content": "수정함함 ",
//           "isLike": 1


    private int postingId;
    private int userId;
    private String nickName;
    private String createdAt;
    private String imgurl;
    private int likeCnt;
    private String content;
    private int isLike;


    public int getPostingId() {
        return postingId;
    }

    public void setPostingId(int postingId) {
        this.postingId = postingId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }
}
