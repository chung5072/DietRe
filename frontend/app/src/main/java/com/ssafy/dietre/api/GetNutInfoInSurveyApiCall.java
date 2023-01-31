package com.ssafy.dietre.api;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ssafy.dietre.LoginActivity;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.response.MealSearchRes;
import com.ssafy.dietre.api.response.RecommendedNutrientRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.type.Gender;
import com.ssafy.dietre.common.type.MealTime;
import com.ssafy.dietre.common.type.UserDesiredStatus;
import com.ssafy.dietre.ui.survey.DetailFragment;
import com.ssafy.dietre.ui.survey.NutFragment;
import com.ssafy.dietre.ui.survey.PhysicInfo;
import com.ssafy.dietre.ui.survey.PhysicViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetNutInfoInSurveyApiCall extends BaseApiCall  {
    public static void call(PhysicInfo physicInfo, PhysicViewModel physicViewModel, LoginActivity loginActivity) {
        service = ApiCallServiceProvider.provide();

        UserDesiredStatus status = physicInfo.getStatus();
        Gender gender = physicInfo.getGender();
        Integer age = physicInfo.getAge();
        Integer height = physicInfo.getHeight();
        Integer weight = physicInfo.getWeight();
        Integer activityLevel = physicInfo.getActivityLevel();

        // 목표, 성별, 키, 몸무게, 활동량, 나이
        Call call = service.getRecommendedNutrients(status, gender, height, weight, activityLevel, age);

        call.enqueue(new Callback<RecommendedNutrientRes>() {
            @Override
            public void onResponse(Call<RecommendedNutrientRes> call, Response<RecommendedNutrientRes> response) {
                if (response.isSuccessful()) {
                    RecommendedNutrientRes res = response.body();

                    physicInfo.setGender(gender);
                    physicInfo.setAge(age);
                    physicInfo.setHeight(height);
                    physicInfo.setWeight(weight);
                    physicInfo.setCountBreakfast(physicInfo.getCountBreakfast());
                    physicInfo.setCountWeekend(physicInfo.getCountWeekend());
                    physicInfo.setActivityLevel(activityLevel);
                    physicInfo.setStatus(status);
                    physicInfo.setRecommendedCalorie(res.getData().getRecommendedCalorie());
                    physicInfo.setRecommendedCarbohydrate(res.getData().getRecommendedCarbohydrate());
                    physicInfo.setRecommendedProtein(res.getData().getRecommendedProtein());
                    physicInfo.setRecommendedFat(res.getData().getRecommendedFat());

                    physicViewModel.setPhysicInfo(physicInfo);

                    // 프래그먼트 - 활동 설문 페이지
                    Fragment nutFragment = new NutFragment();
                    FragmentManager fragmentManager = loginActivity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // 로그인 페이지 대체
                    fragmentTransaction.replace(R.id.fragment_login, nutFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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
