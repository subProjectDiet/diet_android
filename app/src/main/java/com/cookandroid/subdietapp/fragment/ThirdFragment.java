package com.cookandroid.subdietapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cookandroid.subdietapp.AddPostingActivity;
import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.SearchPostingActivity;
import com.cookandroid.subdietapp.SelectedPostingActivity;
import com.cookandroid.subdietapp.adapter.PostingAdapter;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.api.PostingApi;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.posting.Posting;
import com.cookandroid.subdietapp.model.posting.PostingRes;
import com.cookandroid.subdietapp.model.posting.Recommend;
import com.cookandroid.subdietapp.model.posting.RecommendRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    PostingAdapter postingAdapter;
    ArrayList<Posting> postingList = new ArrayList<>();

    Spinner spinner;
    EditText editSearch;
    ImageButton imgSearch;
    CardView cardView;

    String order;
    // 페이징 처리를 위한 변수
    int count;
    int offset = 0;
    int limit = 25;


    private boolean isloading = false;


    TextView txtRecommend, txtNickName, txtDate, txtLike, txtContent;
    ImageButton imgLike;

    ImageView imgPhoto;

    Recommend recommend = new Recommend();

    FloatingActionButton fab;

    String keyword;






    public ThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        txtRecommend = view.findViewById(R.id.txtRecommend);
        txtNickName = view.findViewById(R.id.txtNickName);
        txtDate = view.findViewById(R.id.txtDate);
        txtLike = view.findViewById(R.id.txtLike);
        txtContent = view.findViewById(R.id.txtContent);
        imgLike = view.findViewById(R.id.imgLike);
        imgPhoto = view.findViewById(R.id.imgPhoto);

        spinner = view.findViewById(R.id.spinner);
        imgSearch = view.findViewById(R.id.imgSearch);
        editSearch = view.findViewById(R.id.editSearch);

        cardView = view.findViewById(R.id.cardView);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fab = view.findViewById(R.id.fab);

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
                    addNetworkData();


                }


            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddPostingActivity.class);
                startActivity(intent);
            }
        });


        getRecommendNetworkData();

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword = editSearch.getText().toString().trim();

                if (keyword.isEmpty()){
                    return;
                }

                Intent intent1 = new Intent(getActivity(), SearchPostingActivity.class);
                intent1.putExtra("keyword", keyword);
                startActivity(intent1);


            }
        });


        // 스피너 처리 관련 코드
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.posting, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setSelection(0);

        //스피너에 어댑터 적용
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    order = "likeCnt";
                } else if (position == 1) {
                    order = "createdAt";

                }
                Log.i("SPINNER", position + "");
                getNetworkData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { // 아무것도 선택하지 않았을때
            }

        });




        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        getNetworkData();
    }

    private void getNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());

        PostingApi api = retrofit.create(PostingApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        offset = 0;
        count = 0;

        Call<PostingRes> call = api.getPosting(accessToken, order, offset, limit);
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

                    postingAdapter = new PostingAdapter(getActivity(), postingList);
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

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());

        PostingApi api = retrofit.create(PostingApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<PostingRes> call = api.getPosting(accessToken, order, offset, limit);
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


    private void getRecommendNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        PostingApi api = retrofit.create(PostingApi.class);


        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<RecommendRes> call = api.getRecommend(accessToken);

        call.enqueue(new Callback<RecommendRes>() {

            @Override
            public void onResponse(Call<RecommendRes> call, Response<RecommendRes> response) {
                if (response.isSuccessful()) {
                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.
                    try {

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE); // mode_private : 해당 앱에서만 사용
                        String nickname = sharedPreferences.getString(Config.NICKNAME, ""); // 저장된 닉네임을 가져옴 (없으면 ""을 가져옴)

                        txtRecommend.setText(nickname + "님을 위한 추천 포스팅");

                        recommend = response.body().getRecommend();

                        txtNickName.setText(recommend.getNickName());

                        // glide로 이미지 뿌려주기
                        Glide.with(getActivity())
                                .load(recommend.getImgUrl().replace("http","https"))
                                .placeholder(R.drawable.outline_insert_photo_24)
                                .into(imgPhoto);



                        if (recommend.getIsLike() == 1) {
                            imgLike.setImageResource(R.drawable.baseline_favorite_24);
                        }

                        txtContent.setText(recommend.getContent());
                        txtLike.setText(recommend.getLikeCnt() + "");
                        txtDate.setText( recommend.getCreatedAt().substring(0, 9+1) + " " + recommend.getCreatedAt().substring(11, 18+1) );
                        txtLike.setText(recommend.getLikeCnt()+"");




                        imgPhoto.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), SelectedPostingActivity.class);
                                intent.putExtra("postingId", recommend.getPostingId());
                                startActivity(intent);
                            }
                        });

                        cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), SelectedPostingActivity.class);
                                intent.putExtra("postingId", recommend.getPostingId()+"");
                                startActivity(intent);
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();


                    }

                }
            }

            @Override
            public void onFailure(Call<RecommendRes> call, Throwable t) {
            }
        });



    }


}