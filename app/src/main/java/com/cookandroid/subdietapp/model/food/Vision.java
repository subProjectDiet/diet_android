package com.cookandroid.subdietapp.model.food;

import java.io.Serializable;

public class Vision implements Serializable {

    private String kcal;
    private String 탄수화물;
    private String gram;
    private String 단백질;
    private String 지방;

    public String getKcal() {
        return kcal;
    }

    public void setKcal(String kcal) {
        this.kcal = kcal;
    }

    public String get탄수화물() {
        return 탄수화물;
    }

    public void set탄수화물(String 탄수화물) {
        this.탄수화물 = 탄수화물;
    }

    public String getGram() {
        return gram;
    }

    public void setGram(String gram) {
        this.gram = gram;
    }

    public String get단백질() {
        return 단백질;
    }

    public void set단백질(String 단백질) {
        this.단백질 = 단백질;
    }

    public String get지방() {
        return 지방;
    }

    public void set지방(String 지방) {
        this.지방 = 지방;
    }
}
