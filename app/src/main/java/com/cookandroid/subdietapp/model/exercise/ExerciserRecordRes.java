package com.cookandroid.subdietapp.model.exercise;

import java.io.Serializable;
import java.util.List;

public class ExerciserRecordRes implements Serializable {

    private String result;
    private List<ExerciseRecord> items;
    private  int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<ExerciseRecord> getItems() {
        return items;
    }

    public void setItems(List<ExerciseRecord> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
