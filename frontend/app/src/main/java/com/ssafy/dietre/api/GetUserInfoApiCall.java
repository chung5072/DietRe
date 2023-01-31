package com.ssafy.dietre.api;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.response.RecommendedNutrientRes;
import com.ssafy.dietre.api.response.UserInfoRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.type.Gender;
import com.ssafy.dietre.common.type.UserDesiredStatus;
import com.ssafy.dietre.ui.user.UserFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserInfoApiCall extends BaseApiCall {
    public static void call(
            TextView nameTextView, TextView emailTextView, TextView genderTextView
            , TextView ageTextView, TextView heightTextView, TextView weightTextView
            , TextInputLayout kcalInputLayout, TextView carbTextView, TextView protTextView
            , TextView fatTextView, Button dietGoal, Button dietActivity,
            UserFragment userFragment) {
        service = ApiCallServiceProvider.provide();
        Call call = service.getUserInfo();

        call.enqueue(new Callback<UserInfoRes>() {
            @Override
            public void onResponse(Call<UserInfoRes> call, Response<UserInfoRes> response) {
                if (response.isSuccessful()) {
                    UserInfoRes res = response.body();
                    Log.v("응답 메시지", res.getMessage());
                    Log.v("추천 칼로리", res.getRecommendedCalorie().toString());
                    nameTextView.setText(res.getName());
                    emailTextView.setText(res.getEmail());
                    String gender = "";
                    if (res.getGender().equals(Gender.male)) {
                        gender = "남자";
                    } else {
                        gender = "여자";
                    }
                    genderTextView.setText(gender);
                    String userGender = (String) genderTextView.getText();
                    ageTextView.setText(res.getAge().toString() + " 세");
                    String userAge = (String) ageTextView.getText();
                    heightTextView.setText(res.getHeight().toString() + " cm");
                    String userHeight = (String) heightTextView.getText();
                    weightTextView.setText(res.getWeight().toString() + " kg");
                    String userWeight = (String) weightTextView.getText();
                    kcalInputLayout.getEditText().setText(res.getRecommendedCalorie().toString());
                    carbTextView.setText(res.getRecommendedCarbohydrate().toString());
                    protTextView.setText(res.getRecommendedProtein().toString());
                    fatTextView.setText(res.getRecommendedFat().toString());
                    userFragment.setHiddenInputForUpdate(
                            userGender,
                            userAge.substring(0, userAge.lastIndexOf(" ")),
                            userHeight.substring(0, userHeight.lastIndexOf(" ")),
                            userWeight.substring(0, userWeight.lastIndexOf(" "))
                    );
                    String goal = "";
                    if (res.getStatus().equals(UserDesiredStatus.decrease)) {
                        goal = "감소";
                    } else if (res.getStatus().equals(UserDesiredStatus.maintain)){
                        goal = "유지";
                    } else {
                        goal = "증가";
                    }
                    dietGoal.setText(goal);
                    switch (res.getActivityLevel()) {
                        case 1 : {
                            dietActivity.setText(R.string.text_activity_one);
                            break;
                        }
                        case 2 : {
                            dietActivity.setText(R.string.text_activity_two);
                            break;
                        }
                        case 3 : {
                            dietActivity.setText(R.string.text_activity_three);
                            break;
                        }
                        case 4 : {
                            dietActivity.setText(R.string.text_activity_four);
                            break;
                        }
                        case 5 : {
                            dietActivity.setText(R.string.text_activity_five);
                            break;
                        }
                    }
                } else {
                    Log.e("프래그먼트 데이터 확인", "겟 유저 인포 콜: 호출 실패");
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