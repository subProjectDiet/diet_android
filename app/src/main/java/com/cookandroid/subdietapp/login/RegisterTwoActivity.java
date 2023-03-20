package com.cookandroid.subdietapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.R;

public class RegisterTwoActivity extends AppCompatActivity {
    ImageView imgGirl, imgBoy;
    TextView txtGirl, txtBoy;
    EditText editAge, editHeight, editNowWeight, editHopeWeight;

    ImageView activity1, activity2, activity3;

    Button btnNext;

    int gender = -1;
    int activity = 0;

//    boolean genderSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        imgGirl = findViewById(R.id.imgGirl);
        txtGirl = findViewById(R.id.txtGirl);
        imgBoy = findViewById(R.id.imgBoy);
        txtBoy = findViewById(R.id.txtBoy);

        editAge = findViewById(R.id.editAge);
        editHeight = findViewById(R.id.editHeight);
        editNowWeight = findViewById(R.id.editNowWeight);
        editHopeWeight = findViewById(R.id.editHopeWeight);

        activity1 = findViewById(R.id.activity1);
        activity2 = findViewById(R.id.activity2);
        activity3 = findViewById(R.id.activity3);

        btnNext = findViewById(R.id.btnNext);

        // # 0:남자, 1:여자
        imgGirl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgGirl.setImageResource(R.drawable.girl_selected);
                gender = 1;
            }
        });

        imgBoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgBoy.setImageResource(R.drawable.boy_selected);
                gender = 0;
            }
        });

        String age = editAge.getText().toString().trim();

        String height = editHeight.getText().toString().trim();
        String nowWeight = editNowWeight.getText().toString().trim();
        String hopeWeight = editHopeWeight.getText().toString().trim();

        activity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity1.setImageResource(R.drawable.activity1);
                activity = 1;
            }
        });


        activity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity2.setImageResource(R.drawable.activity2);
                activity = 2;
            }
        });

        activity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity3.setImageResource(R.drawable.activity3);
                activity = 3;
            }
        });



        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterTwoActivity.this, RegisterThreeActivity.class);
                startActivity(intent);
            }
        });
    }
}