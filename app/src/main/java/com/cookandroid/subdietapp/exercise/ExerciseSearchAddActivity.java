package com.cookandroid.subdietapp.exercise;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import static com.cookandroid.subdietapp.R.color.*;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.SelectedDayActivity;
import com.cookandroid.subdietapp.api.ExerciseApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.exercise.ExerciseRecord;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExerciseSearchAddActivity extends AppCompatActivity {

    EditText editKcalBurn,editExerciseTime,editExercise;

    Button btnSave;
    ImageView btnplus,btnminus,imgBack;

    private int recordType;
    private int exerciseTime;
    private String exerciseName;
    private  double totalKcalBurn;

    private double excutekcal;
    private double realWeight;
    private int exerciseId;
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_search_add);

        editKcalBurn=findViewById(R.id.editKcalBurn);
        editExerciseTime=findViewById(R.id.editExerciseTime);
        editExercise=findViewById(R.id.editExercise);
        btnSave=findViewById(R.id.btnSave);
        btnplus=findViewById(R.id.btnplus);
        btnminus=findViewById(R.id.btnminus);
        imgBack=findViewById(R.id.imgBack);


        date = getIntent().getStringExtra("date");

        //검색을통해 온경우 + 버튼눌러서 직접온경우 if문으로 나눠놈

        //1.검색통해 왔을경우
        //검색api로 받은 id값이 있으면 검색으로 온경우
        exerciseId=getIntent().getIntExtra("id",0);
        if(exerciseId != 0){
            Log.i(TAG,"첫번째 경우 진입");
            exerciseName=getIntent().getStringExtra("exercise");
            totalKcalBurn=getIntent().getDoubleExtra("totalKcalBurn", 0.0);
            editExercise.setText(exerciseName);
            editKcalBurn.setText(String.valueOf(totalKcalBurn));
            recordType = 0;


            //1분당 자기몸무계가 이 운동을 소화한 칼로리
            realWeight=totalKcalBurn/30;

            //맘대로 바꾸지 못하게 막아놈
            editKcalBurn.setEnabled(false);
            editExercise.setEnabled(false);


            //실시간 텍스트왓치
            editExerciseTime.addTextChangedListener(new TextWatcher() {
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
                       double excutekcal1 =  Double.parseDouble(editExerciseTime.getText().toString().trim()) * realWeight;
                       long excutekcal2 = Math.round(excutekcal1 * 100);
                        excutekcal = excutekcal2 / 100.0;
                        editKcalBurn.setText(excutekcal+"");


                    }catch (Exception e){

                    }

                }
            });

            //마이너스버튼
            btnminus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int timeminus = Integer.parseInt(editExerciseTime.getText().toString());
                    if (timeminus > 0) {
                        timeminus = timeminus - 5;
                        editExerciseTime.setText(String.valueOf(timeminus));
                    }
                }
            });
            //플러스버튼
            btnplus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int timeplus = Integer.parseInt(editExerciseTime.getText().toString());
                    timeplus = timeplus + 5;
                    editExerciseTime.setText(String.valueOf(timeplus));
                }
            });



            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getNetworkData();
                }
            });


        }



        //2.칼로리 추가 버튼 눌러 온경우
        // 단순히 intent로 온거라 당연히 exerciseId값이 없음
        else if(exerciseId == 0){
            Log.i(TAG,"두번째 경우 진입");


            //초기화
            exerciseName = "";
            totalKcalBurn=0.0;
            recordType = 1;





            //유저가 칼로리 직접 입력시 소숫점 못적게 만듬
            editKcalBurn.setKeyListener(DigitsKeyListener.getInstance("0123456789"));


            //마이너스버튼
            btnminus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int timeminus = Integer.parseInt(editExerciseTime.getText().toString());
                    if (timeminus > 0) {
                        timeminus--;
                        editExerciseTime.setText(String.valueOf(timeminus));
                    }
                }
            });
            //플러스버튼
            btnplus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int timeplus = Integer.parseInt(editExerciseTime.getText().toString());
                    timeplus++;
                    editExerciseTime.setText(String.valueOf(timeplus));
                }
            });


            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getNetworkData();

                }
            });

        }

        imgBack.setOnClickListener(v -> {
            finish();
        });

    }


    // 운동 칼로리 추가하는 api
    void getNetworkData(){




        Retrofit retrofit = NetworkClient.getRetrofitClient(ExerciseSearchAddActivity.this);

        ExerciseApi api = retrofit.create(ExerciseApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        exerciseName =editExercise.getText().toString().trim();
        totalKcalBurn = Double.parseDouble(editKcalBurn.getText().toString().trim());
        String strTime=editExerciseTime.getText().toString().trim();
        exerciseTime = Integer.parseInt(strTime);


        ExerciseRecord exerciseRecord = new ExerciseRecord();
        exerciseRecord.setExerciseName(exerciseName);
        exerciseRecord.setExerciseTime(exerciseTime);
        exerciseRecord.setTotalKcalBurn(totalKcalBurn);
        exerciseRecord.setDate(date);
        Log.i(TAG,"확인용"+exerciseName+exerciseTime+"날짜"+date);



        Call<Res> call = api.ExerciseUserDirect(accessToken,exerciseRecord,recordType);

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


}