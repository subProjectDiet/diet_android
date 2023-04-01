package com.cookandroid.subdietapp.model.eda;

import java.io.Serializable;

public class EatManyday implements Serializable {
//      "userId": 81,
//              "kcal": 683,
//              "date": "2023-03-30"
   private int userId;
   private int kcal;
   private String date;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
