package com.ssafy.dietre.ui.survey;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NutViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public NutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("설문페이지 - 영양 정보 설문 화면");
    }

    public LiveData<String> getText() {
        return mText;
    }
}