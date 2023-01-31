package com.ssafy.dietre.api;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.response.DailyMealRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.ui.calendar.CalendarFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetDailyMealNutApiCall extends BaseApiCall {
    public static void call(String date, View root,
                            TextView kcalText, TextView carbText,
                            TextView protText, TextView fatText,
                            CalendarFragment calendarFragment) {

        service = ApiCallServiceProvider.provide();
        Call call = service.getDailyMealNut(date);
        Log.e("프래그먼트 데이터 확인", "겟데일리밀정보 - 여긴 들어옴");

        call.enqueue(new Callback<DailyMealRes>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<DailyMealRes> call, Response<DailyMealRes> response) {
                if (response.isSuccessful()) {
                    Log.e("프래그먼트 데이터 확인", "겟데일리밀정보 - 성공");
                    StringBuilder sb;
                    DailyMealRes res = response.body();
                    LinearLayout hasData = root.findViewById(R.id.layout_calendar_daily_has_data);
                    LinearLayout noData = root.findViewById(R.id.layout_calendar_daily_no_data);
                    int totalTestValue = 3;
                    int carbRate = 0;
                    int protRate = 0;
                    int fatRate = 0;
                    try {
                        totalTestValue
                                = res.getRealCarbohydrate() + res.getRealProtein() + res.getRealFat();
                        carbRate = (res.getRealCarbohydrate() * 100) / totalTestValue;
                        protRate = (res.getRealProtein() * 100) / totalTestValue;
                        fatRate = (res.getRealFat() * 100) / totalTestValue;

                        Log.e("프래그먼트 데이터 테스트", "전체 값: "+totalTestValue);
                        Log.e("프래그먼트 데이터 테스트", "탄수 값: "+carbRate);
                        Log.e("프래그먼트 데이터 테스트", "단백 값: "+protRate);
                        Log.e("프래그먼트 데이터 테스트", "지방 값: "+fatRate);

                        hasData.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                        sb = new StringBuilder();
                        sb.append("칼로리(kcal): ").append(res.getRealCalorie());
                        kcalText.setText(sb.toString());
                        carbText.setText("탄수화물(g): "+res.getRealCarbohydrate());
                        protText.setText("단백질(g): "+res.getRealProtein());
                        fatText.setText("지방(g): "+res.getRealFat());
                        calendarFragment.makePieChart(root, carbRate, protRate, fatRate);
                    } catch (ArithmeticException divideErr) {
                        hasData.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("프래그먼트 데이터 확인", "겟데일리밀정보 - 실패");
                    Log.v("응답 원본", String.valueOf((response.code())));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("프래그먼트 데이터 확인", "겟데일리밀정보 - 이상");
                Log.v("api 테스트 실패시", t.toString());
            }
        });
    }
}
