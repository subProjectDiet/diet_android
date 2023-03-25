package com.cookandroid.subdietapp.model.exercise;

import java.io.Serializable;

public class ExerciseTotalkcalRes implements Serializable {
//
//    {
//        "result": "success",
//            "item": {
//        "userId": 68,
//                "date": "2023-03-25",
//                "exerciseDateKcal": 2185.16
//    }
//    }


    private String result;
    private ExerciseTotalkcal item;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ExerciseTotalkcal getItem() {
        return item;
    }

    public void setItem(ExerciseTotalkcal item) {
        this.item = item;
    }
}
