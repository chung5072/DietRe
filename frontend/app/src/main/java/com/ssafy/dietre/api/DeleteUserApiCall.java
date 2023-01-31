package com.ssafy.dietre.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.ssafy.dietre.LoginActivity;
import com.ssafy.dietre.api.response.RecommendedNutrientRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.provider.MainActivityProvider;
import com.ssafy.dietre.ui.user.UserFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteUserApiCall extends BaseApiCall {

    public static void call(UserFragment userFragment) {
        service = ApiCallServiceProvider.provide();

        Call call = service.deleteUser();

        call.enqueue(new Callback<RecommendedNutrientRes>() {
            @Override
            public void onResponse(Call<RecommendedNutrientRes> call, Response<RecommendedNutrientRes> response) {
                SharedPreferences.Editor editor = MainActivityProvider.getActivity()
                        .getSharedPreferences("jwt", Context.MODE_PRIVATE).edit();
                editor.remove("jwt");
                editor.commit();

                Intent intent = new Intent(userFragment.getContext(), LoginActivity.class);
                userFragment.startActivity(intent);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.v("api 테스트 실패시", t.toString());
            }
        });
    }
}
