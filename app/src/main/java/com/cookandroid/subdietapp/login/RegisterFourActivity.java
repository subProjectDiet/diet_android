package com.cookandroid.subdietapp.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.api.UserApi;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.user.UserTarget;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterFourActivity extends AppCompatActivity {

    EditText editCarbs, editProtein, editFat;
    TextView txtCarbs, txtProtein, txtFat;
    // 목표 칼로리
    TextView txtKcal;
    Button btnNext;


    Double doubleGetKcal;

    Double targetCarbs;
    Double targetProtein;
    Double targetFat;

    Double targetKcal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_four);

        editCarbs = findViewById(R.id.editCarbs);
        editProtein = findViewById(R.id.editProtein);
        editFat = findViewById(R.id.editFat);

        txtCarbs = findViewById(R.id.txtCarbs);
        txtProtein = findViewById(R.id.txtProtein);
        txtFat = findViewById(R.id.txtFat);

        txtKcal = findViewById(R.id.txtKcal);

        btnNext = findViewById(R.id.btnNext);

        // 회원가입 세번째 페이지에서 받은 칼로리
        // 이 칼로리를 이용해서 탄단지를 계산해준다
        String getKcal = getIntent().getStringExtra("targetKcal");
        doubleGetKcal = Double.parseDouble(getKcal);

        // 먼저 editText 에 탄단지 계산결과 입력
        // txt 에는 editText 에서 나온 결과를 입력하는대로 바로 계산해서 보여주는 방식

        txtKcal.setText(doubleGetKcal+"");

        // 탄수화물
        editCarbs.setText(Math.round(doubleGetKcal * 0.5 / 4)+"");
        txtCarbs.setText(Math.round(doubleGetKcal * 0.5)+"");

        // 단백질
        editProtein.setText(Math.round(doubleGetKcal * 0.3 / 4) +"");
        txtProtein.setText(Math.round(doubleGetKcal * 0.3) +"");
        // 지방
        editFat.setText(Math.round(doubleGetKcal * 0.2 / 9) +"");
        txtFat.setText(Math.round(doubleGetKcal * 0.2) +"");

        editCarbs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {
                    targetFat = Double.valueOf(Math.round(Double.parseDouble(editFat.getText().toString().trim())* 9));
                    targetCarbs = Double.valueOf(Math.round(Double.parseDouble(editCarbs.getText().toString().trim()) * 4));
                    targetProtein = Double.valueOf(Math.round(Double.parseDouble(editProtein.getText().toString().trim()) * 4));

                    txtKcal.setText(targetCarbs+targetFat+targetProtein+"");
                    txtCarbs.setText(targetCarbs +"");

                } catch (Exception e){
                    return;
                }

            }
        });

        editProtein.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {
                    targetFat = Double.valueOf(Math.round(Double.parseDouble(editFat.getText().toString().trim())* 9));
                    targetCarbs = Double.valueOf(Math.round(Double.parseDouble(editCarbs.getText().toString().trim()) * 4));
                    targetProtein = Double.valueOf(Math.round(Double.parseDouble(editProtein.getText().toString().trim()) * 4));

                    txtKcal.setText(targetCarbs+targetFat+targetProtein+"");
                    txtProtein.setText(targetProtein +"");

                } catch (Exception e){
                    return;
                }

            }
        });

        editFat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {
                    targetFat = Double.valueOf(Math.round(Double.parseDouble(editFat.getText().toString().trim())* 9));
                    targetCarbs = Double.valueOf(Math.round(Double.parseDouble(editCarbs.getText().toString().trim()) * 4));
                    targetProtein = Double.valueOf(Math.round(Double.parseDouble(editProtein.getText().toString().trim()) * 4));

                    txtFat.setText(targetFat+"");

                    txtKcal.setText(targetCarbs+targetFat+targetProtein+"");


                } catch (Exception e){
                    return;
                }


            }
        });




        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                targetKcal = Double.valueOf(Math.round(Double.parseDouble(txtKcal.getText().toString().trim())));

                targetCarbs = Double.valueOf(Math.round(Double.parseDouble(txtCarbs.getText().toString().trim())));
                targetFat = Double.valueOf(Math.round(Double.parseDouble(txtFat.getText().toString().trim())));
                targetProtein = Double.valueOf(Math.round(Double.parseDouble(txtProtein.getText().toString().trim())));

                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Config.TARGET_KCAL, txtKcal.getText().toString().trim());
                editor.apply(); // 저장

                // 여기서 네트워크 통신
                getNetworkData();
            }
        });
    }


    void getNetworkData(){
        Retrofit retrofit = NetworkClient.getRetrofitClient(RegisterFourActivity.this);
        UserApi api = retrofit.create(UserApi.class); // 레트로핏으로 서버에 요청할 객체 생성

        // 2-2. 토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        // 3. 저장
        UserTarget userTarget = new UserTarget(targetKcal,targetCarbs, targetProtein,targetFat);

        Call<Res> call = api.registerTarget(accessToken, userTarget); // 서버에 요청
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(@NonNull Call<Res> call, @NonNull Response<Res> response) {

                if(response.isSuccessful()) {

                    Intent intent = new Intent(RegisterFourActivity.this, RegisterFiveActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
            }
        });
    }
}