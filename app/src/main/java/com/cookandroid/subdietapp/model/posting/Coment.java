package com.cookandroid.subdietapp.model.posting;

import java.io.Serializable;

public class Coment implements Serializable {
    private String content;

    public Coment(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
