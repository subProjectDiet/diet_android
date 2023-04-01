package com.cookandroid.subdietapp.model.eda;

import com.google.gson.annotations.SerializedName;

public class EatManydayRes {

//    "result": "success",
//            "items": {
//        "userId": 81,
//                "foodName": "닭볶음탕",
//                "kcal": 500,
//                "cnt": 9

    private String result;

    @SerializedName("items")
    private EatManyday eatManyday;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public EatManyday getEatManyday() {
        return eatManyday;
    }

    public void setEatManyday(EatManyday eatManyday) {
        this.eatManyday = eatManyday;
    }
}
