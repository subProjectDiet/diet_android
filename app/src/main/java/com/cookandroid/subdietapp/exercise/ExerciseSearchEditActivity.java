package com.cookandroid.subdietapp.exercise;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.content.DialogInterface;
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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.api.ExerciseApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.exercise.ExerciseRecord;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExerciseSearchEditActivity extends AppCompatActivity {

    TextView txtExercise;
    EditText editExerciseTime , editKcalBurn;

    ImageView imgBack ,btnminus,btnplus;
    Button btnDel,btnEdit;
    private String exerciseName;
    private int exerciseTime;
    private double totalKcalBurn;
    private int recordType;

    private double excutekcal;
    private double realWeight;

    private int exerciseRecordId;
    ArrayList<ExerciseRecord> exerciseRecordsList = new ArrayList<>();
    private String date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_search_edit);

        editKcalBurn=findViewById(R.id.editKcalBurn);
        editExerciseTime=findViewById(R.id.editExerciseTime);
        txtExercise=findViewById(R.id.txtExercise);
        btnDel=findViewById(R.id.btnDel);
        btnEdit=findViewById(R.id.btnEdit);
        btnplus=findViewById(R.id.btnplus);
        btnminus=findViewById(R.id.btnminus);
        imgBack=findViewById(R.id.imgBack);

        exerciseName=getIntent().getStringExtra("exerciseName");
        totalKcalBurn=getIntent().getDoubleExtra("totalKcal", 0);
        exerciseTime = getIntent().getIntExtra("exerciseTime", 0);
        exerciseRecordId = getIntent().getIntExtra("exerciseId", 0);

        txtExercise.setText(exerciseName);
        editExerciseTime.setText(String.valueOf(exerciseTime));
        editKcalBurn.setText(String.valueOf(totalKcalBurn));

        date = getIntent().getStringExtra("date");

        //검색을통해 받은데이터로 저장한경우 + 버튼눌러서 직접작성한 경우 if문으로 나눠놈
        //1.검색을통한 테이블내 데이터로 작성한거 수정
        //칼로리에 소수점이있고 그숫자가 2자리면 검색을 통해 얻은 데이터로 작성한것
        String recordTypeCheck = Double.toString(totalKcalBurn);
        if (recordTypeCheck.substring(recordTypeCheck.indexOf('.') + 1).length() == 2) {
            Log.i(TAG,"첫번째 경우 진입");
            //소수점 자리가 있음
            //레코드타입 0(테이블)
            recordType=0;
            //1분당 자기몸무계가 이운동을 소화한 칼로리
            realWeight = totalKcalBurn/exerciseTime;

            //칼로리 사용금지
            editKcalBurn.setEnabled(false);

            

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

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    getNetworkData();

                }
            });

        //2.직접입력한 데이터 수정
        // 소수점 자리가 없음
        } else {
            Log.i(TAG,"두번째 경우 진입");

            //레코드타입 1 유저가 직접입력한 경우
            recordType = 1;

            //정수로 만들어줌(그냥 냅두면 소수점도 쓸 수 있음)
            editKcalBurn.setText(String.format("%.0f", totalKcalBurn));
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

        }

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getNetworkData();

            }
        });



        //삭제
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExerciseSearchEditActivity.this);
                builder.setTitle("정보 삭제");
                builder.setMessage("정말 삭제하시겠습니까?");
                builder.setNegativeButton("NO",null);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        deleteNetworkData(exerciseRecordId);
                    }
                }); builder.show();
            }
        });


        imgBack.setOnClickListener(v -> {
            finish();
        });




    }

    //작성한 운동 칼로리 항목 삭제
    private void deleteNetworkData(int exerciseRecordId) {
        Retrofit retrofit = NetworkClient.getRetrofitClient(new ExerciseSearchEditActivity());
        ExerciseApi api = retrofit.create(ExerciseApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<Res> call = api.ExerciseUserDelete(accessToken, exerciseRecordId);
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    int index = getIntent().getIntExtra("exerciseIndex", 0);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {

            }
        });

    }

    //운동 칼로리 수정하는 API
    private void getNetworkData() {

        Log.i(TAG,"수정화면 진입");
        

        Retrofit retrofit = NetworkClient.getRetrofitClient(ExerciseSearchEditActivity.this);

        ExerciseApi api = retrofit.create(ExerciseApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        exerciseName =txtExercise.getText().toString().trim();
        totalKcalBurn = Double.parseDouble(editKcalBurn.getText().toString().trim());
        String strTime=editExerciseTime.getText().toString().trim();
        exerciseTime = Integer.parseInt(strTime);


        ExerciseRecord exerciseRecord = new ExerciseRecord();
        exerciseRecord.setExerciseName(exerciseName);
        exerciseRecord.setExerciseTime(exerciseTime);
        exerciseRecord.setTotalKcalBurn(totalKcalBurn);
        exerciseRecord.setDate(date);



        Call<Res> call = api.ExerciseUserModify(accessToken,exerciseRecord,exerciseRecordId,recordType);

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


