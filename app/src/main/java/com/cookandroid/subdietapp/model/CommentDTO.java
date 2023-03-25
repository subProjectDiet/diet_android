package com.cookandroid.subdietapp.model;

public class CommentDTO {

    private String userName;
    private String message;

    public CommentDTO() {}
    public CommentDTO(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }
}
