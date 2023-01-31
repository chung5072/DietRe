package com.ssafy.dietre.common.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {
    private String token;

    public AuthenticationInterceptor(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request.Builder builder = request
                .newBuilder()
                .header("Authorization", token);

        Request requestWithAuth = builder.build();
        return chain.proceed(requestWithAuth);
    }
}