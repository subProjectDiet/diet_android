package com.cookandroid.subdietapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.api.EdaApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.eda.AvgData;
import com.cookandroid.subdietapp.model.eda.AvgDataRes;
import com.cookandroid.subdietapp.model.eda.BurnKcal;
import com.cookandroid.subdietapp.model.eda.BurnKcalRes;
import com.cookandroid.subdietapp.model.eda.EatFoodKcal;
import com.cookandroid.subdietapp.model.eda.EatFoodKcalRes;
import com.cookandroid.subdietapp.model.eda.EatManyday;
import com.cookandroid.subdietapp.model.eda.EatManydayRes;
import com.cookandroid.subdietapp.model.eda.ExerciseManyday;
import com.cookandroid.subdietapp.model.eda.ExerciseManydayRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class EdaActivity extends AppCompatActivity {

    TextView txtMonthReport;

    TextView EatKcalAvgCount, EatManyDay,ExerciseManyDay,FoodAvgKcal,ExerciseAvgKcal,
            AvgWeight,MonthBurnKcal, EatAvgCPF;

    String nowMonth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eda);

        txtMonthReport = findViewById(R.id.txtMonthReport);

        EatKcalAvgCount = findViewById(R.id.EatKcalAvgCount);

        EatManyDay = findViewById(R.id.EatManyDay);

        ExerciseManyDay = findViewById(R.id.ExerciseManyDay);

        FoodAvgKcal = findViewById(R.id.FoodAvgKcal);

        ExerciseAvgKcal = findViewById(R.id.ExerciseAvgKcal);

        AvgWeight = findViewById(R.id.AvgWeight);

        MonthBurnKcal = findViewById(R.id.MonthBurnKcal);

        EatAvgCPF = findViewById(R.id.EatAvgCPF);

        nowMonth = getIntent().getStringExtra("nowMonth");

        Log.i("NOWMONTH2", nowMonth + "");

        txtMonthReport.setText(nowMonth.substring(5, 6+1) +"월 다이어트 상세 리포트");

        // 가장 많이 먹은 음식 및 칼로리 가져오기
        getFoodKcalNetwork(nowMonth);

        // 가장 많이 먹은 날
        getEatManydayNetwork(nowMonth);

        // 운동을 가장 많이 한 날
        getExerciseManydayNetwork(nowMonth);
        
        // 평균 음식 칼로리, 평균 운동, 평균 몸무게 , 평균 영양소별 섭취량 가져오기
        getAvgKcalDataNetwork(nowMonth);

        // 감량한 칼로리
        getBurnKcalNetwork(nowMonth);
        
        
        

    }

    private void getBurnKcalNetwork(String month) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        EdaApi api = retrofit.create(EdaApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<BurnKcalRes> call = api.getBurnData(accessToken, month);

        call.enqueue(new Callback<BurnKcalRes>() {

            @Override
            public void onResponse(Call<BurnKcalRes> call, Response<BurnKcalRes> response) {
                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.
                    try {

                        BurnKcal burnKcal = new BurnKcal();


                        burnKcal = response.body().getBurnKcal();


                        MonthBurnKcal.setText(burnKcal.getBurnWeight() + " kg");






                    } catch (Exception e) {
                        e.printStackTrace();


                    }

                }
            }

            @Override
            public void onFailure(Call<BurnKcalRes> call, Throwable t) {
                Log.i("포스팅 정보", t.getMessage());
            }
        });
    }

    private void getAvgKcalDataNetwork(String month) {

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        EdaApi api = retrofit.create(EdaApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<AvgDataRes> call = api.getAvgKcal(accessToken, month);

        call.enqueue(new Callback<AvgDataRes>() {

            @Override
            public void onResponse(Call<AvgDataRes> call, Response<AvgDataRes> response) {
                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.
                    try {

                        AvgData avgData = new AvgData();

                        avgData = response.body().getAvgData();

                        // 평균 음식 칼로리
                        avgData.getAvgKcal();
                        String StrAvgKcal = String.format("%.2f" , avgData.getAvgKcal());


                        // 평균 운동 칼로리
                        avgData.getAvgKcalBurn();
                        String StrAvgKcalBurn = String.format("%.2f" , avgData.getAvgKcalBurn());

                        // 평균 몸무게
                        avgData.getAvgWeight();
                        String StrAvgWeight = String.format("%.1f" , avgData.getAvgWeight());

                        // 평균 탄
                        avgData.getAvgCarbs();
                        String StrAvgCarbs = String.format("%.1f" , avgData.getAvgCarbs());

                        // 평균 단
                        avgData.getAvgProtein();
                        String StrAvgProtein = String.format("%.1f" , avgData.getAvgProtein());

                        // 평균 지
                        avgData.getAvgFat();
                        String StrAvgFat = String.format("%.1f" , avgData.getAvgFat());

//                        ,FoodAvgKcal,ExerciseAvgKcal,
//                                AvgWeight, EatAvgCPF;

                        FoodAvgKcal.setText(StrAvgKcal + " kcal");
                        ExerciseAvgKcal.setText(StrAvgKcalBurn + "kcal");
                        AvgWeight.setText(StrAvgWeight + " kg");

//                        탄수화물 (140.3g) | 단백질 (38.2g) | 지방 (34.9g)
                        EatAvgCPF.setText("탄수화물 (" + StrAvgCarbs + "g) | 단백질(" +
                                StrAvgProtein + "g) | 지방 (" + StrAvgFat + "g)");


                    } catch (Exception e) {
                        e.printStackTrace();


                    }

                }
            }

            @Override
            public void onFailure(Call<AvgDataRes> call, Throwable t) {
                Log.i("포스팅 정보", t.getMessage());
            }
        });

    }

    private void getExerciseManydayNetwork(String month) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        EdaApi api = retrofit.create(EdaApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<ExerciseManydayRes> call = api.getExerciseManyDay(accessToken, month);

        call.enqueue(new Callback<ExerciseManydayRes>() {

            @Override
            public void onResponse(Call<ExerciseManydayRes> call, Response<ExerciseManydayRes> response) {
                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.
                    try {

                        ExerciseManyday exerciseManyday = new ExerciseManyday();

                        exerciseManyday = response.body().getExerciseManyday();

                        Double ExTotalKcal = exerciseManyday.getMonthTotal();

                        String StrExTotalKcal = String.format("%.2f" , ExTotalKcal);


//                        2023-03-02 (150 kcal)
                        ExerciseManyDay.setText(exerciseManyday.getDate() + " (" +
                                StrExTotalKcal + " kcal)");






                    } catch (Exception e) {
                        e.printStackTrace();


                    }

                }
            }

            @Override
            public void onFailure(Call<ExerciseManydayRes> call, Throwable t) {
                Log.i("포스팅 정보", t.getMessage());
            }
        });

    }

    private void getEatManydayNetwork(String month) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        EdaApi api = retrofit.create(EdaApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<EatManydayRes> call = api.getEatManyDay(accessToken, month);

        call.enqueue(new Callback<EatManydayRes>() {

            @Override
            public void onResponse(Call<EatManydayRes> call, Response<EatManydayRes> response) {
                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.
                    try {

                        EatManyday eatManyday = new EatManyday();
                        eatManyday = response.body().getEatManyday();

//                        2023-03-03 (1187 kcal)
                        EatManyDay.setText(eatManyday.getDate() + " (" + eatManyday.getKcal()
                         + " kcal)");

                    } catch (Exception e) {
                        e.printStackTrace();


                    }

                }
            }

            @Override
            public void onFailure(Call<EatManydayRes> call, Throwable t) {
                Log.i("포스팅 정보", t.getMessage());
            }
        });


    }

    private void getFoodKcalNetwork(String month) {

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        EdaApi api = retrofit.create(EdaApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<EatFoodKcalRes> call = api.getEatFoodKcal(accessToken, month);

        call.enqueue(new Callback<EatFoodKcalRes>() {

            @Override
            public void onResponse(Call<EatFoodKcalRes> call, Response<EatFoodKcalRes> response) {
                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.
                    try {

                        EatFoodKcal eatFoodKcal = new EatFoodKcal();

                        eatFoodKcal = response.body().getEatFoodKcal();

                        EatKcalAvgCount.setText(eatFoodKcal.getFoodName() + " | " + eatFoodKcal.getKcal() + "kcal | " +
                                eatFoodKcal.getCnt() + "회");







                    } catch (Exception e) {
                        e.printStackTrace();


                    }

                }
            }

            @Override
            public void onFailure(Call<EatFoodKcalRes> call, Throwable t) {
                Log.i("포스팅 정보", t.getMessage());
            }
        });


    }


}