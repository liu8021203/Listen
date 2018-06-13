package com.ting.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.ting.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liu on 2017/6/22.
 */

public class UtilRetrofit {
    private static Retrofit retrofit = null;

    public static final int CONNECT_TIME_OUT = 60;
    public static final int READ_TIME_OUT = 60;
    public static final int WRITE_TIME_OUT = 60;

    public static Retrofit getInstance(){
        if(retrofit == null){
            synchronized (UtilRetrofit.class){
                if (retrofit == null){
                    HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
                    if(BuildConfig.DEBUG){
                        //显示日志
                        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    }else {
                        logInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
                    }
                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logInterceptor).connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS).
                            readTimeout(READ_TIME_OUT, TimeUnit.SECONDS).
                            writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS).build();

                    retrofit = new Retrofit.Builder().baseUrl("http://www.tingshijie.com/").addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
                }
            }
        }
        return retrofit;
    }


}
