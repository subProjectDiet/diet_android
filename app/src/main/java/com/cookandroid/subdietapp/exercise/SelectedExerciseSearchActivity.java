package com.cookandroid.subdietapp.exercise;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.adapter.ExerciseSearchAdapter;
import com.cookandroid.subdietapp.api.ExerciseApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.exercise.Exercise;
import com.cookandroid.subdietapp.model.exercise.ExerciseRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedExerciseSearchActivity extends AppCompatActivity {


    Button btnSearch , btnCal;
    EditText editSearch;
    ImageView imgBack;


    String keyword;
    int count = 0;
    int offset = 0;
    int limit = 30;

    public static Context ExerContext;
    public String date;


    RecyclerView recyclerView;
    ArrayList<Exercise> exercisesList = new ArrayList<>();
    ExerciseSearchAdapter exercisSearchAdapter;
    private boolean isloading = false;
    private String keyword2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_exercise_search);

        btnCal=findViewById(R.id.btnCal);
        btnSearch=findViewById(R.id.btnSearch);
        editSearch=findViewById(R.id.editSearch);
        imgBack=findViewById(R.id.imgBack);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectedExerciseSearchActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(SelectedExerciseSearchActivity.this, 1));

        ExerContext=this;

        date = getIntent().getStringExtra("date");

        // 전페이지에서(selectedexerciseactivity) 검색했던거 바로 띄워줌
        keyword = getIntent().getStringExtra("keyword");
        editSearch.setText(keyword);
        if(keyword !=null){
            searchKeyword();
        }


        //추가 검색
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                keyword= editSearch.getText().toString();
                if (keyword.isEmpty()){
                    Toast.makeText(SelectedExerciseSearchActivity.this, "검색어를 입력해주세요", Toast.LENGTH_SHORT).show();
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

                            searchKeyword();

                        }
                    }

                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                    }
                });


            }
        });

        //직접추가버튼 누를경우
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectedExerciseSearchActivity.this, ExerciseSearchAddActivity.class);
                intent.putExtra("date",date);
                startActivity(intent);
            }
        });

        imgBack.setOnClickListener(v -> {
            Intent intent = new Intent(SelectedExerciseSearchActivity.this, SelectedExerciseActivity.class);
            startActivity(intent);
            finish();
        });

    }

    //검색api
    private void searchKeyword() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedExerciseSearchActivity.this);

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
                    exercisSearchAdapter = new ExerciseSearchAdapter(SelectedExerciseSearchActivity.this, exercisesList);
                    recyclerView.setAdapter(exercisSearchAdapter);

                }
            }

            @Override
            public void onFailure(Call<ExerciseRes> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }



}