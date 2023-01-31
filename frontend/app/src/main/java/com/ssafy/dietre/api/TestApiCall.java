package com.ssafy.dietre.api;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.ssafy.dietre.api.response.MealRegisterRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.response.BaseRes;
import com.ssafy.dietre.databinding.FragmentCalendarBinding;
import com.ssafy.dietre.ui.calendar.CalendarFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestApiCall extends BaseApiCall {

    public static void call(FragmentCalendarBinding binding, CalendarFragment calendarFragment) {
        service = ApiCallServiceProvider.provide();
        Call call = service.testCall();
        ArrayList<Integer> testList = new ArrayList<Integer>();
        testList.add(0);
        testList.add(0);
        testList.add(20);
        testList.add(20);
        testList.add(20);
        testList.add(20);
        testList.add(20);

        call.enqueue(new Callback<MealRegisterRes>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<MealRegisterRes> call, Response<MealRegisterRes> response) {
                if (response.isSuccessful()) {
                    View root = binding.getRoot();
//                    calendarFragment.makeCalendar(root, testList);
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