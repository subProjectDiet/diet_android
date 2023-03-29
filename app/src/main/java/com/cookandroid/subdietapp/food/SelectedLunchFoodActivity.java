package com.cookandroid.subdietapp.food;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.subdietapp.AddKcalDirectActivity;
import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.SearchFoodActivity;
import com.cookandroid.subdietapp.adapter.FoodLunchAdapter;
import com.cookandroid.subdietapp.api.FoodApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.food.Food;
import com.cookandroid.subdietapp.model.food.FoodRes;
import com.cookandroid.subdietapp.model.food.TotalKcal;
import com.cookandroid.subdietapp.model.food.TotalKcalRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedLunchFoodActivity extends AppCompatActivity {
    ImageView imgBack;
    Button btnAdd, btnSearch, btnEnd;
    TextView txtTotalKcal;
    EditText editSearch;
    RecyclerView recyclerView;
    FoodLunchAdapter foodAdapter;
    ArrayList<Food> foodList = new ArrayList<>();
    public String date;

    TotalKcal totalKcal = new TotalKcal();


    // 페이징 처리를 위한 변수
    int count;
    int offset = 0;
    int limit = 25;

    public int mealtime = 1;

    public static Context lunchContext;

    private boolean isloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_lunch_food);

        btnAdd = findViewById(R.id.btnAdd);
        btnSearch = findViewById(R.id.btnSearch);
        btnEnd = findViewById(R.id.btnEnd);

        imgBack = findViewById(R.id.imgBack);

        editSearch = findViewById(R.id.editSearch);

        txtTotalKcal = findViewById(R.id.txtTotalKcal);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectedLunchFoodActivity.this));

        recyclerView.addItemDecoration(new DividerItemDecoration(SelectedLunchFoodActivity.this, 1));

        // 어댑터로 데이터를 보내기 위한 변수
        lunchContext = this;

        // 다이어리 페이지에서 요일 정보 받아오기
        // 2023-03-26
        date = getIntent().getStringExtra("date");

        Log.i("DATETEST", date+"");

        // 1. 칼로리 직접 추가로 이동
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedLunchFoodActivity.this, AddKcalDirectActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("mealtime", mealtime + "");
                startActivity(intent);
            }
        });

        // 2. 검색으로 칼로리 추가
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword = editSearch.getText().toString().trim();

                if (keyword.isEmpty()){
                    return;
                }


                Intent intent = new Intent(SelectedLunchFoodActivity.this, SearchFoodActivity.class);
                intent.putExtra("keyword", keyword);
                intent.putExtra("mealtime", mealtime + "");
                intent.putExtra("date", date);

                startActivity(intent);

            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
    @Override
    protected void onResume() {
        super.onResume();
        getNetworkData();
        getKcalNetworkData();
    }

    // 점심에 섭취한 칼로리 총합을 가져온다
    private void getKcalNetworkData() {

        Log.i("DATETEST", date);

        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        FoodApi api = retrofit.create(FoodApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<TotalKcalRes> call = api.getTotalLunchKcal(accessToken, date);

        call.enqueue(new Callback<TotalKcalRes>() {

            @Override
            public void onResponse(Call<TotalKcalRes> call, Response<TotalKcalRes> response) {


                if (response.isSuccessful()) {


                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.


                    try {

//                        response.body().getTotalKcal().getTotalKcal();

                        txtTotalKcal.setText("("+response.body().getTotalKcal().getTotalKcal() + "kcal)");

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

    private void getNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedLunchFoodActivity.this);

        FoodApi api = retrofit.create(FoodApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<FoodRes> call = api.getLunchKcal(accessToken, date , offset, limit);
        call.enqueue(new Callback<FoodRes>() {

            @Override
            public void onResponse(Call<FoodRes> call, Response<FoodRes> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                foodList.clear();


                if (response.isSuccessful()) {

                    count = response.body().getCount();

                    offset = offset + count;

                    foodList.addAll(response.body().getItems());

                    foodAdapter = new FoodLunchAdapter(SelectedLunchFoodActivity.this, foodList);
                    recyclerView.setAdapter(foodAdapter);


                } else {


                }

            }

            @Override
            public void onFailure(Call<FoodRes> call, Throwable t) {

            }
        });


    }









}