package com.cookandroid.subdietapp.model.exercise;

import java.io.Serializable;

public class ExerciseTotalkcal implements Serializable {
//    {
//        "result": "success",
//            "item": {
//        "userId": 68,
//                "date": "2023-03-25",
//                "exerciseDateKcal": 2185.16
//    }
//    }



    private int userId;
    private String date;
    private double exerciseDateKcal;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getExerciseDateKcal() {
        return exerciseDateKcal;
    }

    public void setExerciseDateKcal(double exerciseDateKcal) {
        this.exerciseDateKcal = exerciseDateKcal;
    }
}
