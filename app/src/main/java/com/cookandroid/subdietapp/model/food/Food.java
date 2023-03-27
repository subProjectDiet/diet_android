package com.cookandroid.subdietapp.model.food;

import java.io.Serializable;

public class Food implements Serializable {
//
//    {
//        "id": 49,
//            "userId": 13,
//            "foodName": "닭볶음탕",
//            "gram": 50,
//            "kcal": 500,
//            "carbs": 31,
//            "protein": 31,
//            "fat": 28,
//            "mealtime": 0,
//            "date": "2023-03-03",
//            "recordType": 2
//    }

    private int id;
    private int userId;
    private String foodName;
    private String gram;
    private String kcal;
    private String carbs;
    private String protein;
    private String fat;
    private String mealtime;
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

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getGram() {
        return gram;
    }

    public void setGram(String gram) {
        this.gram = gram;
    }

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getMealtime() {
        return mealtime;
    }

    public void setMealtime(String mealtime) {
        this.mealtime = mealtime;
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
