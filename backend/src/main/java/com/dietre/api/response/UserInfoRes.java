package com.dietre.api.response;

import com.dietre.common.model.response.BaseRes;
import com.dietre.common.type.ActivityLevel;
import com.dietre.common.type.Gender;
import com.dietre.common.type.UserDesiredStatus;
import com.dietre.common.util.AgeCalculator;
import com.dietre.db.entity.User;
import com.dietre.db.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoRes extends BaseRes {
    private UserInfoRes() {}

    Gender gender;

    Integer height;

    Integer weight;

    Integer age;

    Integer recommendedCalorie;

    Integer recommendedCarbohydrate;

    Integer recommendedProtein;

    Integer recommendedFat;

    Integer activityLevel;

    UserDesiredStatus status;

    Boolean countBreakfast;

    Boolean countWeekend;

    String name;

    String email;

    public static UserInfoRes of(int statusCode, String message, User user) {
        UserInfoRes userInfoRes = new UserInfoRes();

        UserInfo userInfo = user.getUserInfo();

        userInfoRes.setStatusCode(statusCode);
        userInfoRes.setMessage(message);

        userInfoRes.setGender(userInfo.getGender());
        userInfoRes.setHeight(userInfo.getHeight().intValue());
        userInfoRes.setWeight(userInfo.getWeight().intValue());
        userInfoRes.setAge(AgeCalculator.calculate(userInfo));
        userInfoRes.setRecommendedCalorie(userInfo.getRecommendedCalorie().intValue());
        userInfoRes.setRecommendedCarbohydrate(userInfo.getRecommendedCarbohydrate().intValue());
        userInfoRes.setRecommendedProtein(userInfo.getRecommendedProtein().intValue());
        userInfoRes.setRecommendedFat(userInfo.getRecommendedFat().intValue());
        userInfoRes.setActivityLevel(userInfo.getActivityLevel().getOrder());
        userInfoRes.setStatus(userInfo.getDesiredStatus());
        userInfoRes.setCountBreakfast(userInfo.getCountBreakfast());
        userInfoRes.setCountWeekend(userInfo.getCountWeekend());
        userInfoRes.setEmail(user.getEmail());
        userInfoRes.setName(user.getNickname());

        return userInfoRes;
    }
}
