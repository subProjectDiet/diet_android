package com.cookandroid.subdietapp.model.diary;

import com.google.gson.annotations.SerializedName;

public class DiaryExerciseBurnRes {

    private String result;

    @SerializedName("item")
    private DiaryExerciseBurn diaryExerciseBurn;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DiaryExerciseBurn getDiaryExerciseBurn() {
        return diaryExerciseBurn;
    }

    public void setDiaryExerciseBurn(DiaryExerciseBurn diaryExerciseBurn) {
        this.diaryExerciseBurn = diaryExerciseBurn;
    }
}
