package com.ssafy.dietre.api;

import android.util.Log;

import com.ssafy.dietre.api.response.MealSearchRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.type.MealTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMealPlannerApiCall extends BaseApiCall {
    public static void call() {
        service = ApiCallServiceProvider.provide();

        Call call = service.searchMealPlanner("2022-11-10", MealTime.breakfast);

        call.enqueue(new Callback<MealSearchRes>() {
            @Override
            public void onResponse(Call<MealSearchRes> call, Response<MealSearchRes> response) {
                if (response.isSuccessful()) {
                    MealSearchRes res = response.body();
                    Log.v("날짜", res.getDate());
                } else {
                    Log.v("응답 원본", String.valueOf((response.code())));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.v("api 테스트 실패시", t.toString());
            }
        });
    }
}