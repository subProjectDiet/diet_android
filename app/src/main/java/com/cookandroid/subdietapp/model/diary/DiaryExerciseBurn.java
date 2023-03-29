package com.cookandroid.subdietapp.model.diary;

import java.io.Serializable;

public class DiaryExerciseBurn implements Serializable {

//        "userId": 68,
//        "date": "2023-03-25",
//        "exerciseDateKcal": 2185.16

    private int userId;
    private Double exerciseDateKcal;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Double getExerciseDateKcal() {
        return exerciseDateKcal;
    }

    public void setExerciseDateKcal(Double exerciseDateKcal) {
        this.exerciseDateKcal = exerciseDateKcal;
    }
}
