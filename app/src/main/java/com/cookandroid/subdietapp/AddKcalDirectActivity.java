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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.api.FoodApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.food.FoodAdd;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddKcalDirectActivity extends AppCompatActivity {


    EditText editFoodName, editGram, editKcal;

    ImageView imgBack;
    Button btnSave;

    ImageButton imgBtnMinus;
    ImageView imgBtnPlus;
    TextView txtCarbs, txtProtein ,txtFat;

    String kcal;

    String foodName, gram;

    String date;
    int mealtime;
    int mealtimeLunch;
    private int visionfat;
    private int visioncarbs;
    private int visongram;
    private int visionprotein;
    private String vsionKcal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kcal_direct);

        editKcal = findViewById(R.id.editKcal);
        editFoodName = findViewById(R.id.editFoodName);
        editGram = findViewById(R.id.editGram);

        imgBtnMinus = findViewById(R.id.imgBtnMinus);
        imgBtnPlus = findViewById(R.id.imgBtnPlus);

        btnSave = findViewById(R.id.btnSave);

        txtCarbs = findViewById(R.id.txtCarbs);
        txtProtein = findViewById(R.id.txtProtein);
        txtFat = findViewById(R.id.txtFat);

        imgBack = findViewById(R.id.imgBack);
//        "foodName": "닭볶음탕",
//                "gram": 50,
//                "kcal": 500,
//                "mealtime": 0,
//                "date": "2023-03-27"

//        + , - 버튼 누를때마다 50 kcal씩 증가 또는 감소

        // 요일 데이터 받아오기
        date = getIntent().getStringExtra("date");
        mealtime = Integer.parseInt(getIntent().getStringExtra("mealtime"));

        foodName = editFoodName.getText().toString().trim();
        gram = editGram.getText().toString().trim();
        kcal = editKcal.getText().toString().trim();


        visioncarbs = Integer.parseInt(getIntent().getStringExtra("carbs"));
        visongram = Integer.parseInt(getIntent().getStringExtra("gram"));
        visionprotein = Integer.parseInt(getIntent().getStringExtra("protein"));
        visionfat = Integer.parseInt(getIntent().getStringExtra("fat"));
        vsionKcal = getIntent().getStringExtra("kcal");

        Log.i("확인하기", "carbs : " + visioncarbs + " date : "  + date + " mealtime : " + mealtime);


        editKcal.setText((vsionKcal));
        editGram.setText(Integer.toString(visongram));
        txtCarbs.setText(Integer.toString(visioncarbs));
        txtProtein.setText(Integer.toString(visionprotein));
        txtFat.setText(Integer.toString(visionfat));




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

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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
                    txtCarbs.setText(Math.round(Setkcal * 0.5 / 4) + "");
                    txtProtein.setText(Math.round(Setkcal * 0.3 / 4) + "");
                    txtFat.setText(Math.round(Setkcal * 0.2 / 9 )+ "");

                }catch (Exception e){

                }

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodName = editFoodName.getText().toString().trim();
                gram = editGram.getText().toString().trim();
                kcal = editKcal.getText().toString().trim();

                if (foodName.isEmpty()){
                    Toast.makeText(AddKcalDirectActivity.this, "음식명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(kcal.isEmpty()){
                    Toast.makeText(AddKcalDirectActivity.this, "칼로리를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;}

                if (gram.isEmpty()){
                    gram = "";
                }

                getNetworkData();

            }
        });



    }

    private void getNetworkData() {


        Retrofit retrofit = NetworkClient.getRetrofitClient(AddKcalDirectActivity.this);

        FoodApi api = retrofit.create(FoodApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        //    {
//        "foodName": "닭볶음탕",
//            "gram": 50,
//            "kcal": 500,
//            "mealtime": 0,
//            "date": "2023-03-27"
//    }

        FoodAdd foodAdd = new FoodAdd(foodName, Double.parseDouble(gram), Double.parseDouble(kcal), mealtime ,date );


        // recordType 은 유저가 칼로리 데이터를 직접 추가했으면 1
        // 검색결과로 추가했으면 2
        Call<Res> call = api.addFoodDirect(accessToken, 1 ,foodAdd);

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