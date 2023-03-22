package com.cookandroid.subdietapp.model.posting;

import java.io.Serializable;

public class Recommend implements Serializable {
//       "postingId": 1,
//               "userId": 13,
//               "nickName": "나나",
//               "content": "test수",
//               "imgUrl": "http://diet-health-app.s3.ap-northeast-2.amazonaws.com/132023-03-08T23_55_08.342540.jpg",
//               "createdAt": "2023-03-08T13:38:10",
//               "updatedAt": "2023-03-08T14:55:10",
//               "likeCnt": 3,
//               "group": 2,
//               "isLike": 1

    private int postingId;
    private int userId;
    private String nickName;
    private String content;
    private String imgUrl;
    private String createdAt;
    private String updatedAt;
    private int likeCnt;
    private int group;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }
}
