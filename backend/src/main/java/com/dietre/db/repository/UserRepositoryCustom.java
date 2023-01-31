package com.dietre.db.repository;

import com.dietre.common.type.SocialLoginType;
import com.dietre.db.entity.User;

public interface UserRepositoryCustom {

    // 회원가입을 헀는지 확인
    User checkUser(String email, SocialLoginType loginType);

    User checkUser(String id, String email, SocialLoginType loginType);

}
