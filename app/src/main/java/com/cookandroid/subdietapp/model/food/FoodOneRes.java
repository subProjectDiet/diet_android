package com.cookandroid.subdietapp.model.food;

import com.google.gson.annotations.SerializedName;

public class FoodOneRes {
    private String result;

    @SerializedName("items")
    private Food food;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
