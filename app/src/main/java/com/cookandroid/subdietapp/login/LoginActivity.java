package com.cookandroid.subdietapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.R;

public class LoginActivity extends AppCompatActivity {

    TextView txtRegister;

    EditText editEmail;
    EditText editPassword;

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtRegister = findViewById(R.id.txtRegister);
        btnLogin = findViewById(R.id.btnLogin);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterFirstActivity.class);
                startActivity(intent);


            }
        });

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String email = editEmail.getText().toString().trim();
//                Pattern pattern = Patterns.EMAIL_ADDRESS;
//
//                if(pattern.matcher(email).matches() == false){
//                    Toast.makeText(LoginActivity.this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                String password = editPassword.getText().toString().trim();
//                // 우리 기획에는 비번길이가 4~12 만 허용
//                if(password.length() < 4 || password.length() > 12){
//                    Toast.makeText(LoginActivity.this, "비번 길이를 확인하세요.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//
//                Retrofit retrofit = NetworkClient.getRetrofitClient(LoginActivity.this);
//                UserApi api = retrofit.create(UserApi.class);
//
//                User user = new User(email, password);
//
//                Call<UserRes> call = api.login(user);
//                call.enqueue(new Callback<UserRes>() {
//                    @Override
//                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
//
//                        if(response.isSuccessful()){
//
//
//                            UserRes res = response.body();
//
//                            SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sp.edit();
////                            editor.putString(Config.NICKNAME, res.getNickname() );
//                            editor.putString(Config.ACCESS_TOKEN, res.getAccess_token() );
//                            editor.apply();
//
//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
//
//                        } else if (response.code() == 400) {
//                            Toast.makeText(LoginActivity.this, "회원가입이 되어있지 않거나 비번이 틀렸습니다.", Toast.LENGTH_SHORT).show();
//                            return;
//                        } else {
//                            Toast.makeText(LoginActivity.this, "정상적으로 처리되지 않았습니다.", Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<UserRes> call, Throwable t) {
//                        Toast.makeText(LoginActivity.this, "정상적으로 처리되지 않았습니다.", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//
//            }
//        });

    }
}