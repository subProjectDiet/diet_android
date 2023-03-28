package com.cookandroid.subdietapp.model.diary;

import java.io.Serializable;

public class DiaryMonth implements Serializable {

//  "userId": 13,
//          "nowWeight": 50.0,
//          "date": "2023-03-05",
//          "foodKcal": "",
//          "exerciseKcal": ""


    private int userId;
    private  double nowWeight;
    private String date;
    private  double foodKcal;
    private double exerciseKcal;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getNowWeight() {
        return nowWeight;
    }

    public void setNowWeight(double nowWeight) {
        this.nowWeight = nowWeight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getFoodKcal() {
        return foodKcal;
    }

    public void setFoodKcal(double foodKcal) {
        this.foodKcal = foodKcal;
    }

    public double getExerciseKcal() {
        return exerciseKcal;
    }

    public void setExerciseKcal(double exerciseKcal) {
        this.exerciseKcal = exerciseKcal;
    }
}


