package com.cookandroid.subdietapp.model.posting;

import com.google.gson.annotations.SerializedName;

public class RecommendRes {
    // JSON 응답의 키 이름
    // SerializeName 을 사용하면 JSON 응답의 키 이름과 변수 이름을 다르게 사용할 수 있다.
    @SerializedName("items")
    private Recommend recommend;

    private String result;

    private int count;


    public Recommend getRecommend() {
        return recommend;
    }

    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
