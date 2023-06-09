package com.cookandroid.subdietapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.subdietapp.api.EdaApi;
import com.cookandroid.subdietapp.api.NetworkClient;
import com.cookandroid.subdietapp.config.Config;
import com.cookandroid.subdietapp.model.eda.EdaDay;
import com.cookandroid.subdietapp.model.eda.EdaDayRes;
import com.cookandroid.subdietapp.model.eda.EdaMonth;
import com.cookandroid.subdietapp.model.eda.EdaMonthRes;
import com.cookandroid.subdietapp.model.eda.EdaWeek;
import com.cookandroid.subdietapp.model.eda.EdaWeekRes;
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

    TextView txtEda;

    float barWidth;
    float barSpace;
    float groupSpace;
    String EdaDate, EdaDateStart, EdaDateEnd;
    Double EdaDateWeight, EdaDateKcal, EdaDateBurnKcal;
    ArrayList<EdaDay> dayList = new ArrayList<>();
    ArrayList<EdaWeek> weekList = new ArrayList<>();
    ArrayList<EdaMonth> monthList = new ArrayList<>();

    ArrayList xVals = new ArrayList();
    ArrayList yVals1 = new ArrayList();
    ArrayList yVals2 = new ArrayList();

    ArrayList WeekxVals = new ArrayList();
    ArrayList WeekyVals1 = new ArrayList();
    ArrayList WeekyVals2 = new ArrayList();
    ArrayList<Entry> dataList = new ArrayList<>();
    ArrayList<Entry> dataListWeek = new ArrayList<>();
    ArrayList<ILineDataSet> dataSets = new ArrayList<>();

    Button btnDate, btnWeek, btnMonth;

    String nowMonth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        lineChart = findViewById(R.id.lineChart);

        btnDate = findViewById(R.id.btnDate);
        btnWeek = findViewById(R.id.btnWeek);
        btnMonth = findViewById(R.id.btnMonth);
        txtEda = findViewById(R.id.txtEda);


        nowMonth = getIntent().getStringExtra("nowMonth");

        Log.i("nowMonth", "차트액티비티 :" + nowMonth);

        txtEda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChartActivity.this, EdaActivity.class);
                intent.putExtra("nowMonth", nowMonth);
                startActivity(intent);

            }
        });

        barWidth = 0.3f;
        barSpace = 0f;
        groupSpace = 0.4f;

        barChart = (BarChart) findViewById(R.id.fragment_bluetooth_chat_barchart);
        barChart.setDescription(null);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);

        lineChart.setPinchZoom(false);
        lineChart.setScaleEnabled(false);
        lineChart.setDrawGridBackground(false);

//        btnDate.setSelected(true);
//        btnWeek.setSelected(false);
//        btnMonth.setSelected(false);



        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDate.setSelected(true);
                btnWeek.setSelected(false);
                btnMonth.setSelected(false);

                getNetworkDayData(nowMonth);

            }
        });

        btnWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDate.setSelected(false);
                btnWeek.setSelected(true);
                btnMonth.setSelected(false);
                getNetworkWeekData();

            }
        });

        btnMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnDate.setSelected(false);
                btnWeek.setSelected(false);
                btnMonth.setSelected(true);

                getNetworkMonthData();

            }
        });





    }

    @Override
    protected void onResume() {
        super.onResume();
        btnDate.performClick();
    }

    private void getNetworkMonthData() {

        int groupCount = 5;

        Retrofit retrofit = NetworkClient.getRetrofitClient(ChartActivity.this);

        EdaApi api = retrofit.create(EdaApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        Call<EdaMonthRes> call = api.getEdaMonth(accessToken);
        call.enqueue(new Callback<EdaMonthRes>() {
            @Override
            public void onResponse(Call<EdaMonthRes> call, Response<EdaMonthRes> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                monthList.clear();
                dataList.clear();

                xVals.clear();
                yVals1.clear();
                yVals2.clear();
                dataSets.clear();


                if (response.isSuccessful()) {

                    try {

                        monthList = (ArrayList<EdaMonth>) response.body().getItems();

                        for (int i = 0; i < 6; i++) {
                            EdaDate = monthList.get(i).getMonth();
                            xVals.add(EdaDate.replace("-", "/"));
                            Log.i("EDADATETEST", EdaDate + " " + i);
                        }


                    } catch (Exception e) {


                    }

                    // 바차트

                    try {
                        for (int i = 1; i < 6; i++) {
                            for (int j = 0; j < 5; j++) {
                                EdaDateBurnKcal = monthList.get(j).getAvgKcalBurn();
                                EdaDateKcal = monthList.get(j).getAvgKcal();

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
                        for (int i = 0; i < 6; i++) {

                            EdaDateWeight = monthList.get(i).getAvgWeight();

                            dataList.add(new Entry(i, EdaDateWeight.floatValue()));

                            Log.i("EDADATETEST", EdaDate + " " + i);
                        }

                    } catch (Exception e) {

                    }


                    LineDataSet lineDataSet1 = new LineDataSet(dataList, "몸무게(kg)");

//                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();


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
            public void onFailure(Call<EdaMonthRes> call, Throwable t) {

            }
        });
    }


    private void getNetworkWeekData() {
        int groupCount = 5;

        Retrofit retrofit = NetworkClient.getRetrofitClient(ChartActivity.this);

        EdaApi api = retrofit.create(EdaApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        Call<EdaWeekRes> call = api.getEdaWeek(accessToken);
        call.enqueue(new Callback<EdaWeekRes>() {
            @Override
            public void onResponse(Call<EdaWeekRes> call, Response<EdaWeekRes> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.

                weekList.clear();
                dataListWeek.clear();

                WeekxVals.clear();
                WeekyVals1.clear();
                WeekyVals2.clear();
                dataSets.clear();

                if (response.isSuccessful()) {

                    try {

                        weekList = (ArrayList<EdaWeek>) response.body().getItems();

                        for (int i = 0; i < 6; i++) {
                            EdaDateStart = weekList.get(i).getStart().substring(5, 9 + 1).replace("-", "/");
                            EdaDateEnd = weekList.get(i).getEnd().substring(5, 9 + 1).replace("-", "/");

                            WeekxVals.add(EdaDateStart + "~" + EdaDateEnd);
                            Log.i("EDADATESETEST", EdaDateStart + " ~\n" + EdaDateEnd);
                        }


                    } catch (Exception e) {


                    }

                    // 바차트

                    try {
                        for (int i = 1; i < 6; i++) {
                            for (int j = 0; j < 5; j++) {
                                EdaDateWeight = weekList.get(j).getAvgWeight();
                                EdaDateBurnKcal = weekList.get(j).getAvgKcalBurn();
                                EdaDateKcal = weekList.get(j).getAvgKcal();

                                WeekyVals1.add(new BarEntry(i, EdaDateBurnKcal.floatValue()));
                                WeekyVals2.add(new BarEntry(i, EdaDateKcal.floatValue()));
                            }
                        }

                    } catch (Exception e) {

                    }

//                    yVals1.add(new BarEntry(1, (float) 1));
//                    yVals2.add(new BarEntry(1, (float) 2));
//                    rgb(131, 200, 211)
                    BarDataSet set1, set2;
                    set1 = new BarDataSet(WeekyVals1, "소모 칼로리");
                    set1.setColor(Color.rgb(241, 196, 15));

                    set2 = new BarDataSet(WeekyVals2, "섭취 칼로리");
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
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(WeekxVals));


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
                        for (int i = 0; i < 6; i++) {
                            EdaDateWeight = weekList.get(i).getAvgWeight();

                            dataListWeek.add(new Entry(i, EdaDateWeight.floatValue()));

                            Log.i("EDADATETEST", EdaDate + " " + i);
                        }

                    } catch (Exception e) {

                    }


                    LineDataSet lineDataSet1 = new LineDataSet(dataListWeek, "몸무게(kg)");

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
            public void onFailure(Call<EdaWeekRes> call, Throwable t) {

            }
        });
    }

    private void getNetworkDayData(String month) {

        int groupCount = 5;

        Retrofit retrofit = NetworkClient.getRetrofitClient(ChartActivity.this);

        EdaApi api = retrofit.create(EdaApi.class);

        SharedPreferences sp = getSharedPreferences(Config.PREFERENCE_NAME, MODE_PRIVATE);
        String accessToken = "Bearer " + sp.getString(Config.ACCESS_TOKEN, "");


        Call<EdaDayRes> call = api.getEdaDay(accessToken, nowMonth);
        call.enqueue(new Callback<EdaDayRes>() {
            @Override
            public void onResponse(Call<EdaDayRes> call, Response<EdaDayRes> response) {

                // getNetworkData 함수는, 항상 처음에 데이터를 가져오는 동작이므로
                // 초기화 코드가 필요.
                dayList.clear();
                dataList.clear();

                xVals.clear();
                yVals1.clear();
                yVals2.clear();
                dataSets.clear();


                if (response.isSuccessful()) {

                    try {

                        dayList = (ArrayList<EdaDay>) response.body().getItems();

                        for (int i = 0; i < 6; i++) {
                            EdaDate = dayList.get(i).getDate().substring(5, 9 + 1).replace("-", "/");
                            xVals.add(EdaDate);
                            Log.i("EDADATETEST", EdaDate + " " + i);
                        }


                    } catch (Exception e) {


                    }

                    // 바차트

                    try {
                        for (int i = 1; i < 6; i++) {
                            for (int j = 0; j < 5; j++) {
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
                        for (int i = 0; i < 6; i++) {

                            EdaDateWeight = dayList.get(i).getNowWeight();

                            dataList.add(new Entry(i, EdaDateWeight.floatValue()));

                            Log.i("EDADATETEST", EdaDate + " " + i);
                        }

                    } catch (Exception e) {

                    }


                    LineDataSet lineDataSet1 = new LineDataSet(dataList, "몸무게(kg)");

//                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();


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


}
