package com.cookandroid.subdietapp.model.eda;

public class EdaDay {

//               "userId": 124,
//                       "date": "2023-04-04",
//                       "nowWeight": 52.0,
//                       "totalKcal": 785.0
//},

    private int userId;
    private String date;
    private Double nowWeight;
    private Double totalKcal;

    private Double totalKcalBurn;

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

    public Double getNowWeight() {
        return nowWeight;
    }

    public void setNowWeight(Double nowWeight) {
        this.nowWeight = nowWeight;
    }

    public Double getTotalKcal() {
        return totalKcal;
    }

    public void setTotalKcal(Double totalKcal) {
        this.totalKcal = totalKcal;
    }

    public Double getTotalKcalBurn() {
        return totalKcalBurn;
    }

    public void setTotalKcalBurn(Double totalKcalBurn) {
        this.totalKcalBurn = totalKcalBurn;
    }
}
