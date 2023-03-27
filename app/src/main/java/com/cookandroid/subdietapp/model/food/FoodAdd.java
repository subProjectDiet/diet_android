package com.cookandroid.subdietapp.model.food;

import java.io.Serializable;

public class FoodAdd implements Serializable {
//
//    {
//        "foodName": "닭볶음탕",
//            "gram": 50,
//            "kcal": 500,
//            "mealtime": 0,
//            "date": "2023-03-27"
//    }

    private String foodName;
    private Double gram;
    private Double kcal;
    private int mealtime;
    private String date;


    public FoodAdd(String foodName, double gram, double kcal, int mealtime, String date) {
        this.foodName = foodName;
        this.gram = gram;
        this.kcal = kcal;
        this.mealtime = mealtime;
        this.date = date;
    }


    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public Double getGram() {
        return gram;
    }

    public void setGram(Double gram) {
        this.gram = gram;
    }

    public Double getKcal() {
        return kcal;
    }

    public void setKcal(Double kcal) {
        this.kcal = kcal;
    }

    public int getMealtime() {
        return mealtime;
    }

    public void setMealtime(int mealtime) {
        this.mealtime = mealtime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
