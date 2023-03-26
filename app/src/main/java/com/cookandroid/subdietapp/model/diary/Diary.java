package com.cookandroid.subdietapp.model.diary;

import java.io.Serializable;

public class Diary implements Serializable {
//
//    {
//        "nowWeight": 50,
//            "date": "2023-03-05"
//    }
    private String nowWeight;
    private String date;

    public Diary(){

    }

    public Diary(String nowWeight, String date) {
        this.nowWeight = nowWeight;
        this.date = date;
    }

    public String getNowWeight() {
        return nowWeight;
    }

    public void setNowWeight(String nowWeight) {
        this.nowWeight = nowWeight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
