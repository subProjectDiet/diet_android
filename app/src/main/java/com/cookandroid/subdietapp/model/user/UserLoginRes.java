package com.cookandroid.subdietapp.model.user;

import com.google.gson.annotations.SerializedName;

public class UserLoginRes {
//    "result": "success",
//            "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmcmVzaCI6ZmFsc2UsImlhdCI6MTY4MDEwNDExMSwianRpIjoiNWEwOTUwMDEtZTZlNi00Y2ZiLTlhZTUtOWRjYWE0ZTg5MzE0IiwidHlwZSI6ImFjY2VzcyIsInN1YiI6ODEsIm5iZiI6MTY4MDEwNDExMX0.YgXiLN9ZGfKpTUF6JqAsqnwIENAZSDX8TlTruuNAuDU",
//            "userInfo_list": {
//        "id": 81,
//                "nickName": "행복한보리",
//                "hopeWeight": 50.0,
//                "targetKcal": 1454,
//                "targetCarbs": 727,
//                "targetProtein": 436,
//                "targetFat": 290
//    }

    private String result;
    @SerializedName("userInfo_list")
    private UserLogin userLogin;

    private String access_token;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
