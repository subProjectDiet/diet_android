package com.cookandroid.subdietapp.model.eda;

import java.util.List;

public class EdaWeekRes {

    private String result;

    private List<EdaWeek> items;

    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<EdaWeek> getItems() {
        return items;
    }

    public void setItems(List<EdaWeek> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
