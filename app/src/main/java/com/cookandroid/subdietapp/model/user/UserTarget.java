package com.cookandroid.subdietapp.model.user;

import java.io.Serializable;

public class UserTarget implements Serializable {

//    {
//        "targetKcal": 1400,
//            "targetCarbs": 100,
//            "targetProtein": 150,
//            "targetFat": 130
//    }
//

    private double targetKcal;
    private double targetCarbs;
    private double targetProtein;
    private double targetFat;

    public UserTarget(double targetKcal, double targetCarbs, double targetProtein, double targetFat) {
        this.targetKcal = targetKcal;
        this.targetCarbs = targetCarbs;
        this.targetProtein = targetProtein;
        this.targetFat = targetFat;
    }
}
