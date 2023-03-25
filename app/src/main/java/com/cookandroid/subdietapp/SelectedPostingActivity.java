package com.cookandroid.subdietapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.subdietapp.adapter.ComentAdapter;
import com.cookandroid.subdietapp.api.LikeApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.api.PostingApi;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.posting.Coment;
import com.cookandroid.subdietapp.model.posting.ComentRes;
import com.cookandroid.subdietapp.model.posting.PostingInfo;
import com.cookandroid.subdietapp.model.posting.PostingInfoRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPostingActivity extends AppCompatActivity {

    private int deleteIndex;

    TextView txtNickName, txtDate, txtLike, txtContent;

    ImageView imgPhoto, imgLike, imgBack;

    EditText editComment;
    ImageButton imgComment;
    RecyclerView recyclerView;
    ComentAdapter comentAdapter;
    ArrayList<Coment> comentList = new ArrayList<>();

    int count;
    int offset = 0;
    int limit = 25;

    private boolean isloading = false;

    ImageView btnMenu;

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

        btnMenu = findViewById(R.id.btnMenu);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectedPostingActivity.this));

        // 스크롤 내렸을 때 데이터 추가로 가져오는 코드
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 맨 마지막 데이터가 화면에 보이면!!
                // 네트워크 통해서 데이터를 추가로 받아와라!!
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int totalCount = recyclerView.getAdapter().getItemCount();

                // 스크롤을 데이터 맨 끝까지 한 상태
                if (lastPosition + 1 == totalCount && !isloading) {
                    // 네트워크 통해서 데이터를 받아오고, 화면에 표시!
                    isloading = true;
                    addComentListNetworkData();
                }
            }
        });


        // 선택한 알콜 아이디 뽑아내는 코드
        postingId = getIntent().getIntExtra("postingId", 0);

        // 포스팅 정보 나타내는 함수
        getNetworkData();

        // 댓글 리스트 가져오는 함수
        getComentListNetworkData();


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

                if (getComent.isEmpty()) {
                    return;
                }

                getCommentNetworkData();

                editComment.setText("");

            }
        });


        //  하트의 상태를 변화시킨다.
        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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


        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
                getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // 수정
                        if (menuItem.getItemId() == R.id.action_menu1){

                            Intent intent = new Intent(SelectedPostingActivity.this, EditPostingActivity.class);
                            intent.putExtra("postingId", postingId+"");
                            intent.putExtra("postingInfo", postingInfo);
                            startActivity(intent);

                        // 삭제
                        }else if (menuItem.getItemId() == R.id.action_menu2){

                            // 알러트 다이얼로그를 띄움
                            AlertDialog.Builder builder = new AlertDialog.Builder(SelectedPostingActivity.this);
                            builder.setTitle("게시글 삭제");
                            builder.setMessage("정말 삭제 하시겠습니까?");
                            // 예 버튼을 누르면 삭제
                            builder.setPositiveButton("예", (dialogInterface, i) -> { // 람다식
                                // 서버에 로그아웃 요청
                                getDeletePosting(postingId);

                            });
                            // 아니오 버튼을 누르면 아무일도 일어나지 않음
                            builder.setNegativeButton("아니오", null);
                            builder.show();





                }

                        return false;
                    }
                });
                popupMenu.show();
            }

        });
    }

    private void getDeletePosting(int index) {

        // 네트워크로 메모 삭제하는 코드 작성

        deleteIndex = index;

        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedPostingActivity.this);
        PostingApi api = retrofit.create(PostingApi.class);



        postingInfo.getPostingId();



        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        Call<Res> call = api.deletePosting( accessToken, postingInfo.getPostingId()  );
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()){

                    Toast.makeText(SelectedPostingActivity.this, "게시글이 삭제되었습니다", Toast.LENGTH_SHORT);
                    finish();

                }else {

                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {

            }
        });
    }





    void addComentListNetworkData() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedPostingActivity.this);

        PostingApi api = retrofit.create(PostingApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        Call<ComentRes> call = api.getComent(accessToken, postingId,offset, limit);
        call.enqueue(new Callback<ComentRes>() {
            @Override
            public void onResponse(Call<ComentRes> call, Response<ComentRes> response) {


                if (response.isSuccessful()) {
                    offset = offset + count;


                    comentList.addAll(response.body().getItems());

//                    alcoholAdapter = new AlcoholAdapter(MyRecipeWriteSecondActivity.this, alcoholList);
//                    alcoholRecyclerView.setAdapter(alcoholAdapter);
                    comentAdapter.notifyDataSetChanged();
                    isloading = false;


                }
            }

            @Override
            public void onFailure(Call<ComentRes> call, Throwable t) {

            }
        });


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

    @Override
    protected void onResume() {
        super.onResume();
        getNetworkData();
    }

    // 포스팅 정보 가져오는 API
    private void getNetworkData() {

        postingId = getIntent().getIntExtra("postingId", 0);

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

    // 댓글 리스트 가져오는 API
    void getComentListNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(SelectedPostingActivity.this);

        PostingApi api = retrofit.create(PostingApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<ComentRes> call = api.getComent(accessToken, postingId ,offset, limit);
        call.enqueue(new Callback<ComentRes>() {
            @Override
            public void onResponse(Call<ComentRes> call, Response<ComentRes> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                comentList.clear();


                if (response.isSuccessful()) {

                    count = response.body().getCount();

                    offset = offset + count;

                    comentList.addAll(response.body().getItems());

                    comentAdapter = new ComentAdapter(SelectedPostingActivity.this, comentList);

                    recyclerView.setAdapter(comentAdapter);


                } else {

                }

            }

            @Override
            public void onFailure(Call<ComentRes> call, Throwable t) {

            }
        });
    }





}