package com.cookandroid.subdietapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class EdaActivity extends AppCompatActivity {

    TextView txtMonthReport;

    TextView EatKcalAvgCount, EatManyDay,ExerciseManyDay,FoodAvgKcal,ExerciseAvgKcal,
            AvgWeight,MonthBurnKcal, EatAvgCPF;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eda);

        txtMonthReport = findViewById(R.id.txtMonthReport);

        EatKcalAvgCount = findViewById(R.id.EatKcalAvgCount);

        EatManyDay = findViewById(R.id.EatManyDay);

        ExerciseManyDay = findViewById(R.id.ExerciseManyDay);

        FoodAvgKcal = findViewById(R.id.FoodAvgKcal);

        ExerciseAvgKcal = findViewById(R.id.ExerciseAvgKcal);

        AvgWeight = findViewById(R.id.AvgWeight);

        MonthBurnKcal = findViewById(R.id.MonthBurnKcal);

        EatAvgCPF = findViewById(R.id.EatAvgCPF);




    }


}