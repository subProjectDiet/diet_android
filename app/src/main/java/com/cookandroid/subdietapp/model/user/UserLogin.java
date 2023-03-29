package com.cookandroid.subdietapp.model.user;

import java.io.Serializable;

public class UserLogin implements Serializable {


//"id": 81,
//        "nickName": "행복한보리",
//        "hopeWeight": 50.0,
//        "targetKcal": 1454,
//        "targetCarbs": 727,
//        "targetProtein": 436,
//        "targetFat": 290

    private int id;
    private String nickName;
    private Double hopeWeight;
    private Double targetKcal;
    private Double targetCarbs;
    private Double targetProtein;
    private Double targetFat;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Double getHopeWeight() {
        return hopeWeight;
    }

    public void setHopeWeight(Double hopeWeight) {
        this.hopeWeight = hopeWeight;
    }

    public Double getTargetKcal() {
        return targetKcal;
    }

    public void setTargetKcal(Double targetKcal) {
        this.targetKcal = targetKcal;
    }

    public Double getTargetCarbs() {
        return targetCarbs;
    }

    public void setTargetCarbs(Double targetCarbs) {
        this.targetCarbs = targetCarbs;
    }

    public Double getTargetProtein() {
        return targetProtein;
    }

    public void setTargetProtein(Double targetProtein) {
        this.targetProtein = targetProtein;
    }

    public Double getTargetFat() {
        return targetFat;
    }

    public void setTargetFat(Double targetFat) {
        this.targetFat = targetFat;
    }
}
