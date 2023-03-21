package com.cookandroid.subdietapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.R;

public class RegisterThreeActivity extends AppCompatActivity {

    Button btnNext;
    EditText editKcal;

    TextView txtKcal;

    Double recommendKcal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_three);

        btnNext = findViewById(R.id.btnNext);
        editKcal = findViewById(R.id.editKcal);
        txtKcal = findViewById(R.id.txtKcal);


        recommendKcal = getIntent().getDoubleExtra("recommendKcal", 0);
        editKcal.setText((Math.floor(recommendKcal)+""));

        txtKcal.setText((Math.floor(recommendKcal)+""));

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String targetKcal = editKcal.getText().toString().trim();

                Intent intent = new Intent(RegisterThreeActivity.this, RegisterFourActivity.class);
                intent.putExtra("targetKcal", targetKcal);
                startActivity(intent);
            }
        });
    }
}