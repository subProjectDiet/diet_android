package com.cookandroid.subdietapp.model.eda;

import java.io.Serializable;

public class ExerciseManyday implements Serializable {
//    "items": {
//        "userId": 81,
//                "date": "2023-03-30",
//                "monthTotal": 212.77000427246094
//    },
   private int userId;
   private String date;

   private Double monthTotal;

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

    public Double getMonthTotal() {
        return monthTotal;
    }

    public void setMonthTotal(Double monthTotal) {
        this.monthTotal = monthTotal;
    }
}
