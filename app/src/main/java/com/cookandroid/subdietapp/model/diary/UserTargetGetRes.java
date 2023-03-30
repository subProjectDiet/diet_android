package com.cookandroid.subdietapp.model.diary;

import com.google.gson.annotations.SerializedName;

public class UserTargetGetRes {

//    {
//        "result": "success",
//            "items": {
//        "id": 18,
//                "userId": 81,
//                "targetKcal": 1454,
//                "targetCarbs": 727,
//                "targetProtein": 436,
//                "targetFat": 290
//    },
//        "count": 1
//    }

    private String result;

    @SerializedName("items")
    private UserTargetGet userTargetGet;

    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public UserTargetGet getUserTargetGet() {
        return userTargetGet;
    }

    public void setUserTargetGet(UserTargetGet userTargetGet) {
        this.userTargetGet = userTargetGet;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
