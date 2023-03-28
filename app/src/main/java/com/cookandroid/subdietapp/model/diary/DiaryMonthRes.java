package com.cookandroid.subdietapp.model.diary;

import java.io.Serializable;
import java.util.List;

public class DiaryMonthRes implements Serializable {

    private String result;


    private List<DiaryMonth> items;

    private int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<DiaryMonth> getItems() {
        return items;
    }

    public void setItems(List<DiaryMonth> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
