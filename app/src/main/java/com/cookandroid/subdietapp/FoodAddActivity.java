package com.cookandroid.subdietapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.api.FoodApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.food.Food;
import com.cookandroid.subdietapp.model.food.FoodAdd;
import com.cookandroid.subdietapp.model.food.FoodOneRes;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FoodAddActivity extends AppCompatActivity {


    EditText editFoodName, editGram, editKcal, editCarbs, editProtein, editFat;
    Button btnSave;

    ImageButton imgBtnMinus;
    ImageView imgBtnPlus, imgBack;

    int foodId;

    String date;
    int mealtime;

    Food food = new Food();

    String foodName;
    Double gram, kcal;
    int carbs, protein, fat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_add);

        imgBtnMinus = findViewById(R.id.imgBtnMinus);
        imgBtnPlus = findViewById(R.id.imgBtnPlus);

        btnSave = findViewById(R.id.btnSave);
        imgBack = findViewById(R.id.imgBack);

        editFoodName = findViewById(R.id.editFoodName);
        editGram = findViewById(R.id.editGram);
        editKcal = findViewById(R.id.editKcal);
        editCarbs = findViewById(R.id.editCarbs);
        editProtein = findViewById(R.id.editProtein);
        editFat = findViewById(R.id.editFat);


//        intent.putExtra("foodId", foodId);
//        intent.putExtra("date", date);
//        intent.putExtra("mealtime", mealtime + "");

        foodId = Integer.parseInt(getIntent().getStringExtra("foodId"));
        date = getIntent().getStringExtra("date");
        mealtime = Integer.parseInt(getIntent().getStringExtra("mealtime"));

        Log.i("GETDATATEST", "foodID : " + foodId + " date : "  + date + " mealtime : " + mealtime);


        getNetworkData();

        //마이너스버튼
        imgBtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double timeminus = Double.parseDouble(editKcal.getText().toString());
                if (timeminus > 0) {
                    timeminus = timeminus - 50;
                    editKcal.setText(String.valueOf(timeminus));
                }
            }
        });


        //플러스버튼
        imgBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double timeplus = Double.parseDouble(editKcal.getText().toString());
                timeplus = timeplus + 50;
                editKcal.setText(String.valueOf(timeplus));
            }
        });


        //실시간 텍스트와치
        editKcal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try{
                    //칼로리 계산
                    Double Setkcal =  Double.parseDouble(editKcal.getText().toString().trim());
//                    long excutekcal2 = Math.round(excutekcal1 * 100);
                    editCarbs.setText(Math.round(Setkcal * 0.5 / 4) + "");
                    editProtein.setText(Math.round(Setkcal * 0.3 / 4) + "");
                    editFat.setText(Math.round(Setkcal * 0.2 / 9 )+ "");

                }catch (Exception e){

                }

            }
        });



        // 버튼 눌렀을 때 저장
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodName = editFoodName.getText().toString().trim();
                gram = Double.valueOf(editGram.getText().toString().trim());
                kcal = Double.valueOf(editKcal.getText().toString().trim());
                carbs = Integer.parseInt(editCarbs.getText().toString().trim());
                protein = Integer.parseInt(editProtein.getText().toString().trim());
                fat = Integer.parseInt(editFat.getText().toString().trim());


                dataAddNetworkData();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    private void dataAddNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(FoodAddActivity.this);

        FoodApi api = retrofit.create(FoodApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

//        {
//            "foodName": "파맛첵스",
//                "gram": 50,
//                "kcal": 30,
//                "carbs": 50,
//                "protein": 20,
//                "fat": 10,
//                "mealtime": 0
//                "date" : "2023-03-28"
//        }

        FoodAdd foodAdd = new FoodAdd(foodName, gram, kcal, carbs, protein, fat, mealtime, date );


        // recordType 은 유저가 칼로리 데이터를 직접 추가했으면 0
        // 검색결과로 추가했으면 1
        Call<Res> call = api.addFoodSearch(accessToken, 0 ,foodAdd);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if(response.isSuccessful()){
                    finish();

                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());

            }
        });
    }

    private void getNetworkData() {


        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        FoodApi api = retrofit.create(FoodApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<FoodOneRes> call = api.getFoodInfo(accessToken, foodId);

        call.enqueue(new Callback<FoodOneRes>() {

            @Override
            public void onResponse(Call<FoodOneRes> call, Response<FoodOneRes> response) {
                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.
                    try {




                        food = response.body().getFood();

//                        EditText editFoodName, editGram, editKcal, editCarbs, editProtein, editFat;

                        editFoodName.setText(food.getFoodName());
                        editGram.setText(food.getGram() + "");
                        editKcal.setText(food.getKcal() + "");
                        editCarbs.setText(food.getCarbs()+"");
                        editProtein.setText(food.getProtein()+"");
                        editFat.setText(food.getFat() + "");

                    } catch (Exception e) {
                        e.printStackTrace();


                    }

                }
            }

            @Override
            public void onFailure(Call<FoodOneRes> call, Throwable t) {
                Log.i("푸드 정보", t.getMessage());
            }
        });

    }
}