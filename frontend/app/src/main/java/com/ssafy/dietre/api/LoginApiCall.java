package com.ssafy.dietre.api;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ssafy.dietre.LoginActivity;
import com.ssafy.dietre.MainActivity;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.request.UserLoginReq;
import com.ssafy.dietre.api.response.LoginRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.provider.RetrofitProvider;
import com.ssafy.dietre.databinding.ActivityMainBinding;
import com.ssafy.dietre.ui.survey.PhysicFragment;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginApiCall extends BaseApiCall {

    private static SharedPreferences preferences;

    public static void call(UserLoginReq req, LoginActivity activity) throws IOException {
        service = ApiCallServiceProvider.provide();
        Call call = service.login(req);


        /* 동기 방식으로 호출할 경우
        Response<BaseRes> res = call.execute();
        if (!res.isSuccessful()) {
            // do something
        }
        BaseRes responseBody = res.body();
        */
        call.enqueue(new Callback<LoginRes>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                if (response.isSuccessful()) {
                    Log.v("api 테스트", "테스트 성공");
                    Log.v("응답 원본", String.valueOf(response.code()));
                    String token = response.headers().get("Authorization");
                    Log.v("토큰", token);

                    LoginRes loginRes = response.body();
                    Log.v("hasUserInfo", loginRes.getHasUserInfo().toString());

                    preferences = activity.getSharedPreferences("jwt", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("jwt", token);
                    editor.commit();

                    RetrofitProvider.rebuild(token);
                    // TODO 유저 정보가 있는지 체크한 후 여기서 분기 처리
                    if (loginRes.getHasUserInfo()) {
                        Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
                        activity.startActivity(intent);
                    } else {
                        // 프래그먼트 - 신체 설문 페이지
                        Fragment physicFragment = new PhysicFragment();
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        // 로그인 페이지 대체
                        fragmentTransaction.replace(R.id.fragment_login, physicFragment);
                        fragmentTransaction.commit();
                    }


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

