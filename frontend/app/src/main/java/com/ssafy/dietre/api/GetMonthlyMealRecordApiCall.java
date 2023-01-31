package com.ssafy.dietre.api;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.ssafy.dietre.api.response.MonthlyMealRecordRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.databinding.FragmentCalendarBinding;
import com.ssafy.dietre.ui.calendar.CalendarFragment;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetMonthlyMealRecordApiCall extends BaseApiCall {
    public static void call(
            LocalDate date, FragmentCalendarBinding binding, CalendarFragment calendarFragment,
            TextView lowHeartCnt, TextView enoughHeartCnt, TextView tooMuchHeartCnt) {
        service = ApiCallServiceProvider.provide();
        Call call = service.getMonthlyMealRecords(date);
        Log.e("프래그먼트 데이터 테스트", "달력 하트 정보 리스트 - 여긴 들어옴");

        call.enqueue(new Callback<List<MonthlyMealRecordRes>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<MonthlyMealRecordRes>> call, Response<List<MonthlyMealRecordRes>> response) {
                System.out.println("ON RESPONSE " + response.code());

                if (response.isSuccessful()) {
                    Log.e("프래그먼트 데이터 테스트", "달력 하트 정보 리스트 - 값 가져옴");
                    List<MonthlyMealRecordRes> recordList = response.body();

                    View root = binding.getRoot();
                    // 날짜
                    calendarFragment.setMonthView(root, recordList);

                    int low = 0;
                    int enough = 0;
                    int tooMuch = 0;

                    for (MonthlyMealRecordRes res:
                            recordList) {
                        Log.e("프래그먼트 데이터 테스트", "날짜"+res.getDate()+"하트 정도"+res.getHeart());
                        switch (res.getHeart()) {
                            case 0 : {
                                ++low;
                                break;
                            }
                            case 1 : {
                                ++enough;
                                break;
                            }
                            case 2 : {
                                ++tooMuch;
                                break;
                            }
                        }
                    }

                    // 하트 갯수
                    lowHeartCnt.setText(String.valueOf(low));
                    enoughHeartCnt.setText(String.valueOf(enough));
                    tooMuchHeartCnt.setText(String.valueOf(tooMuch));
                } else {
                    Log.e("프래그먼트 데이터 테스트", "달력 하트 정보 리스트 - 값 못 가져옴");
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
