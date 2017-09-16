package com.tmdb.utils;

import android.content.Context;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Developer on 9/16/2017.
 */

public class OkHttpSingleTonClass {
    private static OkHttpClient okHttpClient;
    private OkHttpSingleTonClass() {}

    public static OkHttpClient getOkHttpClient(Context context) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(createLoggingInterceptor())
//                    .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
//                    .cache(createCacheForOkHTTP(context))
                    .build();
        }
        return okHttpClient;
    }

    private static HttpLoggingInterceptor  createLoggingInterceptor(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    private static Cache createCacheForOkHTTP(Context context) {
        //setup cache
        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024;
        return new Cache(httpCacheDirectory, cacheSize);
    }

    /*private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            if (isNetworkAvailable(context)) {
                int maxAge = 60;
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28;
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };*/

}