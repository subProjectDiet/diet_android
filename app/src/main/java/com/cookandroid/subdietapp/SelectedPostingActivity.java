package com.cookandroid.subdietapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cookandroid.subdietapp.api.LikeApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.api.PostingApi;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.CommentDTO;
import com.cookandroid.subdietapp.model.Res;
import com.cookandroid.subdietapp.model.posting.PostingInfo;
import com.cookandroid.subdietapp.model.posting.PostingInfoRes;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SelectedPostingActivity extends AppCompatActivity {

    private int deleteIndex;


    // 댓글 관련 변수
    private String CHAT_NAME;
    private String USER_NAME;

    private ListView chatView;

    // 파이어베이스
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    ArrayAdapter<String> adapter;



    TextView txtNickName, txtDate, txtLike, txtContent;

    ImageView imgPhoto, imgLike, imgBack;

    EditText editComment;
    ImageButton imgComment;

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

        chatView = findViewById(R.id.chatView);


        // 선택한 포스팅 아이디 뽑아내는 코드
        postingId = getIntent().getIntExtra("postingId", 0);

        // 댓글에 나타낼 닉네임을 sp 로 가져옴
        SharedPreferences sharedPreferences = getSharedPreferences(Config.PREFERENCE_NAME, SelectedPostingActivity.MODE_PRIVATE); // mode_private : 해당 앱에서만 사용
        USER_NAME = sharedPreferences.getString(Config.NICKNAME, "");



        // 댓글 작성
        setComment(postingId);


        // 포스팅 정보 나타내는 함수
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
                getComent = editComment.getText().toString();

                if (getComent.isEmpty()) {
                    return;
                }

//                getCommentNetworkData();


                CommentDTO chat = new CommentDTO(USER_NAME, getComent); //CommentDTO 를 이용하여 데이터를 묶는다.
                databaseReference.child("chat").child( "postingId"+ postingId + "comment").push().setValue(chat); // 데이터 푸쉬

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

    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        CommentDTO commentDTO = dataSnapshot.getValue(CommentDTO.class);
        adapter.add(USER_NAME + "   " + commentDTO.getMessage());
    }

    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        CommentDTO commentDTO = dataSnapshot.getValue(CommentDTO.class);
        adapter.remove(USER_NAME + "   " + commentDTO.getMessage());
    }

    private void setComment(int postingId) {
        // 리스트 어댑터 생성 및 세팅
        adapter = new ArrayAdapter<String>(this, R.layout.coment_row, R.id.txtContent);
        chatView.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat")
                .child("postingId"+ postingId + "comment")
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, adapter);
                Log.i("CHATBOT_APP", "s:"+s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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







}