package com.ssafy.dietre.api;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.ssafy.dietre.api.request.UserRegisterReq;
import com.ssafy.dietre.api.response.RecommendedNutrientRes;
import com.ssafy.dietre.api.response.UserInfoRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatchUserInfoApiCall extends BaseApiCall {
    public static void call(UserRegisterReq req,
                            TextInputLayout kcalInputLayout, TextView carbTextView,
                            TextView protTextView, TextView fatTextView) {
        service = ApiCallServiceProvider.provide();
        Call call = service.updateUserInfo(req);

        call.enqueue(new Callback<RecommendedNutrientRes>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<RecommendedNutrientRes> call, Response<RecommendedNutrientRes> response) {
                if (response.isSuccessful()) {
                    Log.e("patch test", "수정 테스트");
                    Log.e("api 테스트", "테스트 성공");
                    Log.e("응답 원본", String.valueOf(response.code()));
                    RecommendedNutrientRes res = response.body();
                    Log.e("프래그먼트 데이터 테스트", "칼로리값:"+res.getData().getRecommendedCalorie());
                    Log.e("프래그먼트 데이터 테스트", "탄수값:"+res.getData().getRecommendedCarbohydrate());
                    Log.e("프래그먼트 데이터 테스트", "단백값:"+res.getData().getRecommendedProtein());
                    Log.e("프래그먼트 데이터 테스트", "지방값:"+res.getData().getRecommendedFat());

                    kcalInputLayout.getEditText().setText(String.valueOf(res.getData().getRecommendedCalorie()));
                    carbTextView.setText(String.valueOf(res.getData().getRecommendedCarbohydrate()));
                    protTextView.setText(String.valueOf(res.getData().getRecommendedProtein()));
                    fatTextView.setText(String.valueOf(res.getData().getRecommendedFat()));
                } else {
                    Log.v("패치 유저 인포", "실패");
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
