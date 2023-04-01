package com.cookandroid.subdietapp.model.eda;

import com.google.gson.annotations.SerializedName;

public class BurnKcalRes {

//    "result": "success",
//            "items": {
//        "userId": 81,
//                "foodName": "닭볶음탕",
//                "kcal": 500,
//                "cnt": 9

    private String result;

    @SerializedName("items")
    private BurnKcal burnKcal;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public BurnKcal getBurnKcal() {
        return burnKcal;
    }

    public void setBurnKcal(BurnKcal burnKcal) {
        this.burnKcal = burnKcal;
    }
}
