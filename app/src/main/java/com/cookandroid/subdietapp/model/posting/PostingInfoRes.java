package com.cookandroid.subdietapp.model.posting;

import com.google.gson.annotations.SerializedName;

public class PostingInfoRes {

    private String result;

    @SerializedName("items")
    private PostingInfo postingInfo;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public PostingInfo getPostingInfo() {
        return postingInfo;
    }

    public void setPostingInfo(PostingInfo postingInfo) {
        this.postingInfo = postingInfo;
    }
}
