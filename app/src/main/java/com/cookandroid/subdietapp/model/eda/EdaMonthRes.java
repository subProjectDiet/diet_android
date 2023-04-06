package com.cookandroid.subdietapp.model.eda;

import java.util.List;

public class EdaMonthRes {

    private String result;

    private List<EdaMonth> items;

    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<EdaMonth> getItems() {
        return items;
    }

    public void setItems(List<EdaMonth> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
