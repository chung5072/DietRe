package com.ssafy.dietre.common.interceptor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ssafy.dietre.MainActivity;
import com.ssafy.dietre.common.provider.MainActivityProvider;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtExceptionInterceptor implements Interceptor {


    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        System.out.println(response.code());

        if (response.code() == 419) {
            /* TODO: 처리할 내용: 토큰 지우기, 화면한번 홈으로 보내기 */
            SharedPreferences.Editor editor = MainActivityProvider.getActivity()
                    .getSharedPreferences("jwt", Context.MODE_PRIVATE).edit();
            editor.remove("jwt");
            editor.commit();

            Intent intent = new Intent(MainActivityProvider.getActivity().getApplicationContext(), MainActivity.class);
            MainActivityProvider.getActivity().startActivity(intent);
        }
        return response;
    }
}