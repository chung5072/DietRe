package com.ssafy.dietre.api.response;

import com.ssafy.dietre.common.response.BaseRes;

public class LoginRes extends BaseRes {
    Boolean hasUserInfo;

    public Boolean getHasUserInfo() {
        return hasUserInfo;
    }

    public void setHasUserInfo(Boolean hasUserInfo) {
        this.hasUserInfo = hasUserInfo;
    }
}
