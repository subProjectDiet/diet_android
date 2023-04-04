package com.cookandroid.subdietapp.model.user;

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

    private  int accountType;

    // 계정 고유 토큰정보
    private String idToken;
    public User() {
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public User(String email, String password,int accountType) {
        this.email = email;
        this.password = password;
        this.accountType=accountType;
    }

    public User(String email, String nickName, String password,int accountType) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.accountType=accountType;
    }

    public User(String email, String nickName, String password, String idToken) {
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
