package com.cookandroid.subdietapp.posting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.api.PostingApi;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.posting.PostingInfo;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditPostingActivity extends AppCompatActivity {

    int postingId;

    EditText editContent;

    ImageButton txtSave;
    ImageView imgPhoto, imgBack;
    File photoFile;
    String content;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_posting);



        PostingInfo postingInfo = (PostingInfo) getIntent().getSerializableExtra("postingInfo");


        postingId =  Integer.parseInt( getIntent().getStringExtra("postingId") );

        Log.i("POSTINGINFOTEST", postingId+"");


        setContentView(R.layout.activity_add_posting);

        editContent = findViewById(R.id.editContent);
        txtSave = findViewById(R.id.txtSave);
        imgPhoto = findViewById(R.id.imgPhoto);
        imgBack = findViewById(R.id.imgBack);

        // 사진은 수정 불가능
        // 콘텐츠만 수정 가능
        editContent.setText(postingInfo.getContent());

        // glide로 이미지 뿌려주기
        Glide.with(EditPostingActivity.this)
                .load(postingInfo.getImgurl().replace("http","https"))
                .placeholder(R.drawable.outline_insert_photo_24)
                .into(imgPhoto);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                content = editContent.getText().toString();
                Retrofit retrofit = NetworkClient.getRetrofitClient(EditPostingActivity.this);


                PostingApi api = retrofit.create(PostingApi.class);

                SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
                String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

                postingInfo.setContent(content);



                Call<Res> call = api.updatePosting(accessToken, Integer.parseInt(postingId + ""),postingInfo);

                call.enqueue(new Callback<Res>() {
                    @Override
                    public void onResponse(Call<Res> call, Response<Res> response) {

                        if (response.isSuccessful()){
                            Toast.makeText(EditPostingActivity.this, "게시글이 수정 되었습니다.", Toast.LENGTH_SHORT).show();

                            finish();
                        } else {
                            Toast.makeText(EditPostingActivity.this, "정상동작 하지 않습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    @Override
                    public void onFailure(Call<Res> call, Throwable t) {

                    }
                });

            }
        });






    }
}