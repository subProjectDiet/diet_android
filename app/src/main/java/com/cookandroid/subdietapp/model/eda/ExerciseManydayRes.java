package com.cookandroid.subdietapp.model.eda;

import com.google.gson.annotations.SerializedName;

public class ExerciseManydayRes {



    private String result;

    @SerializedName("items")
    private ExerciseManyday exerciseManyday;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ExerciseManyday getExerciseManyday() {
        return exerciseManyday;
    }

    public void setExerciseManyday(ExerciseManyday exerciseManyday) {
        this.exerciseManyday = exerciseManyday;
    }
}
