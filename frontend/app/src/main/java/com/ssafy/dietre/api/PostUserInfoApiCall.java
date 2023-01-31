package com.ssafy.dietre.api;

import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;
import com.ssafy.dietre.LoginActivity;
import com.ssafy.dietre.MainActivity;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.request.UserRegisterReq;
import com.ssafy.dietre.api.response.RecommendedNutrientRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.type.Gender;
import com.ssafy.dietre.common.type.UserDesiredStatus;
import com.ssafy.dietre.ui.survey.DetailFragment;
import com.ssafy.dietre.ui.survey.NutFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostUserInfoApiCall extends BaseApiCall {

    public static void call(UserRegisterReq req, LoginActivity loginActivity) {
        service = ApiCallServiceProvider.provide();

        Call call = service.registerUserInfo(req);

//        Log.e("프래그먼트 데이터 테스트", "나이"+req.getAge());
//        Log.e("프래그먼트 데이터 테스트", "성별"+req.getGender());
//        Log.e("프래그먼트 데이터 테스트", "키"+req.getHeight());
//        Log.e("프래그먼트 데이터 테스트", "몸무게"+req.getWeight());
//        Log.e("프래그먼트 데이터 테스트", "목표"+req.getStatus());
//        Log.e("프래그먼트 데이터 테스트", "활동량"+req.getActivityLevel());
//        Log.e("프래그먼트 데이터 테스트", "칼로리"+req.getRecommendedCalorie());
//        Log.e("프래그먼트 데이터 테스트", "탄수"+req.getRecommendedCarbohydrate());
//        Log.e("프래그먼트 데이터 테스트", "단백"+req.getRecommendedProtein());
//        Log.e("프래그먼트 데이터 테스트", "지방"+req.getRecommendedFat());
//        Log.e("프래그먼트 데이터 테스트", "아침유무"+req.getCountBreakfast());
//        Log.e("프래그먼트 데이터 테스트", "주말유무"+req.getCountWeekend());

        UserRegisterReq finalReq = req;
        call.enqueue(new Callback<RecommendedNutrientRes>() {
            @Override
            public void onResponse(Call<RecommendedNutrientRes> call, Response<RecommendedNutrientRes> response) {
                Log.v("API 요청 보내짐" , "API 요청 보내짐");
                if (response.isSuccessful()) {
                    Log.v("api 테스트", "테스트 성공");
                    Log.v("응답 원본", String.valueOf(response.code()));
                    RecommendedNutrientRes res = response.body();
                    Log.v("응답 메시지", res.getMessage());
                    Log.v("추천 칼로리", res.getData().getRecommendedCalorie().toString());
                    Log.v("추천 탄수화물", res.getData().getRecommendedCarbohydrate().toString());
                    Log.v("추천 단백질", res.getData().getRecommendedProtein().toString());
                    Log.v("추천 지방", res.getData().getRecommendedFat().toString());

                    // 설문 조사 이후 MainActivity로 이동
                    Intent intent = new Intent(loginActivity, MainActivity.class);
                    loginActivity.startActivity(intent);
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
