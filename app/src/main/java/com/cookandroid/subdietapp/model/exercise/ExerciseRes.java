package com.cookandroid.subdietapp.model.exercise;

import java.io.Serializable;
import java.util.List;

public class ExerciseRes implements Serializable {

//    {
//        "result": "success",
//            "items": [
//        {
//
//    ],
//        "count": 3
//    }

    private String result;

    private List<Exercise> items;
    private  int count;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Exercise> getItems() {
        return items;
    }

    public void setItems(List<Exercise> items) {
        this.items = items;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
