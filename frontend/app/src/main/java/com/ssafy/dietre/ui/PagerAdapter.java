package com.ssafy.dietre.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ssafy.dietre.ui.week.WeekViewModel;
import com.ssafy.dietre.ui.week.WeekchartFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class PagerAdapter extends FragmentStateAdapter {

    private int mCnt;
    private static int PAGE_NUMBER = 4;

    public PagerAdapter(@NonNull FragmentActivity fa, int count) {
        super(fa);
        mCnt = count;
    }

    // TODO 데이터 받아와서 arraylist에 세팅해주기

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return WeekchartFragment.getInstance("탄수화물", 1);
            case 2:
                return WeekchartFragment.getInstance("단백질", 2);
            case 3:
                return WeekchartFragment.getInstance("지방", 3);
            default:
                return WeekchartFragment.getInstance("칼로리", 0);
        }
    }

    @Override
    public int getItemCount() {
        return PAGE_NUMBER;
    }
}
