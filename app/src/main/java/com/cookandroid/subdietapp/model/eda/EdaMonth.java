package com.cookandroid.subdietapp.model.eda;

public class EdaMonth {

//
//"userId": 124,
//        "month": 1,
//        "AvgWeight": 52.4,
//        "AvgKcal": 411.0,
//        "AvgKcalBurn": 401.0
    private int userId;
    private Double AvgWeight;
    private Double AvgKcal;
    private Double AvgKcalBurn;
    private String month;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Double getAvgWeight() {
        return AvgWeight;
    }

    public void setAvgWeight(Double avgWeight) {
        AvgWeight = avgWeight;
    }

    public Double getAvgKcal() {
        return AvgKcal;
    }

    public void setAvgKcal(Double avgKcal) {
        AvgKcal = avgKcal;
    }

    public Double getAvgKcalBurn() {
        return AvgKcalBurn;
    }

    public void setAvgKcalBurn(Double avgKcalBurn) {
        AvgKcalBurn = avgKcalBurn;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }
}
