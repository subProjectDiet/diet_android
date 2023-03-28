package com.cookandroid.subdietapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.api.FoodApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.food.Food;
import com.cookandroid.subdietapp.model.food.FoodAdd;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FoodEditActivity extends AppCompatActivity {

    EditText editFoodName, editGram, editKcal, editCarbs, editProtein, editFat;
    Button btnEdit, btnDelete;

    ImageButton imgBtnMinus;
    ImageView imgBtnPlus, imgBack;

    int foodRecordId;
    String date;
    int mealtime;

    Food food = new Food();


    String foodName;
    Double gram, kcal;
    int carbs, protein, fat;

    // 데이터를 삭제하기 위한 변수
    private int deleteIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_edit);

        imgBtnMinus = findViewById(R.id.imgBtnMinus);
        imgBtnPlus = findViewById(R.id.imgBtnPlus);

        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        imgBack = findViewById(R.id.imgBack);

        editFoodName = findViewById(R.id.editFoodName);
        editGram = findViewById(R.id.editGram);
        editKcal = findViewById(R.id.editKcal);
        editCarbs = findViewById(R.id.editCarbs);
        editProtein = findViewById(R.id.editProtein);
        editFat = findViewById(R.id.editFat);




        foodRecordId = Integer.parseInt(getIntent().getStringExtra("foodRecordId"));
        date = getIntent().getStringExtra("date");
        mealtime = Integer.parseInt(getIntent().getStringExtra("mealtime"));

        Log.i("DATAGETTEST", "foodRecordId :" + foodRecordId + " date : " + date + " mealtime : " + mealtime);

        food = (Food) getIntent().getSerializableExtra("food");

//        EditText editFoodName, editGram, editKcal, editCarbs, editProtein, editFat;
        editFoodName.setText(food.getFoodName());
        editGram.setText(food.getGram());
        editKcal.setText(food.getKcal());
        editCarbs.setText(food.getCarbs());
        editProtein.setText(food.getProtein());
        editFat.setText(food.getFat());


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

        // 데이터 수정
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                foodName = editFoodName.getText().toString().trim();
                kcal = Double.valueOf(editKcal.getText().toString().trim());
                gram = Double.valueOf(editGram.getText().toString().trim());
                carbs = Integer.parseInt(editCarbs.getText().toString().trim());
                protein = Integer.parseInt(editProtein.getText().toString().trim());
                fat = Integer.parseInt(editFat.getText().toString().trim());


                getNetworkData();

            }
        });

        // 데이터 삭제
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                foodRecordId = Integer.parseInt(getIntent().getStringExtra("foodRecordId"));


                // 알러트 다이얼로그를 띄움
                AlertDialog.Builder builder = new AlertDialog.Builder(FoodEditActivity.this);
                builder.setTitle("칼로리 삭제");
                builder.setMessage("정말 삭제 하시겠습니까?");
                // 예 버튼을 누르면 삭제
                builder.setPositiveButton("예", (dialogInterface, i) -> { // 람다식
                    // 서버에 로그아웃 요청
                    getDeletePosting();

                });
                // 아니오 버튼을 누르면 아무일도 일어나지 않음
                builder.setNegativeButton("아니오", null);
                builder.show();
            }
        });




    }

    private void getDeletePosting() {

        foodRecordId = Integer.parseInt(getIntent().getStringExtra("foodRecordId"));


//        deleteIndex = index;

        Retrofit retrofit = NetworkClient.getRetrofitClient(FoodEditActivity.this);
        FoodApi api = retrofit.create(FoodApi.class);

//        food.getId();

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<Res> call = api.deleteFoodRecord(accessToken, foodRecordId);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {

                if (response.isSuccessful()){

                    Log.i("FOODEDIT", "칼로리가 삭제 되었습니다.");

                    finish();
                } else {
                    Log.i("FOODEDIT", "정상동작하지 않습니다.");
                    return;
                }
            }
            @Override
            public void onFailure(Call<Res> call, Throwable t) {

            }
        });
    }


    private void getNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(FoodEditActivity.this);


        FoodApi api = retrofit.create(FoodApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");



        FoodAdd foodAdd = new FoodAdd(foodName, gram, kcal, carbs, protein, fat, mealtime);


        Call<Res> call = api.updateFoodRecord(accessToken, foodRecordId, foodAdd);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {

                if (response.isSuccessful()){

                    Log.i("FOODEDIT", "칼로리가 수정 되었습니다.");

                    finish();
                } else {
                    Log.i("FOODEDIT", "정상동작하지 않습니다.");
                    return;
                }
            }
            @Override
            public void onFailure(Call<Res> call, Throwable t) {

            }
        });
    }


}