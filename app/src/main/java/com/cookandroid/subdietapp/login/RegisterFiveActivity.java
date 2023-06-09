package com.cookandroid.subdietapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cookandroid.subdietapp.MainActivity;
import com.cookandroid.subdietapp.R;

public class RegisterFiveActivity extends AppCompatActivity {

    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_five);

        btnHome = findViewById(R.id.btnHome);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterFiveActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}