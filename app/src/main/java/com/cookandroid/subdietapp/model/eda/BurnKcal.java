package com.cookandroid.subdietapp.model.eda;

import java.io.Serializable;

public class BurnKcal implements Serializable {
    //       "userId": 81,
//               "burnWeight": -2.0
    private int userId;
    private Double burnWeight;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Double getBurnWeight() {
        return burnWeight;
    }

    public void setBurnWeight(Double burnWeight) {
        this.burnWeight = burnWeight;
    }
}

