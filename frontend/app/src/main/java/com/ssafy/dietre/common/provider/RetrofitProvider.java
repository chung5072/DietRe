package com.ssafy.dietre.common.provider;

import com.ssafy.dietre.BuildConfig;
import com.ssafy.dietre.common.interceptor.AuthenticationInterceptor;
import com.ssafy.dietre.common.interceptor.JwtExceptionInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {
    private static final String API_URL = BuildConfig.BASE_URL;

    private static AuthenticationInterceptor headerInterceptor;
    private static JwtExceptionInterceptor jwtExceptionInterceptor = new JwtExceptionInterceptor();

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient
                    .Builder()
                    .addInterceptor(jwtExceptionInterceptor);


    private static Retrofit.Builder builder =
            new Retrofit
                    .Builder()
                    .client(httpClient.build())
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create());


    private static class RetrofitProviderHolder {
        private static Retrofit instance = builder.build();
    }

    public static Retrofit provide() {
        return RetrofitProviderHolder.instance;
    }

    public static Retrofit rebuild(String token) {
        headerInterceptor = new AuthenticationInterceptor(token);
        httpClient = new OkHttpClient.Builder();
        httpClient
                .addInterceptor(headerInterceptor)
                .addInterceptor(jwtExceptionInterceptor);

        RetrofitProviderHolder.instance = builder.client(httpClient.build()).build();
        ApiCallServiceProvider.rebuild();

        return RetrofitProviderHolder.instance;
    }
}