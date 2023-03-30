package com.cookandroid.subdietapp.model.diary;

import java.io.Serializable;

public class UserTargetGet implements Serializable {


//           "id": 18,
//                   "userId": 81,
//                   "targetKcal": 1454,
//                   "targetCarbs": 727,
//                   "targetProtein": 436,
//                   "targetFat": 290


    private int id;
    private int targetKcal;
    private int targetCarbs;
    private int targetProtein;
    private int targetFat;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTargetKcal() {
        return targetKcal;
    }

    public void setTargetKcal(int targetKcal) {
        this.targetKcal = targetKcal;
    }

    public int getTargetCarbs() {
        return targetCarbs;
    }

    public void setTargetCarbs(int targetCarbs) {
        this.targetCarbs = targetCarbs;
    }

    public int getTargetProtein() {
        return targetProtein;
    }

    public void setTargetProtein(int targetProtein) {
        this.targetProtein = targetProtein;
    }

    public int getTargetFat() {
        return targetFat;
    }

    public void setTargetFat(int targetFat) {
        this.targetFat = targetFat;
    }
}
