package com.cookandroid.subdietapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.api.EdaApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.eda.EdaDay;
import com.cookandroid.subdietapp.model.eda.EdaDayRes;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChartActivity extends AppCompatActivity {

    BarChart barChart;

    LineChart lineChart;

    float barWidth;
    float barSpace;
    float groupSpace;
    String EdaDate;
    Double EdaDateWeight, EdaDateKcal, EdaDateBurnKcal;
    ArrayList<EdaDay> dayList = new ArrayList<>();
    ArrayList xVals = new ArrayList();
    ArrayList yVals1 = new ArrayList();
    ArrayList yVals2 = new ArrayList();

    ArrayList yVals3 = new ArrayList();

    ArrayList<Entry> dataList = new ArrayList<>();


//    List<Entry> entries = new ArrayList<>();
//
//    XAxis xAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        lineChart = findViewById(R.id.lineChart);


        barWidth = 0.3f;
        barSpace = 0f;
        groupSpace = 0.4f;

        barChart = (BarChart)findViewById(R.id.fragment_bluetooth_chat_barchart);
        barChart.setDescription(null);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);








        int groupCount = 5;

        Retrofit retrofit = NetworkClient.getRetrofitClient(ChartActivity.this);

        EdaApi api = retrofit.create(EdaApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");



        Call<EdaDayRes> call = api.getEdaDay(accessToken, "2023-04");
        call.enqueue(new Callback<EdaDayRes>() {
            @Override
            public void onResponse(Call<EdaDayRes> call, Response<EdaDayRes> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                dayList.clear();


                if (response.isSuccessful()) {

                    try {

                        dayList = (ArrayList<EdaDay>) response.body().getItems();

                        for (int i = 0; i <6; i++){
                            EdaDate = dayList.get(i).getDate().substring(5, 9+1);
                            xVals.add(EdaDate);
                            Log.i("EDADATETEST", EdaDate + " " + i);
                        }


                    } catch (Exception e) {


                    }

                    // 바차트

                    try {
                        for (int i=1; i<6; i++){
                            for (int j=0; j<5; j++){
                                EdaDateWeight = dayList.get(j).getNowWeight();
                                EdaDateBurnKcal = dayList.get(j).getTotalKcalBurn();
                                EdaDateKcal = dayList.get(j).getTotalKcal();

                                yVals1.add(new BarEntry(i, EdaDateBurnKcal.floatValue()));
                                yVals2.add(new BarEntry(i, EdaDateKcal.floatValue()));
                            }
                        }

                    } catch (Exception e) {

                    }

//                    yVals1.add(new BarEntry(1, (float) 1));
//                    yVals2.add(new BarEntry(1, (float) 2));
//                    rgb(131, 200, 211)
                    BarDataSet set1, set2;
                    set1 = new BarDataSet(yVals1, "소모 칼로리");
                    set1.setColor(Color.rgb(241, 196, 15));

                    set2 = new BarDataSet(yVals2, "섭취 칼로리");
                    set2.setColor(Color.rgb(255, 145, 160));



                    BarData data = new BarData(set1, set2);
                    data.setValueFormatter(new LargeValueFormatter());
                    data.setBarWidth(0.001f);
                    barChart.setData(data);
                    barChart.getBarData().setBarWidth(barWidth);
                    barChart.getXAxis().setAxisMinimum(0);
                    barChart.getXAxis().setAxisMaximum(0 + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
                    barChart.groupBars(0, groupSpace, barSpace);
                    barChart.getData().setHighlightEnabled(false);
                    barChart.invalidate();

                    Legend l = barChart.getLegend();
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                    l.setDrawInside(true);
                    l.setYOffset(13f);
                    l.setXOffset(0f);
                    l.setYEntrySpace(0f);
                    l.setTextSize(8f);


                    //X-axis
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setGranularity(1f);
                    xAxis.setGranularityEnabled(true);
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setDrawGridLines(false);
                    xAxis.setAxisMaximum(5);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));


                    //Y-axis
                    barChart.getAxisRight().setEnabled(false);
                    YAxis leftAxis = barChart.getAxisLeft();
                    leftAxis.setValueFormatter(new LargeValueFormatter());
                    leftAxis.setDrawGridLines(true);
                    leftAxis.setSpaceTop(40f);
                    leftAxis.setAxisMinimum(0f);


                    // 라인차트




//                    dataList.add(new Entry(1, 15));
//                    dataList.add(new Entry(2, 20));

                    try {
                        for (int i = 0; i <6; i++){
                            EdaDateWeight = dayList.get(i).getNowWeight();

                            dataList.add(new Entry(i, EdaDateWeight.floatValue()));

                            Log.i("EDADATETEST", EdaDate + " " + i);
                        }

                    } catch (Exception e) {

                    }


                    LineDataSet lineDataSet1 = new LineDataSet(dataList, "몸무게(kg)");

                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(lineDataSet1);

                    // 커스텀
                    // 라인 굵기
                    lineDataSet1.setLineWidth(4);

                    // 라인 색상
                    lineDataSet1.setColor(Color.rgb(187, 227, 255));

                    // 데이터 원 표시 여부
                    lineDataSet1.setDrawCircles(true);

                    // 데이터 원 색상
//                    lineDataSet1.setCircleColor(Color.GRAY);

                    // 데이터 원 홀 색상
                    lineDataSet1.setCircleHoleColor(Color.rgb(187, 227, 255));

                    // 데이터 원 홀 반지름
//                    lineDataSet1.setCircleRadius(10);

                    // 데이터 숫자 색상
                    lineDataSet1.setValueTextColor(Color.BLACK);

                    lineDataSet1.setValueTextSize(10);
                    // 라인데이터에 리스트 추가
                    LineData lineData = new LineData(dataSets);

                    // 차트에 라인 데이터 추가
                    lineChart.setData(lineData);
                    // 차트 초기화
                    lineChart.invalidate();












                } else {

                }

            }

            @Override
            public void onFailure(Call<EdaDayRes> call, Throwable t) {

            }
        });










    }

    private ArrayList<Entry> data1() {
        ArrayList<Entry> dataList = new ArrayList<>();


        return dataList;
    }

    private void getNetworkData() {

        Retrofit retrofit = NetworkClient.getRetrofitClient(ChartActivity.this);

        EdaApi api = retrofit.create(EdaApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");



        Call<EdaDayRes> call = api.getEdaDay(accessToken, "2023-04");
        call.enqueue(new Callback<EdaDayRes>() {
            @Override
            public void onResponse(Call<EdaDayRes> call, Response<EdaDayRes> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                dayList.clear();


                if (response.isSuccessful()) {


                    dayList.addAll(response.body().getItems());

//                    postingAdapter = new PostingAdapter(getActivity(), postingList);
//                    recyclerView.setAdapter(postingAdapter);


                } else {

                }

            }

            @Override
            public void onFailure(Call<EdaDayRes> call, Throwable t) {

            }
        });


    }


}
