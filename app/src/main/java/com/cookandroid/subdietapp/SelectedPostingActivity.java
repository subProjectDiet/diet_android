package com.cookandroid.subdietapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.api.PostingApi;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.posting.Coment;
import com.cookandroid.subdietapp.model.posting.PostingInfo;
import com.cookandroid.subdietapp.model.posting.PostingInfoRes;

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

    Button btnEdit;

    private String accessToken;
    int postingId;
    String getComent;


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


        getNetworkData();

        // 뒤로가기 버튼
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





        //댓글 작성
        imgComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getComent = editComment.getText().toString().trim();

                if (getComent.isEmpty()){
                    return;
                }

                getCommentNetworkData();

                editComment.setText("");

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        getNetworkData();
    }

    // 댓글 작성 함수
    private void getCommentNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedPostingActivity.this);
        PostingApi api = retrofit.create(PostingApi.class); // 레트로핏으로 서버에 요청할 객체 생성

        // 2-2. 토큰 가져오기
        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        // 3. 저장
        Coment coment = new Coment(getComent);

        Call<Res> call = api.addComent(accessToken, postingId,coment); // 서버에 요청
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(@NonNull Call<Res> call, @NonNull Response<Res> response) {

                if(response.isSuccessful()) {


                }else{
                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
            }
        });




    }


    private void getNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);

        PostingApi api = retrofit.create(PostingApi.class);


        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<PostingInfoRes> call = api.getPostingInfo(accessToken, postingId);

        call.enqueue(new Callback<PostingInfoRes>() {

            @Override
            public void onResponse(Call<PostingInfoRes> call, Response<PostingInfoRes> response) {
                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.
                    try {


                        postingInfo = response.body().getPostingInfo();

                        txtNickName.setText(postingInfo.getNickName());


                        // glide로 이미지 뿌려주기
                        Glide.with(SelectedPostingActivity.this)
                                .load(postingInfo.getImgurl().replace("http","https"))
                                .placeholder(R.drawable.outline_insert_photo_24)
                                .into(imgPhoto);



                        if (postingInfo.getIsLike() == 1) {
                            imgLike.setImageResource(R.drawable.baseline_favorite_24);
                        }

                        txtLike.setText(postingInfo.getLikeCnt() + "");
                        txtDate.setText(postingInfo.getCreatedAt().substring(0, 9+1) + " " + postingInfo.getCreatedAt().substring(11, 18+1) + "");
                        txtContent.setText(postingInfo.getContent());


                        likeCnt = postingInfo.getLikeCnt();




                    } catch (Exception e) {
                        e.printStackTrace();


                    }

                }
            }

            @Override
            public void onFailure(Call<PostingInfoRes> call, Throwable t) {
                Log.i("포스팅 정보", t.getMessage());
            }
        });



    
        
    }

}