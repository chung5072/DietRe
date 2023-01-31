package com.ssafy.dietre;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ssafy.dietre.common.provider.MainActivityProvider;
import com.ssafy.dietre.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // 메인 페이지
    private ActivityMainBinding binding_main;
    private String login = "FAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("MAIN ACTIVITY 실행");
        MainActivityProvider.setActivity(this);

        /*
            인텐트로 페이지 이동 및 값을 넘기는 방법
            참고 사이트:
            1. 버튼 클릭으로 페이지 이동: https://deumdroid.tistory.com/29
            2. 인텐트 널 체크 방법: https://daldalhanstory.tistory.com/206
         */
        Intent intent = getIntent();
        SharedPreferences preferences = getSharedPreferences("jwt", MODE_PRIVATE);
        System.out.println(preferences.getString("jwt", null));

        if (preferences.getString("jwt", null) == null) {
            System.out.println("Login 확인");
            // 이동할 페이지 선택
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            // 해당 인텐트의 컴포넌트는 UnLoggedActivity
            // 페이지 이동
            startActivity(intent);
        } else {
            System.out.println("Main 바인딩");
            binding_main = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding_main.getRoot());

            // nav바 구성
            BottomNavigationView navView = findViewById(R.id.nav_view);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_calendar, R.id.navigation_pick)
                    .build();
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(navView, navController);
        }
    }

    // 기타 fragment 전환 화면
    public void changeFragment(int layout, Fragment f) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null)
                .replace(layout, f).commit(); // Fragment가 사용할 activity내 layout -> nav바 공간 제외한 layout
    }
}
