package com.tmdb.utils;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tmdb.interfaces.RetrofitInterface;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by YounasBangash on 9/14/2017.
 */

public class RetrofitBuilder {

    public RetrofitInterface mApi;
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.themoviedb.org/";


    public RetrofitBuilder(Context context) {

        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(OkHttpSingleTonClass.getOkHttpClient(context))
                    .build();
        }
        mApi = retrofit.create(RetrofitInterface.class);
    }
}



