package com.cookandroid.subdietapp.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.api.UserApi;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.UserInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterTwoActivity extends AppCompatActivity {
    EditText editAge, editHeight, editNowWeight, editHopeWeight;

    RadioGroup radioGroup1, radioGroup2;

    RadioButton radioGirl, radioBoy, radio1, radio2, radio3;
    Button btnNext;


    int gender;
    int activity;

    String height;
    String nowWeight;
    String hopeWeight;
    String age;

    Double recommendKcal;
//    boolean genderSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);


        editAge = findViewById(R.id.editAge);
        editHeight = findViewById(R.id.editHeight);
        editNowWeight = findViewById(R.id.editNowWeight);
        editHopeWeight = findViewById(R.id.editHopeWeight);

        radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup2 = findViewById(R.id.radioGroup2);

        radioGirl = findViewById(R.id.radioGirl);
        radioBoy = findViewById(R.id.radioBoy);

        radio1 = findViewById(R.id.radio1);
        radio2 = findViewById(R.id.radio2);
        radio3 = findViewById(R.id.radio3);

        btnNext = findViewById(R.id.btnNext);


        // # 0:남자, 1:여자
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int radioGender = radioGroup1.getCheckedRadioButtonId();

                if (radioGender == R.id.radioGirl){
                    radioGirl.setBackgroundResource(R.drawable.girl_selected);
                    gender = 0;
                } else if (radioGender == R.id.radioBoy) {
                    radioBoy.setBackgroundResource(R.drawable.boy_selected);
                    gender = 1;
                } else {
                    Toast.makeText(RegisterTwoActivity.this, "성별을 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int radioActivity = radioGroup2.getCheckedRadioButtonId();

                if (radioActivity == R.id.radio1){
                    radio1.setBackgroundResource(R.drawable.activity1);
                    activity = 1;
                } else if (radioActivity == R.id.radio2) {
                    radio2.setBackgroundResource(R.drawable.activity2);
                    activity = 2;
                } else if (radioActivity == R.id.radio3) {
                    radio3.setBackgroundResource(R.drawable.activity3);
                    activity = 3;
                } else {
                    Toast.makeText(RegisterTwoActivity.this, "활동량을 선택하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                age = editAge.getText().toString().trim();

                height = editHeight.getText().toString().trim();
                nowWeight = editNowWeight.getText().toString().trim();
                hopeWeight = editHopeWeight.getText().toString().trim();


//                남자 66.47+(13.75X체중)+(5X키)–(6.76X나이)
//                여자 655.1+(9.56X체중)+(1.85X키)–(4.68X나이)

                // 남자일때 추천 칼로리 계산
                if (gender == 0){
                    recommendKcal = 66.47 + (13.75 * Double.parseDouble(nowWeight))
                            + (5 * Double.parseDouble(height)) - (6.76 * Integer.parseInt(age));

                } else if (gender == 1) {

                    recommendKcal = 655.17 + (9.56 * Double.parseDouble(nowWeight))
                            + (1.85 * Double.parseDouble(height)) - (4.68 * Integer.parseInt(age));
                }


                addNetworkData();

    }


    void addNetworkData(){
        Retrofit retrofit = NetworkClient.getRetrofitClient(RegisterTwoActivity.this);
        UserApi api = retrofit.create(UserApi.class); // 레트로핏으로 서버에 요청할 객체 생성

        // 2-2. 토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        // 3. 저장
        UserInfo userInfo= new UserInfo(gender,age, height, nowWeight, hopeWeight,activity); // UserPreference 객체 생성
        Call<Res> call = api.registerInfo(accessToken, userInfo); // 서버에 요청
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(@NonNull Call<Res> call, @NonNull Response<Res> response) {

                if(response.isSuccessful()) {

                    Intent intent = new Intent(RegisterTwoActivity.this, RegisterThreeActivity.class);
                    Log.i("RECOMMEND", recommendKcal+"");
                    intent.putExtra("recommendKcal", recommendKcal);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(RegisterTwoActivity.this, "내용을 모두 입력해 주세요.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
            }
        });
    }
        });
    }
}