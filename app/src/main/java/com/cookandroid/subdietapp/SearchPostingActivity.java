package com.cookandroid.subdietapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.subdietapp.adapter.PostingAdapter;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.api.PostingApi;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.posting.Posting;
import com.cookandroid.subdietapp.model.posting.PostingRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPostingActivity extends AppCompatActivity {

    ImageView imgSearch;
    EditText editSearch;
    int count;
    int offset = 0;
    int limit = 25;
    private boolean isloading = false;

    Posting posting = new Posting();

    int likeCnt;

    RecyclerView recyclerView;
    PostingAdapter postingAdapter;
    ArrayList<Posting> postingList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_posting);

        imgSearch = findViewById(R.id.imgSearch);
        editSearch = findViewById(R.id.editSearch);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchPostingActivity.this));


        

        String getKeyword = getIntent().getStringExtra("keyword");
        Log.i("KEYWORD_POSTING", getKeyword);
        getNetworkData(getKeyword);

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nowKeyword = editSearch.getText().toString().trim();
                getNetworkData(nowKeyword);

            }
        });








    }

    private void getNetworkData(String keyword) {

        Retrofit retrofit = NetworkClient.getRetrofitClient(SearchPostingActivity.this);

        PostingApi api = retrofit.create(PostingApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<PostingRes> call = api.getTagPosting(accessToken ,offset, limit, keyword);
        call.enqueue(new Callback<PostingRes>() {
            @Override
            public void onResponse(Call<PostingRes> call, Response<PostingRes> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                postingList.clear();


                if (response.isSuccessful()) {

                    count = response.body().getCount();

                    offset = offset + count;

                    postingList.addAll(response.body().getItems());

                    postingAdapter = new PostingAdapter(SearchPostingActivity.this, postingList);

                    recyclerView.setAdapter(postingAdapter);


                } else {

                }

            }

            @Override
            public void onFailure(Call<PostingRes> call, Throwable t) {

            }
        });
    }


}