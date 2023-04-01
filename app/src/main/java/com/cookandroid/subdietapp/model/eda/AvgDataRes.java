package com.cookandroid.subdietapp.model.eda;

import com.google.gson.annotations.SerializedName;

public class AvgDataRes {

//    "result": "success",
//            "items": {
//        "userId": 81,
//                "foodName": "닭볶음탕",
//                "kcal": 500,
//                "cnt": 9

    private String result;

    @SerializedName("items")
    private AvgData avgData;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public AvgData getAvgData() {
        return avgData;
    }

    public void setAvgData(AvgData avgData) {
        this.avgData = avgData;
    }
}
