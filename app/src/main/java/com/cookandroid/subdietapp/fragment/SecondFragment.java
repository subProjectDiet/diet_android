package com.cookandroid.subdietapp.fragment;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cookandroid.subdietapp.CalendarDecor.SatDecor;
import com.cookandroid.subdietapp.CalendarDecor.SundayDecor;
import com.cookandroid.subdietapp.R;
import com.cookandroid.subdietapp.SelectedDayActivity;
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
    private String dayexercise;
    private String dayfood;
    private String dayweight;
    Button btndiary;
    private String selectedMonth;


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
        btndiary=rootView.findViewById(R.id.btndiary);

        //일요일 색칠
        SundayDecor sundayDecor = new SundayDecor(context);
        calendarView.addDecorator(sundayDecor);

        //토요일 색칠
        SatDecor satDecor = new SatDecor(context);
        calendarView.addDecorator(satDecor);

//        //평일 진하게
//        BoldDecor boldDecor = new BoldDecor(context);
//        calendarView.addDecorator(boldDecor);


        //달력 디자인 원하는걸로 바꾸기 가능(레이아웃만 바꿔주면됨)
//        CustomDecorator customDecorator = new CustomDecorator(context);
//        calendarView.addDecorator((customDecorator));


        // 연/월 순서 바꾸기
        calendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                // 선택한 날짜의 월과 연도를 포맷팅하여 반환
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM", Locale.getDefault());
                return sdf.format(day.getDate());
            }
        });


        // 오늘 날짜 디폴트 설정
        calendarView.setSelectedDate(CalendarDay.today());


        //바로 월별 내기록 api 호출(무분별한 호출 줄이기위해)
        getNetworkData();



        //다이어리로 이동(날짜 받아서 이동)
        //다이어리 넘어갈때 "date" 형식 맞춰서 보내기
        btndiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                CalendarDay selday = calendarView.getSelectedDate();
                if(selday != null){
                    int year = selday.getYear();
                    int month = selday.getMonth() + 1;
                    int day = selday.getDay();

                    if(month <10 ){
                        if(day <10 ){
                            selectedDay = String.valueOf(year+"-0"+month+"-0"+day);
                        }else if(day>= 10){
                            selectedDay = String.valueOf(year+"-0"+month+"-"+day);
                        }

                    } else if (month >=10) {
                        if(day<10){

                        } else if (day>=10) {
                            selectedDay = String.valueOf(year+"-"+month+"-0"+day);
                        }
                        selectedDay = String.valueOf(year+"-"+month+"-"+day);
                    }

                }

                Log.i(TAG,"날짜 형식 확인"+selectedDay);

                Intent intent = new Intent(getActivity(), SelectedDayActivity.class);
                intent.putExtra("date", selectedDay);
                startActivity(intent);

            }
        });


        return rootView;
    }

// 월별 내 운동 몸무게 체중 데이터 가저오기 api
    void getNetworkData(){
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
        DiaryApi api = retrofit.create(DiaryApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(Config.PREFERENCE_NAME, getActivity().MODE_PRIVATE);
        accessToken = "Bearer "  + sp.getString(Config.ACCESS_TOKEN, "");// 액세스 토큰이 없으면 "" 리턴


        //오늘 정보를 통해 api 호출( 2023-03 이런데이터 필요)
        //오늘 정보를 불러온다음 가공
        CalendarDay selday = calendarView.getSelectedDate();
        if(selday != null) {
            int year = selday.getYear();
            int month = selday.getMonth() + 1;


            if (month < 10) {
                selectedMonth = String.valueOf(year + "-0" + month);
            } else if (month > 10) {
                selectedMonth = String.valueOf(year + "-" + month);
            }
        }

        Call<DiaryMonthRes> call = api.getDiaryMonth(accessToken,selectedMonth);
        call.enqueue(new Callback<DiaryMonthRes>() {
            @Override
            public void onResponse(Call<DiaryMonthRes> call, Response<DiaryMonthRes> response) {
               if(response.isSuccessful()){

                    //response 성공시 오늘 데이터 디폴트로 보여줌
                   // 오늘 날짜 일자까지 가공 (2023-03-29) 이런식으로 나올수있게
                   CalendarDay selday = calendarView.getSelectedDate();
                   if(selday != null){
                       int year = selday.getYear();
                       int month = selday.getMonth() + 1;
                       int day = selday.getDay();

                       if(month <10 ){
                           if(day <10 ){
                               selectedDay = String.valueOf(year+"-0"+month+"-0"+day);
                           }else if(day>= 10){
                               selectedDay = String.valueOf(year+"-0"+month+"-"+day);
                           }

                       } else if (month >=10) {
                           if(day<10){

                           } else if (day>=10) {
                               selectedDay = String.valueOf(year+"-"+month+"-0"+day);
                           }
                           selectedDay = String.valueOf(year+"-"+month+"-"+day);
                       }

                   }


                    // 선택한 날짜가 response받은(유저가 기록한)데이터와 일치하는지 확인하고
                   //일치한다면 바로 띄워주기
                   for (int i = 0; i < response.body().getItems().size(); i++) {
                       String today = response.body().getItems().get(i).getDate();

                       Log.i(TAG, "DATE확인" + selectedDay + "today화깅" + today);

                       if (today.equals(selectedDay)) {


                           dayexercise = response.body().getItems().get(i).getExerciseKcal();
                           dayweight = response.body().getItems().get(i).getNowWeight();
                           dayfood = response.body().getItems().get(i).getFoodKcal();


                           if (dayexercise.isEmpty()) {
                               txtExercise.setText("이 날 기록이 없어요");
                           } else {
                               double todayexer =  Float.parseFloat(dayexercise);
                               long todayexerexcute = Math.round(todayexer*100);
                               long todayexerexcute2 =todayexerexcute / 100;
                               dayexercise =  String.format("%,d",todayexerexcute2);
                               txtExercise.setText(dayexercise+" Kcal");
                           }
                           if (dayweight.isEmpty()) {
                               txtWeight.setText("이 날 기록이 없어요");
                           } else {
                               double todayweight = Float.parseFloat(dayweight);
                               long todayweightexcute1 =  Math.round(todayweight*100);
                               long todayweightexcute2 = todayweightexcute1/100;
                               dayweight =  String.format("%,d",todayweightexcute2);
                               txtWeight.setText(dayweight+" Kg");
                           }
                           if (dayfood.isEmpty()) {
                               txtKcal.setText("이 날 기록이 없어요");
                           } else {
                               double todayfood = Float.parseFloat(dayfood);
                               long todayfoodexcute1 = Math.round(todayfood*1000);
                               long todayfoodexcute2 = todayfoodexcute1/1000;
                               dayfood = String.format("%,d",todayfoodexcute2);
                               txtKcal.setText(dayfood+ " Kcal");


                           }
                           break;
                       } else if (today != selectedDay) {

                           txtExercise.setText("이 날 기록이 없어요");
                           txtWeight.setText("이 날 기록이 없어요");
                           txtKcal.setText("이 날 기록이 없어요");
                       }
                   }

                    //이제부턴 유저가 선택하는 날짜 데이터 찾기
                   calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                       @Override
                       public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {


                           //과정은 같음
                           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                           String selectedDate = dateFormat.format(date.getDate());

                           for (int i = 0; i < response.body().getItems().size(); i++) {
                               String today = response.body().getItems().get(i).getDate();

                               Log.i(TAG, "DATE확인" + selectedDate + "today화깅" + today);

                               if (today.equals(selectedDate)) {


                                   dayexercise = response.body().getItems().get(i).getExerciseKcal();
                                   dayweight = response.body().getItems().get(i).getNowWeight();
                                   dayfood = response.body().getItems().get(i).getFoodKcal();


                                   if (dayexercise.isEmpty()) {
                                       txtExercise.setText("이 날 기록이 없어요");
                                   } else {
                                       double todayexer =  Float.parseFloat(dayexercise);
                                       long todayexerexcute = Math.round(todayexer*100);
                                       long todayexerexcute2 =todayexerexcute / 100;
                                       dayexercise =  String.format("%,d",todayexerexcute2);
                                       txtExercise.setText(dayexercise+" Kcal");
                                   }
                                   if (dayweight.isEmpty()) {
                                       txtWeight.setText("이 날 기록이 없어요");
                                   } else {
                                       double todayweight = Float.parseFloat(dayweight);
                                       long todayweightexcute1 =  Math.round(todayweight*100);
                                       long todayweightexcute2 = todayweightexcute1/100;
                                       dayweight =  String.format("%,d",todayweightexcute2);
                                       txtWeight.setText(dayweight+" Kg");
                                   }
                                   if (dayfood.isEmpty()) {
                                       txtKcal.setText("이 날 기록이 없어요");
                                   } else {
                                       double todayfood = Float.parseFloat(dayfood);
                                       long todayfoodexcute1 = Math.round(todayfood*1000);
                                       long todayfoodexcute2 = todayfoodexcute1/1000;
                                       dayfood = String.format("%,d",todayfoodexcute2);
                                       txtKcal.setText(dayfood+ " Kcal");



                                   }
                                   break;
                               } else if (today != selectedDate) {

                                   txtExercise.setText("이 날 기록이 없어요");
                                   txtWeight.setText("이 날 기록이 없어요");
                                   txtKcal.setText("이 날 기록이 없어요");
                               }
                           }


                       }
                   });



               }

            }

            @Override
            public void onFailure(Call<DiaryMonthRes> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

    }

}



