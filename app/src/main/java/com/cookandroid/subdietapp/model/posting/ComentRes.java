package com.cookandroid.subdietapp.model.posting;

import java.util.List;

public class ComentRes {

    private String result;


    private List<Coment> items;

    private int count;

    public int getCount() {
        return count;
    }

    public List<Coment> getItems() {
        return items;
    }

    public void setItems(List<Coment> items) {
        this.items = items;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
