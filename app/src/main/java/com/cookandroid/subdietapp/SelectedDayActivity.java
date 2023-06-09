package com.cookandroid.subdietapp;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cookandroid.subdietapp.api.DiaryApi;
import com.cookandroid.subdietapp.api.ExerciseApi;
import com.cookandroid.subdietapp.api.FoodApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.exercise.SelectedExerciseActivity;
import com.cookandroid.subdietapp.food.SelectedBreakfastFoodActivity;
import com.cookandroid.subdietapp.food.SelectedDinnerFoodActivity;
import com.cookandroid.subdietapp.food.SelectedLunchFoodActivity;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.diary.Diary;
import com.cookandroid.subdietapp.model.diary.DiaryRes;
import com.cookandroid.subdietapp.model.diary.FoodEatData;
import com.cookandroid.subdietapp.model.diary.FoodEatDataRes;
import com.cookandroid.subdietapp.model.diary.UserTargetGet;
import com.cookandroid.subdietapp.model.diary.UserTargetGetRes;
import com.cookandroid.subdietapp.model.exercise.ExerciseTotalkcal;
import com.cookandroid.subdietapp.model.exercise.ExerciseTotalkcalRes;
import com.cookandroid.subdietapp.model.food.TotalKcalRes;
import com.cookandroid.subdietapp.posting.SelectedPostingActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedDayActivity extends AppCompatActivity {

    String date;
    String getWeight;
    TextView txtDate, txtMonth, txtWeight, txtTargetKcal ,txtExercise, txtEatTotalKcal;

    TextView txtBreakfast, txtLunch, txtDinner;

    Diary diary = new Diary();
    ProgressBar progressBarExercise, progressBarFood;
    ProgressBar progressBarCarbs,progressBarProtein,progressBarFat;

    TextView txtGetCarbs ,txtGetProtein,txtGetFat, txtExerciseKcal;

    FoodEatData foodEatData = new FoodEatData();
    UserTargetGet userTargetGet = new UserTargetGet();
    ExerciseTotalkcal exerciseTotalkcal = new ExerciseTotalkcal();

    String targetKcal;

    // 유저가 총 섭취한 탄단지 합

    int eatTotalCarbs ,eatTotalProtein ,eatTotalFat,eatTotalKcal;
    int targetCarbs, targetProtein, targetFat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_day);

        txtWeight = findViewById(R.id.txtWeight);
        txtDate = findViewById(R.id.txtDate);
        txtMonth = findViewById(R.id.txtMonth);

        txtTargetKcal = findViewById(R.id.txtTargetKcal);
        txtExercise=findViewById(R.id.txtExercise);

        txtBreakfast = findViewById(R.id.txtBreakfast);
        txtLunch = findViewById(R.id.txtLunch);
        txtDinner = findViewById(R.id.txtDinner);

        progressBarExercise = findViewById(R.id.progressBarExercise);
        progressBarFood = findViewById(R.id.progressBarFood);

        progressBarCarbs = findViewById(R.id.progressBarCarbs);
        progressBarProtein = findViewById(R.id.progressBarProtein);
        progressBarFat = findViewById(R.id.progressBarFat);

        txtGetCarbs = findViewById(R.id.txtGetCarbs);
        txtGetProtein = findViewById(R.id.txtGetProtein);
        txtGetFat = findViewById(R.id.txtGetFat);

        txtEatTotalKcal = findViewById(R.id.txtEatTotalKcal);

        txtExerciseKcal = findViewById(R.id.txtExerciseKcal);


        // 요일 정보 받아오기
        // 2023-03-26
        date = getIntent().getStringExtra("date");






        Log.i("NOWDATE_DIARY", date);

        txtMonth.setText(date.substring(0,3+1)+ "년 " + date.substring(5, 6+1) + "월 다이어리");
        txtDate.setText(date.substring(0,3+1)+ "년 " + date.substring(5, 6+1) + "월 " + date.substring(8, 9+1) + "일");

        txtWeight.setPaintFlags(txtWeight.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);



        // 유저 목표 칼로리 (회원가입할때 입력한 kcal 정보임) 텍스트뷰에 나타내기
        SharedPreferences sharedPreferences = getSharedPreferences(Config.PREFERENCE_NAME, SelectedPostingActivity.MODE_PRIVATE); // mode_private : 해당 앱에서만 사용
        targetKcal = sharedPreferences.getString(Config.TARGET_KCAL, "1.1");
        txtTargetKcal.setText("/"+Math.round(Double.parseDouble(targetKcal)));


        // 몸무게 입력
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

        // 아침 버튼 클릭시
        // 아침 음식 화면으로 이동
        // 해당 다이어리의 날짜 데이터를 넘겨준다
        txtBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedDayActivity.this, SelectedBreakfastFoodActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        txtLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedDayActivity.this, SelectedLunchFoodActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        txtDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedDayActivity.this, SelectedDinnerFoodActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });

        txtExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedDayActivity.this, SelectedExerciseActivity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeightNetworkData();
        getKcalNetworkData();
        getKcalLunchNetworkData();
        getKcalDinnerNetworkData();
        getUserTargetNetwork();
        getFoodEatData();
        getExerciseTotalKcal();

    }



    // 아침에 총 섭취한 칼로리 가져오기
    private void getKcalNetworkData() {

        Log.i("DATETEST", date);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        FoodApi api = retrofit.create(FoodApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<TotalKcalRes> call = api.getTotalBreakfastKcal(accessToken, date);

        call.enqueue(new Callback<TotalKcalRes>() {

            @SuppressLint({"ResourceAsColor", "ResourceType"})
            @Override
            public void onResponse(Call<TotalKcalRes> call, Response<TotalKcalRes> response) {


                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.

                    try {
                        String getBreakfastKcal =  response.body().getTotalKcal().getTotalKcal();

                        if (getBreakfastKcal.isEmpty()){
                            return;
                        } else {
                            txtBreakfast.setText(getBreakfastKcal + "\nkcal");
                            txtBreakfast.setBackground(ContextCompat.getDrawable(SelectedDayActivity.this, R.drawable.color_shape));
                            txtBreakfast.setTextColor(ContextCompat.getColor(SelectedDayActivity.this, R.color.white));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<TotalKcalRes> call, Throwable t) {
                Log.i("다이어리", t.getMessage());
            }
        });






    }

    // 점심에 총 섭취한 칼로리 가져오기
    private void getKcalLunchNetworkData() {

        Log.i("DATETEST", date);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        FoodApi api = retrofit.create(FoodApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<TotalKcalRes> call = api.getTotalLunchKcal(accessToken, date);

        call.enqueue(new Callback<TotalKcalRes>() {

            @SuppressLint({"ResourceAsColor", "ResourceType"})
            @Override
            public void onResponse(Call<TotalKcalRes> call, Response<TotalKcalRes> response) {


                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.

                    try {
                        String getLunchKcal =  response.body().getTotalKcal().getTotalKcal();

                        if (getLunchKcal.isEmpty()){
                            return;
                        } else {
                            txtLunch.setText(getLunchKcal + "\nkcal");
                            txtLunch.setBackground(ContextCompat.getDrawable(SelectedDayActivity.this, R.drawable.color_shape));
                            txtLunch.setTextColor(ContextCompat.getColor(SelectedDayActivity.this, R.color.white));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<TotalKcalRes> call, Throwable t) {
                Log.i("다이어리", t.getMessage());
            }
        });






    }

    // 저녁에 총 섭취한 칼로리 가져오기
    private void getKcalDinnerNetworkData() {

        Log.i("DATETEST", date);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        FoodApi api = retrofit.create(FoodApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<TotalKcalRes> call = api.getTotalDinnerKcal(accessToken, date);

        call.enqueue(new Callback<TotalKcalRes>() {

            @SuppressLint({"ResourceAsColor", "ResourceType"})
            @Override
            public void onResponse(Call<TotalKcalRes> call, Response<TotalKcalRes> response) {


                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.

                    try {
                        String getDinnerKcal =  response.body().getTotalKcal().getTotalKcal();

                        if (getDinnerKcal.isEmpty()){
                            return;
                        } else {
                            txtDinner.setText(getDinnerKcal + "\nkcal");
                            txtDinner.setBackground(ContextCompat.getDrawable(SelectedDayActivity.this, R.drawable.color_shape));
                            txtDinner.setTextColor(ContextCompat.getColor(SelectedDayActivity.this, R.color.white));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<TotalKcalRes> call, Throwable t) {
                Log.i("다이어리", t.getMessage());
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
                Log.i("다이어리", t.getMessage());
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

    // 유저가 섭취한 탄단지와 칼로리 합 가져오기
    void getFoodEatData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        DiaryApi api = retrofit.create(DiaryApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<FoodEatDataRes> call = api.getFoodEatData(accessToken, date);

        call.enqueue(new Callback<FoodEatDataRes>() {

            @Override
            public void onResponse(Call<FoodEatDataRes> call, Response<FoodEatDataRes> response) {


                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.

                    try {

                        foodEatData = response.body().getFoodEatData();

                        eatTotalKcal = foodEatData.getTotalKcal();
                        eatTotalCarbs = foodEatData.getTotalCarbs();
                        eatTotalProtein = foodEatData.getTotalProtein();
                        eatTotalFat = foodEatData.getTotalFat();

                        Log.i("EATDATATEST", eatTotalKcal + " " + eatTotalCarbs + " " + eatTotalProtein + " " + eatTotalFat);

                        progressBarFood.setMax((int) Math.round(Double.parseDouble(targetKcal)));
                        progressBarFood.setProgress(eatTotalKcal);

                        txtEatTotalKcal.setText(eatTotalKcal + "");

                        txtGetCarbs.setText(eatTotalCarbs + "g");
                        txtGetProtein.setText(eatTotalProtein + "g");
                        txtGetFat.setText(eatTotalFat + "g");

                        progressBarCarbs.setProgress(eatTotalCarbs);
                        progressBarProtein.setProgress(eatTotalProtein);
                        progressBarFat.setProgress(eatTotalFat);


                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<FoodEatDataRes> call, Throwable t) {
                Log.i("다이어리", t.getMessage());
            }
        });

    }

    // 유저 목표 탄단지 데이터 가져오기
    void getUserTargetNetwork(){
        // 유저 목표 탄단지 데이터 가져오기
        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedDayActivity.this);

        DiaryApi api = retrofit.create(DiaryApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<UserTargetGetRes> call1 = api.getUserTargetGet(accessToken);

        call1.enqueue(new Callback<UserTargetGetRes>() {

            @Override
            public void onResponse(Call<UserTargetGetRes> call1, Response<UserTargetGetRes> response) {


                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.

                    try {

                        userTargetGet = response.body().getUserTargetGet();


                        targetCarbs = userTargetGet.getTargetCarbs();
                        targetProtein = userTargetGet.getTargetProtein();
                        targetFat = userTargetGet.getTargetFat();

                        Log.i("TARGETDATATEST", targetCarbs + " " + targetProtein + " " + targetFat + " " + eatTotalFat);

                        progressBarCarbs.setMax(targetCarbs);
                        progressBarProtein.setMax(targetProtein);
                        progressBarFat.setMax(targetFat);


                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<UserTargetGetRes> call, Throwable t) {
                Log.i("다이어리", t.getMessage());
            }
        });

    }

    // 유저가 특정날짜에 운동한 데이터 총 합 가져오기
    void getExerciseTotalKcal(){
        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedDayActivity.this);

        ExerciseApi api = retrofit.create(ExerciseApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<ExerciseTotalkcalRes> call1 = api.ExerciseTotalkcal(accessToken, date);

        call1.enqueue(new Callback<ExerciseTotalkcalRes>() {

            @Override
            public void onResponse(Call<ExerciseTotalkcalRes> call1, Response<ExerciseTotalkcalRes> response) {


                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.

                    try {

                        exerciseTotalkcal = response.body().getItem();


                        // 프로그레스바에 나타냄
                        progressBarExercise.setMax(1500);
                        progressBarExercise.setProgress((int)Math.round(exerciseTotalkcal.getExerciseDateKcal()));

                        txtExerciseKcal.setText(Math.round(exerciseTotalkcal.getExerciseDateKcal()) + "");


                        // 아래 원에 나타내는 용도
                        txtExercise.setText(Math.round(exerciseTotalkcal.getExerciseDateKcal()) + "\nkcal");
                        txtExercise.setBackground(ContextCompat.getDrawable(SelectedDayActivity.this, R.drawable.color_shape));
                        txtExercise.setTextColor(ContextCompat.getColor(SelectedDayActivity.this, R.color.white));


                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<ExerciseTotalkcalRes> call, Throwable t) {
                Log.i("다이어리", t.getMessage());
            }
        });

    }

}