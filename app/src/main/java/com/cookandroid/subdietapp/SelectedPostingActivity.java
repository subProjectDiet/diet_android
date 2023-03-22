package com.cookandroid.subdietapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.subdietapp.api.LikeApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.posting.PostingInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPostingActivity extends AppCompatActivity {

    TextView txtNickName, txtDate, txtLike, txtContent;

    ImageView imgPhoto, imgLike, imgBack;

    EditText editComment;
    ImageButton imgComment;
    RecyclerView recyclerView;

    private String accessToken;
    int postingId;


    PostingInfo postingInfo = new PostingInfo();

    public static int state = 0;
    int likeCnt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_posting);

        txtNickName = findViewById(R.id.txtNickName);
        txtDate = findViewById(R.id.txtDate);
        txtLike = findViewById(R.id.txtLike);
        txtContent = findViewById(R.id.txtContent);

        imgPhoto = findViewById(R.id.imgPhoto);
        imgLike = findViewById(R.id.imgLike);
        imgBack = findViewById(R.id.imgBack);

        editComment = findViewById(R.id.editComment);
        imgComment = findViewById(R.id.imgComment);
        recyclerView = findViewById(R.id.recyclerView);

        // 선택한 알콜 아이디 뽑아내는 코드
        postingId = getIntent().getIntExtra("postingId", 0);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //  하트의 상태를 변화시킨다.
        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

                if (imgLike.isSelected()) { // 하트가 채워져 있다면
                    imgLike.setSelected(false); // 하트가 비워진다.

                    imgLike.setImageResource(R.drawable.baseline_favorite_border_24);
                    // 비동기로 네트워크 실행
                    Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedPostingActivity.this);
                    LikeApi api = retrofit.create(LikeApi.class);
                    Call<Void> call = api.deleteLike(accessToken, postingId);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                // 상태가변화됨을 감지하는 변수
                                state = 1;
                                // txtLikeCnt 에도 하트가 눌렸을 때의 숫자를 반영한다.
                                likeCnt = likeCnt - 1;
                                txtLike.setText(Integer.toString(likeCnt));
                                Log.i("하트", "하트가 눌렸습니다.");
                                Log.i("액티비티스탯3", state + "");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.i("하트", "하트가 눌리지 않았습니다.");
                        }
                    });


                } else { // 하트가 비워져 있다면
                    imgLike.setSelected(true); // 하트가 채워진다
                    imgLike.setImageResource(R.drawable.baseline_favorite_24);
                    // 비동기로 네트워크 실행
                    Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedPostingActivity.this);
                    LikeApi api = retrofit.create(LikeApi.class);
                    Call<Void> call = api.postLike(accessToken, postingId);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                state = 1;
                                // txtLikeCnt 에도 하트가 눌렸을 때의 숫자를 반영한다.
                                likeCnt = likeCnt + 1;
                                txtLike.setText(Integer.toString(likeCnt));
                                Log.i("액티비티스탯2", state + "");
                                Log.i("하트", "하트가 눌렸습니다.");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.i("하트", "하트가 눌리지 않았습니다.");
                        }
                    });
                }
            }
        });





    }

}