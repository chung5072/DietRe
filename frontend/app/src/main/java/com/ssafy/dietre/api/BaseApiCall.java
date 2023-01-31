package com.ssafy.dietre.api;

import com.ssafy.dietre.common.provider.RetrofitProvider;

import retrofit2.Retrofit;

public class BaseApiCall {
    protected static Retrofit retrofit = RetrofitProvider.provide();
    protected static ApiCallService service = retrofit.create(ApiCallService.class);
}

