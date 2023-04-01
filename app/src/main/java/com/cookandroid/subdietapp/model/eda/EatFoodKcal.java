package com.cookandroid.subdietapp.model.eda;

import java.io.Serializable;

public class EatFoodKcal implements Serializable {
// "items": {
//        "userId": 81,
//                "foodName": "닭볶음탕",
//                "kcal": 500,
//                "cnt": 9

   private int userId;
   private String foodName;
   private int kcal;
   private int cnt;

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

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }
}
