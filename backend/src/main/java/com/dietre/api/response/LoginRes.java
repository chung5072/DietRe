package com.dietre.api.response;

import com.dietre.common.model.response.BaseRes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRes extends BaseRes {
    Boolean hasUserInfo;

    public static LoginRes of(Integer statusCode, String message, Boolean hasUserInfo) {
        LoginRes loginRes = new LoginRes();
        loginRes.setMessage(message);
        loginRes.setStatusCode(statusCode);
        loginRes.setHasUserInfo(hasUserInfo);

        return loginRes;
    }
}
