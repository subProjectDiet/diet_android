package com.cookandroid.subdietapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.SelectedDayActivity;
import com.cookandroid.subdietapp.api.DiaryApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.diary.Diary;
import com.cookandroid.subdietapp.model.diary.DiaryExerciseBurn;
import com.cookandroid.subdietapp.model.diary.DiaryExerciseBurnRes;
import com.cookandroid.subdietapp.model.diary.DiaryRes;
import com.cookandroid.subdietapp.model.diary.TotalKcal;
import com.cookandroid.subdietapp.model.diary.TotalKcalRes;
import com.cookandroid.subdietapp.posting.SelectedPostingActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    LinearLayout layout1;
    String nowDate;
    Diary diary = new Diary();

    TotalKcal totalKcal = new TotalKcal();

    DiaryExerciseBurn diaryExerciseBurn = new DiaryExerciseBurn();

    TextView txtWeight, txtTargetKcal, txtGetTotalKcal,txtBurnKcal,txtSetText, txtEatKcal;
    String getTotalKcal = "0";
    String targetKcal = "0";

    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
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
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        layout1 = view.findViewById(R.id.layout1);

        txtWeight = view.findViewById(R.id.txtWeight);
        txtTargetKcal = view.findViewById(R.id.txtTargetKcal);
        txtGetTotalKcal = view.findViewById(R.id.txtGetTotalKcal);
        txtBurnKcal = view.findViewById(R.id.txtBurnKcal);
        txtSetText = view.findViewById(R.id.txtSetText);
        txtEatKcal = view.findViewById(R.id.txtEatKcal);


        // 유저가 회원가입할때 작성했던 targetkcal 데이터를 sp 에서 가져온다
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, SelectedPostingActivity.MODE_PRIVATE); // mode_private : 해당 앱에서만 사용
        targetKcal = sharedPreferences.getString(Config.TARGET_KCAL, "");

        Log.i("TEXTTEST", targetKcal + " " );

        // 목표 몸무게 셋팅
        txtTargetKcal.setText(Math.round(Float.parseFloat(targetKcal)) + " kcal");



        // 날짜를 가져오는 코드
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        nowDate = sdf.format(date);

        Log.i("NOWDATE", nowDate+"");





        // 첫번째 레이아웃 클릭시 다이어리 화면으로 이동
        // 메인엑티비티는 오늘 날짜에 대한 정보를 보여주고 클릭시 오늘날짜의 다이어리로 이동하므로
        // 현재 시간을 selectedDayActivity 로 데이터를 보낸다
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SelectedDayActivity.class);

                // 여기서 오늘 날짜를 다이어리 페이지에 넘겨줌
                intent.putExtra("date", nowDate);
                startActivity(intent);
            }
        });





        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        getWeightNetworkData();
        getTotalKcalNetworkData();
        getBurnKcalNetworkData();
    }
    // 오늘 몸무게 가져오는 API
    private void getWeightNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());

        DiaryApi api = retrofit.create(DiaryApi.class);


        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<DiaryRes> call = api.setDiaryWeight(accessToken, nowDate);

        call.enqueue(new Callback<DiaryRes>() {

            @Override
            public void onResponse(Call<DiaryRes> call, Response<DiaryRes> response) {


                if (response.isSuccessful()) {

                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.


                    try {

                        diary = response.body().getDiary();


                        String nowWeight = diary.getNowWeight();

                        Log.i("NOWWEIGHT_TEST", nowWeight);

                        txtWeight.setText(nowWeight);


                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<DiaryRes> call, Throwable t) {
                Log.i("다이어리", t.getMessage());
            }
        });






    }
    // 오늘 섭취한 칼로리 총 합 가져오기
    private void getTotalKcalNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());

        DiaryApi api = retrofit.create(DiaryApi.class);


        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<TotalKcalRes> call = api.getTotalKcal(accessToken, nowDate);

        call.enqueue(new Callback<TotalKcalRes>() {

            @Override
            public void onResponse(Call<TotalKcalRes> call, Response<TotalKcalRes> response) {


                if (response.isSuccessful()) {

                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.


                    try {


                        totalKcal = response.body().getTotalKcal();


                        getTotalKcal = totalKcal.getTotalKcal();

                        Log.i("getTotalKcal", getTotalKcal);

                        txtGetTotalKcal.setText(getTotalKcal + " kcal");

                        // 목표보타 000kcal 초과됐어요 부분에 데이터를 나타내기 위한 코드
                        Double sumData =  Double.parseDouble(getTotalKcal) - Double.parseDouble(targetKcal);
                        Log.i("SUMDATA", targetKcal + " " + getTotalKcal);
                        txtSetText.setText("목표보다 " + Math.round(sumData) + " kcal 초과됐어요" );

                        // 오늘 000kcal 먹었어요 부분에 나타내주는 데이터
                        txtEatKcal.setText(getTotalKcal+" kcal\n먹었어요");

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<TotalKcalRes> call, Throwable t) {
                Log.i("다이어리", t.getMessage());
            }
        });






    }
    // 오늘 소모한 운동 칼로리 가져오기
    private void getBurnKcalNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());

        DiaryApi api = retrofit.create(DiaryApi.class);


        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");

        Call<DiaryExerciseBurnRes> call = api.getExerciseBurnTotalKcal(accessToken, nowDate);

        call.enqueue(new Callback<DiaryExerciseBurnRes>() {

            @Override
            public void onResponse(Call<DiaryExerciseBurnRes> call, Response<DiaryExerciseBurnRes> response) {


                if (response.isSuccessful()) {

                    // 사용자가 너무빨리 뒤로가기를 눌렀을때 에러가 발생한다.
                    // 이를 방지하기 위해 try catch문을 사용한다.


                    try {


                        diaryExerciseBurn = response.body().getDiaryExerciseBurn();


                        Double burnKcal = diaryExerciseBurn.getExerciseDateKcal();

                        Log.i("burnKcal", burnKcal+"");

                        txtBurnKcal.setText(Math.round(burnKcal) + " kcal");


                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<DiaryExerciseBurnRes> call, Throwable t) {
                Log.i("다이어리", t.getMessage());
            }
        });






    }

}