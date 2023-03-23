package com.cookandroid.subdietapp.exercise;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.cookandroid.subdietapp.ExerciseAdapter;
import com.cookandroid.subdietapp.ExerciseSearchAdapter;
import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.SelectedDayActivity;
import com.cookandroid.subdietapp.api.ExerciseApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.Exercise;
import com.cookandroid.subdietapp.model.ExerciseRes;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedExerciseActivity extends AppCompatActivity {

    Button btnCal,brnSearch , button6;
    EditText editSearch;
    ImageView imgBack;

    String keyword;
    int count = 0;
    int offset = 0;
    int limit = 30;

    private boolean isloading = false;

    private String date;

    RecyclerView recyclerView;
    ExerciseAdapter exerciseAdapter;
    ArrayList<Exercise> exercisesList = new ArrayList<>();
    ExerciseSearchAdapter exercisSearchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_exercise);

        btnCal=findViewById(R.id.btnCal);
        brnSearch=findViewById(R.id.brnSearch);
        editSearch=findViewById(R.id.editSearch);
        imgBack=findViewById(R.id.imgBack);
        button6=findViewById(R.id.button6);

//        date = getIntent().getStringExtra("date");


        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectedExerciseActivity.this));
        recyclerView.setHasFixedSize(true);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);



            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int totalCount = recyclerView.getItemDecorationCount();
                if(lastPosition+1 == totalCount){
                    // 네트워크 통해서 데이터를 더 불러온다.

                        NetworkData();


                }


            }


        });

        //운동 직접입력
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedExerciseActivity.this, ExerciseSearchAddActivity.class);
                startActivity(intent);
            }
        });


        brnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = editSearch.getText().toString().trim();

                if (keyword.isEmpty()){
                    return;
                }

                searchKeyword();

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                        int totalCount = recyclerView.getAdapter().getItemCount();

                        if (lastPosition + 1 == totalCount && !isloading) {
                            // 네트워크 통해서 데이터를 받아오고, 화면에 표시!
                            isloading = true;

                            addSearchKeyword();

                        }

                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                });


            }
        });


        //등록버튼
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedExerciseActivity.this, SelectedDayActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private void addSearchKeyword() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedExerciseActivity.this);

        ExerciseApi api = retrofit.create(ExerciseApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        Call<ExerciseRes> call = api.searchExercise(accessToken,keyword,offset,limit);
        call.enqueue(new Callback<ExerciseRes>() {
            @Override
            public void onResponse(Call<ExerciseRes> call, Response<ExerciseRes> response) {

                if (response.isSuccessful()) {

                    offset = offset + count;


                    exercisesList.addAll(response.body().getItems());

                    exercisSearchAdapter.notifyDataSetChanged();
                    isloading = false;


                }
            }

            @Override
            public void onFailure(Call<ExerciseRes> call, Throwable t) {



            }
        });



    }

    private void searchKeyword() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedExerciseActivity.this);

        ExerciseApi api = retrofit.create(ExerciseApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<ExerciseRes> call = api.searchExercise(accessToken,keyword,offset,limit);
        call.enqueue(new Callback<ExerciseRes>() {
            @Override
            public void onResponse(Call<ExerciseRes> call, Response<ExerciseRes> response) {
                exercisesList.clear();

                if (response.isSuccessful()){
                    count = response.body().getCount();
                    offset = offset + count;
                    exercisesList.addAll(response.body().getItems());
                    exercisSearchAdapter = new ExerciseSearchAdapter(SelectedExerciseActivity.this, exercisesList);
                    recyclerView.setAdapter(exercisSearchAdapter);

                }
            }

            @Override
            public void onFailure(Call<ExerciseRes> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });



    }

    private void NetworkData() {

        Log.i(TAG,"시작합니다");

        // 임시 오늘날짜
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);



        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedExerciseActivity.this);

        ExerciseApi api = retrofit.create(ExerciseApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<ExerciseRes> call = api.dailyExercise(accessToken,dateString);
        call.enqueue(new Callback<ExerciseRes>() {
            @Override
            public void onResponse(Call<ExerciseRes> call, Response<ExerciseRes> response) {
                exercisesList.clear();

                if(response.isSuccessful()){
                    exercisesList.addAll(response.body().getItems());
                    Log.i(TAG,"가저옴"+exercisesList.addAll(response.body().getItems()));


                    exerciseAdapter = new ExerciseAdapter(SelectedExerciseActivity.this,exercisesList);
                    recyclerView.setAdapter(exerciseAdapter);




                }else{
                    Log.i(TAG,"하나도 못가저옴");
                }

            }

            @Override
            public void onFailure(Call<ExerciseRes> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });




    }
}