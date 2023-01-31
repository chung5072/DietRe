package com.ssafy.dietre.ui.survey;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PhysicViewModel extends ViewModel {
    private MutableLiveData<PhysicInfo> mutablePhysicInfo;

    public PhysicViewModel() {
        mutablePhysicInfo = new MutableLiveData<>();
    }

    public MutableLiveData<PhysicInfo> getPhysicInfo() {
        if (mutablePhysicInfo == null) {
            mutablePhysicInfo = new MutableLiveData<PhysicInfo>();
        }
        return mutablePhysicInfo;
    }

    public void setPhysicInfo(PhysicInfo physicInfo) {
        mutablePhysicInfo.postValue(physicInfo);
    }
}