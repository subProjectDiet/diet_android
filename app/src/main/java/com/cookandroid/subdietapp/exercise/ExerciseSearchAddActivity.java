package com.cookandroid.subdietapp.exercise;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.api.ExerciseApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.Exercise;
import com.cookandroid.subdietapp.model.ExerciseSave;
import com.cookandroid.subdietapp.model.Res;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExerciseSearchAddActivity extends AppCompatActivity {

    EditText editKcalBurn,editExerciseTime,editExercise;

    Button btnSave;

    private int recordType;
    private int exerciseTime;
    private String exerciseName;
    private  int totalKcalBurn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_search_add);

        editKcalBurn=findViewById(R.id.editKcalBurn);
        editExerciseTime=findViewById(R.id.editExerciseTime);
        editExercise=findViewById(R.id.editExercise);
        btnSave=findViewById(R.id.btnSave);






//        String exercise = getIntent().getStringExtra("exercise");
//        float totalKcalBurn = getIntent().getFloatExtra("totalKcalBurn",0.0f);
//        Log.i(TAG,"가저왔어요"+exercise+"칼로리"+totalKcalBurn);

//        if (exercise != null) {
//
//            editExercise.setText(exercise);
//            editKcalBurn.setText((int) totalKcalBurn);
//        }

            exerciseName =editExercise.getText().toString().trim();
           exerciseTime = Integer.parseInt(editExerciseTime.getText().toString().trim());
            totalKcalBurn = Integer.parseInt(editKcalBurn.getText().toString().trim());
            recordType = 1;

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    getNetworkData();


                }
            });







    }

    void getNetworkData(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);



        Retrofit retrofit = NetworkClient.getRetrofitClient(ExerciseSearchAddActivity.this);

        ExerciseApi api = retrofit.create(ExerciseApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");




        ExerciseSave exerciseSave = new ExerciseSave();
        exerciseSave.setExerciseName(exerciseName);
        exerciseSave.setExerciseTime(exerciseTime);
        exerciseSave.setTotalKcalBurn(totalKcalBurn);
        exerciseSave.setDate(dateString);



        Call<Res> call = api.ExerciseUserDirect(accessToken,exerciseSave,recordType);

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