package com.cookandroid.subdietapp.model.exercise;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Exercise implements Serializable {

//                "id": 28,
//                        "userId": 12,
//                        "exercise": "줄넘기",
//                        "exerciseTime": 30,
//                        "totalKcalBurn": 200.0,
//                        "date": "2023-03-05",
//                        "recordType": 1


    private int id;
    private int userId;

    private String exercise;
    private int exerciseTime;

    private double  totalKcalBurn;
    private String date;
    private int recordType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getExercise() {
        return exercise;
    }

    public double getTotalKcalBurn() {
        return totalKcalBurn;
    }

    public void setTotalKcalBurn(double totalKcalBurn) {
        this.totalKcalBurn = totalKcalBurn;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
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

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }
}
