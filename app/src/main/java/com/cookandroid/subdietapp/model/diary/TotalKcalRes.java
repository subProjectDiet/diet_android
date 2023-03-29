package com.cookandroid.subdietapp.model.diary;

import com.google.gson.annotations.SerializedName;

public class TotalKcalRes {


    private String result;

    @SerializedName("items")
    private TotalKcal totalKcal;

    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public TotalKcal getTotalKcal() {
        return totalKcal;
    }

    public void setTotalKcal(TotalKcal totalKcal) {
        this.totalKcal = totalKcal;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
