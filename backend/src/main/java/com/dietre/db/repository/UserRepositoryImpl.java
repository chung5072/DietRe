package com.dietre.db.repository;

import com.dietre.common.type.SocialLoginType;
import com.dietre.db.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.dietre.db.entity.QUser.user;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public User checkUser(String email, SocialLoginType loginType) {
        return queryFactory.selectFrom(user)
                .where(user.email.eq(email).and(user.type.eq(loginType)))
                .fetchOne();
    }

    @Override
    public User checkUser(String id, String email, SocialLoginType loginType) {
        return queryFactory.selectFrom(user)
                .where(user.socialLoginId.eq(id).and(user.email.eq(email).and(user.type.eq(loginType))))
                .fetchOne();
    }
}
