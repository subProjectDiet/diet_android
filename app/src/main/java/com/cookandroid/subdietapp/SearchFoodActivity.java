package com.cookandroid.subdietapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.subdietapp.adapter.FoodAdapter;
import com.cookandroid.subdietapp.api.FoodApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.food.Food;
import com.cookandroid.subdietapp.model.food.FoodRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchFoodActivity extends AppCompatActivity {

    String keyword;

    EditText editSearch;
    Button btnSearch, btnAdd;
    ImageView imgBack;

    RecyclerView recyclerView;
    FoodAdapter foodAdapter;
    ArrayList<Food> foodList = new ArrayList<>();

    // 페이징 처리를 위한 변수
    int count;
    int offset = 0;
    int limit = 25;
    private boolean isloading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);

        editSearch = findViewById(R.id.editSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnAdd = findViewById(R.id.btnAdd);
        imgBack = findViewById(R.id.imgBack);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchFoodActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(SearchFoodActivity.this, 1));

        keyword = getIntent().getStringExtra("keyword");
        Log.i("KEYWORDTEST", keyword + "");


        getNetworkData(keyword);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchKeyword = editSearch.getText().toString().trim();
                getNetworkData(searchKeyword);



            }
        });




        // 뒤로가기 버튼
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }



    private void getNetworkData(String keyword) {

        Log.i("KEYWORDTEST2", keyword + "");

        Retrofit retrofit = NetworkClient.getRetrofitClient(SearchFoodActivity.this);

        FoodApi api = retrofit.create(FoodApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<FoodRes> call = api.getSearchFood(accessToken, keyword , offset, limit);
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

                    foodAdapter = new FoodAdapter(SearchFoodActivity.this, foodList);
                    recyclerView.setAdapter(foodAdapter);


                } else {


                }

            }

            @Override
            public void onFailure(Call<FoodRes> call, Throwable t) {

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 맨 마지막 데이터가 화면에 보이면!!
                // 네트워크 통해서 데이터를 추가로 받아와라!!
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                // 스크롤을 데이터 맨 끝까지 한 상태
                if (lastPosition + 1 == totalCount && !isloading) {
                    // 네트워크 통해서 데이터를 받아오고, 화면에 표시!
                    isloading = true;
                    addNetworkData(keyword);


                }


            }
        });


    }

    private void addNetworkData(String keyword) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(SearchFoodActivity.this);

        FoodApi api = retrofit.create(FoodApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        Call<FoodRes> call = api.getSearchFood(accessToken, keyword , offset, limit);
        call.enqueue(new Callback<FoodRes>() {
            @Override
            public void onResponse(Call<FoodRes> call, Response<FoodRes> response) {

                if (response.isSuccessful()) {

                    offset = offset + count;

                    foodList.addAll(response.body().getItems());

                    foodAdapter.notifyDataSetChanged();
                    isloading = false;


                }
            }

            @Override
            public void onFailure(Call<FoodRes> call, Throwable t) {

            }
        });
    }




}