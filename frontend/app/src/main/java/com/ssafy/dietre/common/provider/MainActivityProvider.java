package com.ssafy.dietre.common.provider;

import com.ssafy.dietre.MainActivity;

public class MainActivityProvider {

    private static MainActivity activity;

    public static void setActivity(MainActivity activity) {
        MainActivityProvider.activity = activity;
    }

    public static MainActivity getActivity() {
        return activity;
    }
}
