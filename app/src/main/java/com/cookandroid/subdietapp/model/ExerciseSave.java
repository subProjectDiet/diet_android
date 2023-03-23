package com.cookandroid.subdietapp.model;

import java.io.Serializable;

public class ExerciseSave implements Serializable {
//    {
//        "exerciseName": "달려버리기",
//            "exerciseTime": 30,
//            "totalKcalBurn": 200,
//            "date": "2023-03-05"
//    }
    private String exerciseName;
    private int exerciseTime;
    private  double totalKcalBurn;
    private String date;

    public double getTotalKcalBurn() {
        return totalKcalBurn;
    }

    public void setTotalKcalBurn(double totalKcalBurn) {
        this.totalKcalBurn = totalKcalBurn;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getExerciseTime() {
        return exerciseTime;
    }

    public void setExerciseTime(int exerciseTime) {
        this.exerciseTime = exerciseTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
