package com.cookandroid.subdietapp.model.diary;

import java.io.Serializable;

public class TotalKcal implements Serializable {


    private String totalKcal;

    public TotalKcal(){

    }

    public String getTotalKcal() {
        return totalKcal;
    }

    public void setTotalKcal(String totalKcal) {
        this.totalKcal = totalKcal;
    }
}
