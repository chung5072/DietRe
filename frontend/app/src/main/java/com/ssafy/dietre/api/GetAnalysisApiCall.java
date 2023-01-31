package com.ssafy.dietre.api;

import android.content.DialogInterface;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ssafy.dietre.MainActivity;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.response.MealAnalysisRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.type.MealTime;
import com.ssafy.dietre.ui.pick.ResolveFragment;

import java.time.LocalDate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAnalysisApiCall extends BaseApiCall {
    public static void call(String date, MealTime mealTime, MainActivity mainActivity) {
        service = ApiCallServiceProvider.provide();

        Call call = service.getAnalysis(date, mealTime);

        call.enqueue(new Callback<MealAnalysisRes>() {
            @Override
            public void onResponse(Call<MealAnalysisRes> call, Response<MealAnalysisRes> response) {
                try {
                    Log.e("프래그먼트 데이터 테스트", "겟분석콜 - 성공");
                    response.body().getData().getRecommendedCalorie();
                    mainActivity.changeFragment(R.id.layout_calendar, ResolveFragment.getInstance(date, mealTime));
                } catch (NullPointerException nullErr) {
                    Log.e("프래그먼트 데이터 테스트", "겟분석콜 - 응답원본");
                    Log.e("응답 원본", String.valueOf((response.code())));
                    new MaterialAlertDialogBuilder(mainActivity, R.style.AlertDialogTheme)
                        .setTitle("입력된 식단 정보가 없습니다")
                        // 우측을 누르면 다이얼로그가 꺼짐
                        .setPositiveButton("돌아가기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("프래그먼트 데이터 테스트", "겟분석콜 - 실패");
                Log.v("api 테스트 실패시", t.toString());
            }
        });
    }
}

