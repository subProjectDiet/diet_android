package com.cookandroid.subdietapp.model.eda;

import com.google.gson.annotations.SerializedName;

public class EatFoodKcalRes {

//    "result": "success",
//            "items": {
//        "userId": 81,
//                "foodName": "닭볶음탕",
//                "kcal": 500,
//                "cnt": 9

    private String result;

    @SerializedName("items")
    private EatFoodKcal eatFoodKcal;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public EatFoodKcal getEatFoodKcal() {
        return eatFoodKcal;
    }

    public void setEatFoodKcal(EatFoodKcal eatFoodKcal) {
        this.eatFoodKcal = eatFoodKcal;
    }
}
