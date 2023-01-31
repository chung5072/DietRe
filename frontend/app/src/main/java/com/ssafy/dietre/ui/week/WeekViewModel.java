package com.ssafy.dietre.ui.week;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ssafy.dietre.api.ApiCallService;
import com.ssafy.dietre.api.data.DailyStatistics;
import com.ssafy.dietre.api.response.WeeklyAnalysisRes;
import com.ssafy.dietre.api.response.WeeklyStatistics;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeekViewModel extends ViewModel {
    // week call로부터 데이터를 받아서 저장할 arraylist
    private MutableLiveData<ArrayList<Integer>> kcal;
    private MutableLiveData<ArrayList<Integer>> carbo;
    private MutableLiveData<ArrayList<Integer>> protein;
    private MutableLiveData<ArrayList<Integer>> fat;
    private MutableLiveData<Integer> dayCnt;
    private MutableLiveData<String[]> day;


    public WeekViewModel() {
        kcal = new MutableLiveData<>();
        carbo = new MutableLiveData<>();
        protein = new MutableLiveData<>();
        fat = new MutableLiveData<>();
        dayCnt = new MutableLiveData<>();
        day = new MutableLiveData<>();
    }

    // week 정보 세팅
    public void setWeek(ArrayList<Integer> kcalArr, ArrayList<Integer> carboArr, ArrayList<Integer> proteinArr, ArrayList<Integer> fatArr, String[] dayArr) {
        kcal.setValue(kcalArr);
        carbo.setValue(carboArr);
        protein.setValue(proteinArr);
        fat.setValue(fatArr);
        dayCnt.setValue(dayArr.length);
        day.setValue(dayArr);
        System.out.println("setting day: "+dayCnt.getValue());
    }

    // kcal 배열 호출
    public ArrayList<Integer> getKcalArr() {
        return kcal.getValue();
    }

    // kcal 배열 호출
    public ArrayList<Integer> getCarboArr() {
        return carbo.getValue();
    }

    // kcal 배열 호출
    public ArrayList<Integer> getProteinArr() {
        return protein.getValue();
    }

    // kcal 배열 호출
    public ArrayList<Integer> getFatArr() {
        return fat.getValue();
    }

    // 요일 배열 호출
    public String[] getWeekDay() {
        return day.getValue();
    }

    // 요일 길이 호출
    public int getWeekLength() {
        return dayCnt.getValue();
    }

}