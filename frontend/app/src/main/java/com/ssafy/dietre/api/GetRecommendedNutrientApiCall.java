package com.ssafy.dietre.api;

import android.util.Log;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.ssafy.dietre.api.response.RecommendedNutrientRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.type.UserDesiredStatus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetRecommendedNutrientApiCall extends BaseApiCall {
    public static void call(UserDesiredStatus status, TextInputLayout kcalInput, TextView carbText, TextView protText, TextView fatText) {
        service = ApiCallServiceProvider.provide();
        Call call = service.getRecommendedNutrients(status);

        call.enqueue(new Callback<RecommendedNutrientRes>() {
            @Override
            public void onResponse(Call<RecommendedNutrientRes> call, Response<RecommendedNutrientRes> response) {
                if (response.isSuccessful()) {
                    Log.e("프래그먼트 데이터 확인", "레코멘드 뉴트리언트 에이피아이: 성공");
                    RecommendedNutrientRes res = response.body();
                    Log.v("응답 메시지", res.getMessage());
                    Log.v("추천 칼로리", res.getData().getRecommendedCalorie().toString());
                    Log.v("추천 탄수화물", res.getData().getRecommendedCarbohydrate().toString());
                    Log.v("추천 단백질", res.getData().getRecommendedProtein().toString());
                    Log.v("추천 지방", res.getData().getRecommendedFat().toString());

                    kcalInput.getEditText().setText(res.getData().getRecommendedCalorie().toString());
                    carbText.setText(res.getData().getRecommendedCarbohydrate().toString());
                    protText.setText(res.getData().getRecommendedProtein().toString());
                    fatText.setText(res.getData().getRecommendedFat().toString());
                } else {
                    Log.e("프래그먼트 데이터 확인", "레코멘드 뉴트리언트 에이피아이: 실패");
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