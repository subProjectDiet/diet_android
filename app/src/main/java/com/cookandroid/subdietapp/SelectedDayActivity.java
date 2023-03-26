package com.cookandroid.subdietapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.api.DiaryApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.diary.Diary;
import com.cookandroid.subdietapp.model.diary.DiaryRes;
import com.cookandroid.subdietapp.posting.SelectedPostingActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedDayActivity extends AppCompatActivity {

    String date;
    String getWeight;
    TextView txtDate, txtMonth, txtWeight, txtTargetKcal;

    Diary diary = new Diary();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_day);

        txtWeight = findViewById(R.id.txtWeight);
        txtDate = findViewById(R.id.txtDate);
        txtMonth = findViewById(R.id.txtMonth);

        txtTargetKcal = findViewById(R.id.txtTargetKcal);

        // 요일 정보 받아오기
        // 2023-03-26
        date = getIntent().getStringExtra("date");

        Log.i("NOWDATE_DIARY", date);

        txtMonth.setText(date.substring(0,3+1)+ "년 " + date.substring(5, 6+1) + "월 다이어리");
        txtDate.setText(date.substring(0,3+1)+ "년 " + date.substring(5, 6+1) + "월 " + date.substring(8, 9+1) + "일");

        txtWeight.setPaintFlags(txtWeight.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // 몸무게 입력란에 유저가 입력한 정보가 있다면 미리 셋팅
        getWeightNetworkData();

        // 유저 목표 칼로리 (회원가입할때 입력한 kcal 정보임) 텍스트뷰에 나타내기
        SharedPreferences sharedPreferences = getSharedPreferences(Config.PREFERENCE_NAME, SelectedPostingActivity.MODE_PRIVATE); // mode_private : 해당 앱에서만 사용
        String targetKcal = sharedPreferences.getString(Config.TARGET_KCAL, "");
        txtTargetKcal.setText("/"+targetKcal);


        txtWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectedDayActivity.this);
                builder.setTitle("몸무게");
                builder.setMessage("몸무게를 입력하세요");
                final EditText et_pass = new EditText(SelectedDayActivity.this);  // 여기가 EditText를 생성하는 곳입니다.
                builder.setView(et_pass);
                builder.setNegativeButton("취소", null);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        getWeight = et_pass.getText().toString();
                        txtWeight.setText("   "+getWeight + "   ");

                        setWeightNetwork();

                    }
                });
                builder.show();
            }
        });




    }

    // 특정날짜에 입력한 몸무게 데이터 가져오기
    // 몸무게를 나타내는 텍스트뷰에 나타내기 위한 용도
    private void getWeightNetworkData() {

//        date

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        DiaryApi api = retrofit.create(DiaryApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<DiaryRes> call = api.setDiaryWeight(accessToken, date);

        call.enqueue(new Callback<DiaryRes>() {

            @Override
            public void onResponse(Call<DiaryRes> call, Response<DiaryRes> response) {


                if (response.isSuccessful()) {

                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.


                    try {

                        diary = response.body().getDiary();


                        String nowWeight = diary.getNowWeight();

                        Log.i("NOWWEIGHT_TEST", nowWeight);

                        txtWeight.setText(nowWeight);


                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<DiaryRes> call, Throwable t) {
                Log.i("포스팅 정보", t.getMessage());
            }
        });






    }

    // 몸무게 다이어리에 입력하는 함수
    private void setWeightNetwork() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedDayActivity.this);
        DiaryApi api = retrofit.create(DiaryApi.class); // 레트로핏으로 서버에 요청할 객체 생성

        // 2-2. 토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        // 3. 저장
//        Diary diary = new Diary(getWeight, date);
        diary.setNowWeight(getWeight);
        diary.setDate(date);

        Call<Res> call = api.addDiaryWeight(accessToken, diary); // 서버에 요청
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(@NonNull Call<Res> call, @NonNull Response<Res> response) {

                if(response.isSuccessful()) {

                    Log.i("WEIGHT",   diary.getDate()+" " +  diary.getNowWeight()+ "몸무게 입력 성공");

                }else{
                    Log.i("WEIGHT",   "같은값으로 인한 에러발생");
                    updateWeightNetwork();
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {


            }

        });




    }

    private void updateWeightNetwork() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedDayActivity.this);
        DiaryApi api = retrofit.create(DiaryApi.class); // 레트로핏으로 서버에 요청할 객체 생성

        // 2-2. 토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        // 3. 저장
//        Diary diary = new Diary(getWeight, date);
        diary.setNowWeight(getWeight);
        diary.setDate(date);


        Call<Res> call = api.updateDiaryWeight(accessToken, diary); // 서버에 요청
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(@NonNull Call<Res> call, @NonNull Response<Res> response) {

                if(response.isSuccessful()) {

                    Log.i("WEIGHT",   "수정 : "+ diary.getDate()+" " +  diary.getNowWeight()+ "몸무게 입력 성공");

                }else{
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {


            }

        });
    }
}