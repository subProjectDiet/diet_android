package com.cookandroid.subdietapp.exercise;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cookandroid.subdietapp.adapter.ExerciseAdapter;
import com.cookandroid.subdietapp.adapter.ExerciseSearchAdapter;
import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.SelectedDayActivity;
import com.cookandroid.subdietapp.api.ExerciseApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.exercise.Exercise;
import com.cookandroid.subdietapp.model.exercise.ExerciseRecord;
import com.cookandroid.subdietapp.model.exercise.ExerciseRes;
import com.cookandroid.subdietapp.model.exercise.ExerciseTotalkcal;
import com.cookandroid.subdietapp.model.exercise.ExerciseTotalkcalRes;
import com.cookandroid.subdietapp.model.exercise.ExerciserRecordRes;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedExerciseActivity extends AppCompatActivity {

    Button btnCal,btnSearch , btnNext;
    EditText editSearch;
    TextView txtTotalkcal;
    ImageView imgBack;

    String keyword;
    int count = 0;
    int offset = 0;
    int limit = 20;

    private boolean isloading = false;

    private String date;

    RecyclerView recyclerView;
    ExerciseAdapter exerciseAdapter;
    ArrayList<ExerciseRecord> exercisesList = new ArrayList<>();
    private String daytotalkcal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_exercise);

        btnCal=findViewById(R.id.btnCal);
        btnSearch=findViewById(R.id.btnSearch);
        editSearch=findViewById(R.id.editSearch);
        imgBack=findViewById(R.id.imgBack);
        btnNext=findViewById(R.id.btnNext);
        txtTotalkcal=findViewById(R.id.txtTotalkcal);


        //칼로리계산산
       TotalkcalNetworkData();


        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectedExerciseActivity.this));
        recyclerView.setHasFixedSize(true);

        NetworkData();

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

        //검색페이지로 넘어가기
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword=editSearch.getText().toString().trim();
                Intent intent = new Intent(SelectedExerciseActivity.this, SelectedExerciseSearchActivity.class);
                intent.putExtra("keyword", keyword);
                startActivity(intent);
                finish();

            }
        });


        //등록버튼
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedExerciseActivity.this, SelectedDayActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        // 네트워크로부터 변경된 오늘 운동리스트와 토탈칼로리 가저오기
        NetworkData();
        TotalkcalNetworkData();
    }


    //운동리스트 가저오는api
    private void NetworkData() {

        Log.i(TAG,"시작합니다");

        // 임시 오늘날짜
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);

        offset = 0;
        count = 0;


        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedExerciseActivity.this);

        ExerciseApi api = retrofit.create(ExerciseApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<ExerciserRecordRes> call = api.dailyExercise(accessToken,dateString,offset,limit);
        call.enqueue(new Callback<ExerciserRecordRes>() {
            @Override
            public void onResponse(Call<ExerciserRecordRes> call, Response<ExerciserRecordRes> response) {
                exercisesList.clear();

                if(response.isSuccessful()){
                    exercisesList.addAll(response.body().getItems());


                    exerciseAdapter = new ExerciseAdapter(SelectedExerciseActivity.this,exercisesList);
                    recyclerView.setAdapter(exerciseAdapter);

                    exerciseAdapter.notifyDataSetChanged();


                }else{
                    Log.i(TAG,"하나도 못가저옴");
                }

            }

            @Override
            public void onFailure(Call<ExerciserRecordRes> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });




    }

    //토탈 칼로리 api
    private  void TotalkcalNetworkData(){
        Log.i(TAG,"토탈칼로리입니다");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);


        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedExerciseActivity.this);

        ExerciseApi api = retrofit.create(ExerciseApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        Call<ExerciseTotalkcalRes> call =api.ExerciseTotalkcal(accessToken,dateString);
        call.enqueue(new Callback<ExerciseTotalkcalRes>() {
            @Override
            public void onResponse(Call<ExerciseTotalkcalRes> call, Response<ExerciseTotalkcalRes> response) {
                if(response.isSuccessful()){
                    //정수로 바꾸고 문자열로 바꾸고 천의자리 ,
                    int daytotalkcal1= (int) Math.round(response.body().getItem().getExerciseDateKcal());
                    daytotalkcal = String.format("%,d", daytotalkcal1);

                    Log.i(TAG,"토탈칼로리"+daytotalkcal);
                    txtTotalkcal.setText("("+daytotalkcal+"Kcal)");
                }
            }

            @Override
            public void onFailure(Call<ExerciseTotalkcalRes> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });


    }

}