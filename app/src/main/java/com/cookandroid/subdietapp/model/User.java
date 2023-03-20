package com.cookandroid.subdietapp.model;

import java.io.Serializable;

public class User implements Serializable {
    // 포스트맨 body 부분
//    {
//        "nickname": "TITI",
//         "email": "TTT@naver.com",
//         "password": "1234"
//    }
    private String email;

    private String nickName;

    private String password;

    public User() {
    }

    public User(String email, String nickName, String password) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
    }



}
