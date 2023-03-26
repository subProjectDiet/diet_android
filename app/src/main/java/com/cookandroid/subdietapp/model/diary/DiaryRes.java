package com.cookandroid.subdietapp.model.diary;

import com.google.gson.annotations.SerializedName;

public class DiaryRes {

    private String result;

    @SerializedName("items")
    private Diary diary;

    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Diary getDiary() {
        return diary;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
