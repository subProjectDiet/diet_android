package com.cookandroid.subdietapp.food;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.SelectedDayActivity;

public class SelectedBreakfastFoodActivity extends AppCompatActivity {


    Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_breakfast_food);


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