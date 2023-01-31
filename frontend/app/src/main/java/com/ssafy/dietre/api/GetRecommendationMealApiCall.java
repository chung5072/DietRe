package com.ssafy.dietre.api;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.response.MealRecommendRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.type.MealTime;
import com.ssafy.dietre.common.type.MealType;
import com.ssafy.dietre.common.type.UserDesiredStatus;
import com.ssafy.dietre.ui.home.HomeFragment;
import com.ssafy.dietre.ui.home.HomeViewModel;
import com.ssafy.dietre.ui.home.LoadingRecoFragment;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetRecommendationMealApiCall extends BaseApiCall {
    public static void call(MealTime mealTime, UserDesiredStatus userDesiredStatus, MealType mealType,
                            ImageView singleFoodImg, ImageView mainFoodImg, ImageView soupImg,
                            ImageView sideOneImg, ImageView sideTwoImg, ImageView kimchiImg,
                            LinearLayout noneLayout, LinearLayout trayLayout, LinearLayout singleLayout,
                            LoadingRecoFragment loadingRecoFragment, HomeFragment homeFragment,
                            HomeViewModel homeViewModel) {
        service = ApiCallServiceProvider.provide();

        Call call = service.getRecommendation(mealTime, userDesiredStatus, mealType);

        call.enqueue(new Callback<MealRecommendRes>() {
            @Override
            public void onResponse(Call<MealRecommendRes> call, Response<MealRecommendRes> response) {
                if (response.isSuccessful()) {
                    // 로딩 alert 창 지움
                    loadingRecoFragment.dismiss();

                    Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 성공");
                    MealRecommendRes res = response.body();
                    homeViewModel.setRecoInfo(res);
                    Bitmap imageBitmap;
                    if (res.getSingle()) {
                        // 단품
                        Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 단품 이름" + res.getMainDish().getName());
                        Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 단품 경로" + res.getMainDish().getImgUrl());
                        imageBitmap = getImageBitmap(res.getMainDish().getImgUrl());
                        singleFoodImg.setImageBitmap(imageBitmap);

                        noneLayout.setVisibility(View.GONE);
                        trayLayout.setVisibility(View.GONE);
                        singleLayout.setVisibility(View.VISIBLE);
                    } else {
                        // 식판
                        Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 밥 이름" + res.getMainDish().getName());
                        Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 밥 경로" + res.getMainDish().getImgUrl());
                        imageBitmap = getImageBitmap(res.getMainDish().getImgUrl());
                        mainFoodImg.setImageBitmap(imageBitmap);

                        Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 국 이름" + res.getSoup().getName());
                        Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 국 경로" + res.getSoup().getImgUrl());
                        imageBitmap = getImageBitmap(res.getSoup().getImgUrl());
                        soupImg.setImageBitmap(imageBitmap);

                        Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 사이드1 이름" + res.getSide1().getName());
                        Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 사이드1 경로" + res.getSide1().getImgUrl());
                        imageBitmap = getImageBitmap(res.getSide1().getImgUrl());
                        sideOneImg.setImageBitmap(imageBitmap);

                        Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 사이드2 이름" + res.getSide2().getName());
                        Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 사이드2 경로" + res.getSide2().getImgUrl());
                        imageBitmap = getImageBitmap(res.getSide2().getImgUrl());
                        sideTwoImg.setImageBitmap(imageBitmap);

                        Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 김치 이름" + res.getKimchi().getName());
                        Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 김치 경로" + res.getKimchi().getImgUrl());
                        imageBitmap = getImageBitmap(res.getKimchi().getImgUrl());
                        kimchiImg.setImageBitmap(imageBitmap);

                        // 추천받고 나서 레이아웃을 보여줌
                        noneLayout.setVisibility(View.GONE);
                        trayLayout.setVisibility(View.VISIBLE);
                        singleLayout.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 응답원본");
                    Log.e("응답 원본", String.valueOf((response.code())));
                    // 로딩 alert창 지움
                    loadingRecoFragment.dismiss();
                    // 아직 추천을 못받았기 때문에 레이아웃을 보여주지 않음
                    noneLayout.setVisibility(View.VISIBLE);
                    trayLayout.setVisibility(View.GONE);
                    singleLayout.setVisibility(View.GONE);
                    // 새로운 추천을 받아보라는 권유
                    new MaterialAlertDialogBuilder(homeFragment.getContext(), R.style.AlertDialogTheme)
                        .setTitle("다른 조건으로 검색해보세요")
                        .setPositiveButton("다시 추천받기", new DialogInterface.OnClickListener() {
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
                Log.e("프래그먼트 데이터 테스트", "겟 추천 식단 - 실패");
                Log.e("api 테스트 실패시", t.toString());
                // 로딩 alert창 지움
                loadingRecoFragment.dismiss();
                // 아직 추천을 못받았기 때문에 레이아웃을 보여주지 않음
                noneLayout.setVisibility(View.VISIBLE);
                trayLayout.setVisibility(View.GONE);
                singleLayout.setVisibility(View.GONE);
                // 새로운 추천을 받아보라는 권유
                new MaterialAlertDialogBuilder(homeFragment.getContext(), R.style.AlertDialogTheme)
                    .setTitle("다른 조건으로 검색해보세요")
                    // 우측을 누르면 다이얼로그가 꺼짐
                    .setPositiveButton("다시 추천받기", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
            }
        });
    }

    /**
     * 서버에 있는 이미지를 불러옴
     * @param url
     * @return
     */
    private static Bitmap getImageBitmap(String url) {
        checkpolicy();
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("프래그먼트 데이터 테스트", "이미지 로드 - Error getting bitmap", e);
        }
        return bm;
    }

    /**
     * 권한 허용
     */
    private static void checkpolicy() {
        if (Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}
