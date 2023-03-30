package com.cookandroid.subdietapp.model.diary;

import com.google.gson.annotations.SerializedName;

public class FoodEatDataRes {

//    {
//        "result": "success",
//            "items": {
//        "userId": 13,
//                "totalKcal": "584",
//                "totalCarbs": "125",
//                "totalProtein": "125",
//                "totalFat": "112"
//    },
//        "count": 1


    private String result;

    @SerializedName("items")
    private FoodEatData foodEatData;

    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public FoodEatData getFoodEatData() {
        return foodEatData;
    }

    public void setFoodEatData(FoodEatData foodEatData) {
        this.foodEatData = foodEatData;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
