package com.cookandroid.subdietapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cookandroid.subdietapp.model.posting.PostingInfo;

import java.io.File;

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


        postingId = getIntent().getIntExtra("postingId", 0);


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







    }
}