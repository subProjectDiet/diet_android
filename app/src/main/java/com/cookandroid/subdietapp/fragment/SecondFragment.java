package com.cookandroid.subdietapp.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cookandroid.subdietapp.CalendarDecor.SatDecor;
import com.cookandroid.subdietapp.CalendarDecor.SundayDecor;
import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.api.DiaryApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.diary.DiaryMonthRes;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SecondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    TextView txtWeight ,txtKcal, txtExercise;


    MaterialCalendarView calendarView;

    Context context;
    private String accessToken;
    private String selectedDay;



    public SecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_second, container, false);
        calendarView = rootView.findViewById(R.id.calendarview);
        txtWeight = rootView.findViewById(R.id.txtWeight);
        txtKcal=rootView.findViewById(R.id.txtKcal);
        txtExercise=rootView.findViewById(R.id.txtExercise);

        //일요일 색칠
        SundayDecor sundayDecor = new SundayDecor(context);
        calendarView.addDecorator(sundayDecor);

        //토요일 색칠
        SatDecor satDecor = new SatDecor(context);
        calendarView.addDecorator(satDecor);

//        //평일 진하게
//        BoldDecor boldDecor = new BoldDecor(context);
//        calendarView.addDecorator(boldDecor);


        // 연/월 순서 바꾸기
        calendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                // 선택한 날짜의 월과 연도를 포맷팅하여 반환
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM", Locale.getDefault());
                return sdf.format(day.getDate());
            }
        });


        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                int selectedMonth = Integer.parseInt(String.valueOf(date.getMonth())) +1;
                int selectedYear = Integer.parseInt(String.valueOf(date.getYear()));

                if(selectedMonth <10 ){
                 selectedDay = String.valueOf(selectedYear+"-0"+selectedMonth);
                } else if (selectedMonth >10) {
                    selectedDay = String.valueOf(selectedYear+"-"+selectedMonth);
                }


                Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
                DiaryApi api = retrofit.create(DiaryApi.class);

                SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE);
                accessToken = "Bearer "  + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴

                Call<DiaryMonthRes> call = api.getDiaryMonth(accessToken,selectedDay);

                call.enqueue(new Callback<DiaryMonthRes>() {
                    @Override
                    public void onResponse(Call<DiaryMonthRes> call, Response<DiaryMonthRes> response) {


                        if(response.isSuccessful()){


//                            String today = response.body().getItems().get(0).getDate();
//                            String selectedDate = String.valueOf(date.getDate());
//                            Log.i(TAG,"DATE확인"+selectedDate);
//
//
//                            if(today ==  selectedDate){
//
//                                double dayexercise =response.body().getItems().get(0).getExerciseKcal();
//                                double dayweight =response.body().getItems().get(0).getNowWeight();
//                                double dayfood =response.body().getItems().get(0).getFoodKcal();
//                                txtExercise.setText(String.valueOf(dayexercise));
//                                txtWeight.setText(String.valueOf(dayweight));
//                                txtKcal.setText(String.valueOf(dayfood));
//                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<DiaryMonthRes> call, Throwable t) {
                        Log.d("TAG", "onFailure: " + t.getMessage());
                    }
                });




            }
        });




        return rootView;
    }




}