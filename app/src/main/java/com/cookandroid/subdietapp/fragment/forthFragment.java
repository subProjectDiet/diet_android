package com.cookandroid.subdietapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cookandroid.subdietapp.MyLikePostingActivity;
import com.cookandroid.subdietapp.MyWritePostingActivity;
import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.api.UserApi;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.login.LoginActivity;
import com.cookandroid.subdietapp.model.Res;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link forthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class forthFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String accessToken;

    ImageButton btnLogout;

    TextView txtLikePosting, txtMyPosting;



    public forthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment forthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static forthFragment newInstance(String param1, String param2) {
        forthFragment fragment = new forthFragment();
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
//        return inflater.inflate(R.layout.fragment_forth, container, false);
        View view = inflater.inflate(R.layout.fragment_forth, container, false);


        btnLogout = view.findViewById(R.id.btnLogout);

        txtLikePosting = view.findViewById(R.id.txtLikePosting);
        txtMyPosting = view.findViewById(R.id.txtMyPosting);



        // 로그아웃 버튼을 누르면 "정말 로그아웃 하시겠습니까?" 알러트 다이얼로그를 띄움
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("로그아웃");
                builder.setMessage("정말 로그아웃 하시겠습니까?");
                // 예 버튼을 누르면 로그인 화면으로 이동
                builder.setPositiveButton("예", (dialogInterface, i) -> { // 람다식
                    // 서버에 로그아웃 요청
                    getNetworkData();

                });
                // 아니오 버튼을 누르면 아무일도 일어나지 않음
                builder.setNegativeButton("아니오", null);
                builder.show();

            }
        });

        // 내가 좋아요한 게시글 보기로 이동
        txtLikePosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getActivity(), MyLikePostingActivity.class);
                startActivity(intent);
            }
        });


        txtMyPosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getActivity(), MyWritePostingActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    private void getNetworkData(){

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        UserApi api = retrofit.create(UserApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE);
        accessToken = "Bearer "  + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

        Call<Res> call = api.logout(accessToken);

        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {

                if(response.isSuccessful()){
                    // 로그아웃을 하면 토큰과 닉네임을 삭제
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear(); // 모든 데이터 삭제
                    editor.apply(); // 저장

                    // 로그인 화면으로 이동
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().finish(); // 현재 액티비티 종료

                    Toast.makeText(getActivity(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
}