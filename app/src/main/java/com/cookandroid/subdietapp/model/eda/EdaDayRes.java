package com.cookandroid.subdietapp.model.eda;

import java.util.List;

public class EdaDayRes {

    private String result;

    private List<EdaDay> items;

    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<EdaDay> getItems() {
        return items;
    }

    public void setItems(List<EdaDay> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
