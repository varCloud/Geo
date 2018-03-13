package com.example.rexv666480.verificadores.ServiciosWeb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rexv666480 on 13/12/2017.
 */

public class RetrofitClient {

    private Gson gson;

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    private Retrofit retrofit;
    public static final String BASE_URL = "http://bluecloud.com.mx/verificadores/Servicios/";//8023

    public RetrofitClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        OkHttpClient client = new OkHttpClient.Builder().
                readTimeout(100,TimeUnit.SECONDS).connectTimeout(100, TimeUnit.SECONDS).
                addInterceptor(interceptor).build();



        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))

                .build();

    }
}
