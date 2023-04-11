package com.cookandroid.subdietapp.api;

import android.content.Context;

import com.cookandroid.subdietapp.config.Config;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 바꿀 코드가 따로없다
public class VisionNetworkClient {

    public static Retrofit retrofit;

    public static Retrofit getRetrofitClient(Context context){
        if(retrofit == null){
            // 통신 로그 확인할때 필요한 코드
            HttpLoggingInterceptor loggingInterceptor =
                    new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 실배포시는 NONE으로 설정 (해킹문제)

            // 네트워크 연결관련 코드
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .addInterceptor(loggingInterceptor)
                    .build();
            // 네트워크로 데이터를 보내고 받는
            // 레트로핏 라이브러리 관련 코드
            retrofit = new Retrofit.Builder()
                    .baseUrl(Config.VISIONDOMAIN)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}