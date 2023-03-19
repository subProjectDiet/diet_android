package com.cookandroid.subdietapp.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.SelectedDayActivity;

public class SelectedBreakfastFoodActivity extends AppCompatActivity {

    EditText editFood;
    TextView totalKcal;
    Button  btnKcal;
    Button  btnSearch;
    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_breakfast_food);

        editFood.findViewById(R.id.editFood);
        totalKcal.findViewById(R.id.totalKcal);
        btnKcal.findViewById(R.id.btnKcal);
        btnSearch.findViewById(R.id.btnSearch);
        btnSave.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedBreakfastFoodActivity.this, SelectedDayActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
}