package com.dietre.api.request;

import com.dietre.common.type.SocialLoginType;
import lombok.Getter;

@Getter
public class UserLoginReq {
    private String id;
    private String email;
    private String name;
    private SocialLoginType type;
}
