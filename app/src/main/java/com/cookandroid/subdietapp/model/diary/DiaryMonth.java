package com.cookandroid.subdietapp.model.diary;

import java.io.Serializable;

public class DiaryMonth implements Serializable {

//  "userId": 13,
//          "nowWeight": 50.0,
//          "date": "2023-03-05",
//          "foodKcal": "",
//          "exerciseKcal": ""


    private int userId;
    private  String nowWeight;
    private String date;
    private  String foodKcal;
    private String exerciseKcal;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getFoodKcal() {
        return foodKcal;
    }

    public void setFoodKcal(String foodKcal) {
        this.foodKcal = foodKcal;
    }

    public String getExerciseKcal() {
        return exerciseKcal;
    }

    public void setExerciseKcal(String exerciseKcal) {
        this.exerciseKcal = exerciseKcal;
    }
}


