package com.ssafy.dietre.api;

import android.util.Log;

import com.ssafy.dietre.api.data.MealData;
import com.ssafy.dietre.api.response.MealRegisterRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.type.MealTime;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatchMealPlannerApiCall extends BaseApiCall {
    public static void call() {
        service = ApiCallServiceProvider.provide();
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        list.add(3L);
        list.add(4L);
        list.add(5L);
        list.add(6L);
        MealData mealData = new MealData("2022-11-10", MealTime.breakfast, null, list);
        Call call = service.updateMealPlanner(5L, mealData);

        call.enqueue(new Callback<MealRegisterRes>() {
            @Override
            public void onResponse(Call<MealRegisterRes> call, Response<MealRegisterRes> response) {
                if (response.isSuccessful()) {
                    MealRegisterRes res = response.body();
                    Log.v("응답 메시지", res.getMessage());
                    Log.v("식단 번호", res.getMealPlannerId().toString());
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