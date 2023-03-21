package com.cookandroid.subdietapp.model;

import java.io.Serializable;

public class UserInfo implements Serializable {
//    {
//        "gender": 1,
//            "age": 23,
//            "height": 160.3,
//            "nowWeight": 50.2,
//            "hopeWeight": 47.3,
//            "activity": 1
//    }


    private int gender;
    private String age;
    private String height;
    private String nowWeight;
    private String hopeWeight;
    private int activity;


    public UserInfo(int gender, String age, String height, String nowWeight, String hopeWeight, int activity) {
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.nowWeight = nowWeight;
        this.hopeWeight = hopeWeight;
        this.activity = activity;
    }
}
