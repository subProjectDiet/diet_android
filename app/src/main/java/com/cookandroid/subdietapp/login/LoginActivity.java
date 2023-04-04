package com.cookandroid.subdietapp.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.MainActivity;
import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.api.UserApi;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.user.User;
import com.cookandroid.subdietapp.model.user.UserLoginRes;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    TextView txtRegister;

    EditText editEmail;
    EditText editPassword;

    Button btnLogin;

    private int accountType;

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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString().trim();
                Pattern pattern = Patterns.EMAIL_ADDRESS;
                accountType=0;

                if(pattern.matcher(email).matches() == false){
                    Toast.makeText(LoginActivity.this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String password = editPassword.getText().toString().trim();
                // 우리 기획에는 비번길이가 4~12 만 허용
                if(password.length() < 4 || password.length() > 12){
                    Toast.makeText(LoginActivity.this, "비번 길이를 확인하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }


                Retrofit retrofit = NetworkClient.getRetrofitClient(LoginActivity.this);
                UserApi api = retrofit.create(UserApi.class);

                User user = new User(email, password,accountType);

                Call<UserLoginRes> call = api.login(user);
                call.enqueue(new Callback<UserLoginRes>() {
                    @Override
                    public void onResponse(Call<UserLoginRes> call, Response<UserLoginRes> response) {

                        if(response.isSuccessful()){

                            UserLoginRes userLoginRes = response.body();


                            SharedPreferences sp = getApplication().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString(Config.TARGET_KCAL, userLoginRes.getUserLogin().getTargetCarbs() + "" );
                            editor.putString(Config.HOPE_WEIGHT, userLoginRes.getUserLogin().getHopeWeight() + "" );
                            editor.putString(Config.NICKNAME, userLoginRes.getUserLogin().getNickName() );
                            editor.putString(Config.ACCESS_TOKEN, userLoginRes.getAccess_token() );
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        } else if (response.code() == 400) {
                            Toast.makeText(LoginActivity.this, "회원가입이 되어있지 않거나 비번이 틀렸습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(LoginActivity.this, "정상적으로 처리되지 않았습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<UserLoginRes> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "정상적으로 처리되지 않았습니다.", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }
}