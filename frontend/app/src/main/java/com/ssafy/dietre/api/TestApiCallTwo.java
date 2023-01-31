package com.ssafy.dietre.api;


import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.ssafy.dietre.api.response.MealRegisterRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.ui.calendar.CalendarFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestApiCallTwo extends BaseApiCall {

    public static void call(View root,
                            TextView kcalText, TextView carbText,
                            TextView protText, TextView fatText,
                            CalendarFragment calendarFragment) {
        service = ApiCallServiceProvider.provide();
        Call call = service.testCall();
        int kcalTest = 1500;
        int carbTest = 1600;
        int protTest = 1700;
        int fatTest = 1800;

        call.enqueue(new Callback<MealRegisterRes>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<MealRegisterRes> call, Response<MealRegisterRes> response) {
                if (response.isSuccessful()) {
                    int totalTestValue = carbTest + protTest + fatTest;
                    int carbRate = (carbTest * 100) / totalTestValue;
                    int protRate = (protTest * 100) / totalTestValue;
                    int fatRate = (fatTest * 100) / totalTestValue;

                    Log.e("프래그먼트 데이터 테스트", "전체 값: "+totalTestValue);
                    Log.e("프래그먼트 데이터 테스트", "탄수 값: "+carbRate);
                    Log.e("프래그먼트 데이터 테스트", "단백 값: "+protRate);
                    Log.e("프래그먼트 데이터 테스트", "지방 값: "+fatRate);

                    calendarFragment.makePieChart(root, carbRate, protRate, fatRate);
                    kcalText.setText("칼로리: "+"1500");
                    carbText.setText("탄수화물: "+"1600");
                    protText.setText("단백질: "+"1700");
                    fatText.setText("지방: "+"1800");
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

