package com.cookandroid.subdietapp.posting;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.adapter.PostingAdapter;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.api.PostingApi;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.posting.Posting;
import com.cookandroid.subdietapp.model.posting.PostingInfo;
import com.cookandroid.subdietapp.model.posting.PostingRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyLikePostingActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    int count;
    int offset = 0;
    int limit = 25;

    private boolean isloading = false;
    ArrayList<Posting> postingList = new ArrayList<>();

    PostingAdapter postingAdapter;

    private String accessToken;
    int postingId;


    PostingInfo postingInfo = new PostingInfo();

    public static int state = 0;
    int likeCnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_like_posting);

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                // 맨 마지막 데이터가 화면에 보이면!!
//                // 네트워크 통해서 데이터를 추가로 받아와라!!
//                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
//                int totalCount = recyclerView.getAdapter().getItemCount();
//
//                // 스크롤을 데이터 맨 끝까지 한 상태
//                if (lastPosition + 1 == totalCount && !isloading) {
//                    // 네트워크 통해서 데이터를 받아오고, 화면에 표시!
//                    isloading = true;
//                    addNetworkData();
//
//
//                }
//
//
//            }
//        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MyLikePostingActivity.this));

        getNetworkData();






    }

    private void getNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MyLikePostingActivity.this);

        PostingApi api = retrofit.create(PostingApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<PostingRes> call = api.getMyLikePosting(accessToken ,offset, limit);
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

                    postingAdapter = new PostingAdapter(MyLikePostingActivity.this, postingList);

                    recyclerView.setAdapter(postingAdapter);


                } else {

                }

            }

            @Override
            public void onFailure(Call<PostingRes> call, Throwable t) {

            }
        });
    }

    private void addNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MyLikePostingActivity.this);

        PostingApi api = retrofit.create(PostingApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<PostingRes> call = api.getMyLikePosting(accessToken, offset, limit);
        call.enqueue(new Callback<PostingRes>() {
            @Override
            public void onResponse(Call<PostingRes> call, Response<PostingRes> response) {

                if (response.isSuccessful()) {
                    offset = offset + count;


                    postingList.addAll(response.body().getItems());

                    postingAdapter.notifyDataSetChanged();
                    isloading = false;


                }
            }

            @Override
            public void onFailure(Call<PostingRes> call, Throwable t) {

            }
        });

    }
}