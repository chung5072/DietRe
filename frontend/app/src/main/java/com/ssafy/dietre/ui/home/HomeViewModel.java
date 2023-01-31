package com.ssafy.dietre.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ssafy.dietre.api.response.MealRecommendRes;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<MealRecommendRes> mRecoInfo;

    public HomeViewModel() {
        mRecoInfo = new MutableLiveData<>();
    }

    public MutableLiveData<MealRecommendRes> getRecoInfo() {
        if (mRecoInfo == null) {
            mRecoInfo = new MutableLiveData<MealRecommendRes>();
        }
        return mRecoInfo;
    }

    public void setRecoInfo(MealRecommendRes recoInfo) {
        mRecoInfo.postValue(recoInfo);
    }
}