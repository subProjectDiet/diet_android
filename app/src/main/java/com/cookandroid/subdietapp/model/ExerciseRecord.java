package com.cookandroid.subdietapp.model;

import java.io.Serializable;

public class ExerciseRecord implements Serializable {
//"id": 28,
//        "userId": 12,
//        "exerciseName": "줄넘기",
//        "exerciseTime": 30,
//        "totalKcalBurn": 200.0,
//        "date": "2023-03-05",
//        "recordType": 1

    private int id;
    private int userId;
    private String exerciseName;
    private int exerciseTime;
    private Float totalKcalBurn;
    private  String date;
    private  int recordType;


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

    public Float getTotalKcalBurn() {
        return totalKcalBurn;
    }

    public void setTotalKcalBurn(Float totalKcalBurn) {
        this.totalKcalBurn = totalKcalBurn;
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
