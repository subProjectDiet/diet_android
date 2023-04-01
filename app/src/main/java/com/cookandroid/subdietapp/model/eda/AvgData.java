package com.cookandroid.subdietapp.model.eda;

import java.io.Serializable;

public class AvgData implements Serializable {
//        "userId": 81,
//                "AvgKcal": 499.9167,
//                "AvgCarbs": 49.0833,
//                "AvgProtein": 30.6667,
//                "AvgFat": 23.0833,
//                "AvgKcalBurn": 212.77000427246094,
//                "AvgWeight": 53.5
    private int userId;
    private Double AvgKcal;
    private Double AvgCarbs;
    private Double AvgProtein;
    private Double AvgFat;
    private Double AvgKcalBurn;
    private Double AvgWeight;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Double getAvgKcal() {
        return AvgKcal;
    }

    public void setAvgKcal(Double avgKcal) {
        AvgKcal = avgKcal;
    }

    public Double getAvgCarbs() {
        return AvgCarbs;
    }

    public void setAvgCarbs(Double avgCarbs) {
        AvgCarbs = avgCarbs;
    }

    public Double getAvgProtein() {
        return AvgProtein;
    }

    public void setAvgProtein(Double avgProtein) {
        AvgProtein = avgProtein;
    }

    public Double getAvgFat() {
        return AvgFat;
    }

    public void setAvgFat(Double avgFat) {
        AvgFat = avgFat;
    }

    public Double getAvgKcalBurn() {
        return AvgKcalBurn;
    }

    public void setAvgKcalBurn(Double avgKcalBurn) {
        AvgKcalBurn = avgKcalBurn;
    }

    public Double getAvgWeight() {
        return AvgWeight;
    }

    public void setAvgWeight(Double avgWeight) {
        AvgWeight = avgWeight;
    }
}
