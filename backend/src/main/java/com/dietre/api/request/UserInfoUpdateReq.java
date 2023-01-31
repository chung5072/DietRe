package com.dietre.api.request;

import com.dietre.common.type.ActivityLevel;
import com.dietre.common.type.Gender;
import com.dietre.common.type.UserDesiredStatus;
import com.dietre.common.util.ActivityLevelMapper;
import com.dietre.common.util.AgeCalculator;
import com.dietre.db.entity.User;
import com.dietre.db.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class UserInfoUpdateReq {
    @Min(0)
    Integer age;
    Gender gender;
    @Min(0)
    Double height;
    @Min(0)
    Double weight;
    UserDesiredStatus status;
    @Min(0)
    Integer activityLevel;
    Integer recommendedCalorie;
    Integer recommendedCarbohydrate;
    Integer recommendedProtein;
    Integer recommendedFat;
    Boolean countBreakfast;
    Boolean countWeekend;


    public UserInfo updateEntity(UserInfo userInfo) {
        ActivityLevel activityLevel = ActivityLevelMapper.mapIntegerToActivityLevel(getActivityLevel());
        if (activityLevel != null) {
            userInfo.setActivityLevel(activityLevel);
        }
        if (getRecommendedCalorie() != null) {
            userInfo.setRecommendedCalorie(getRecommendedCalorie().doubleValue());
        }
        if (getRecommendedCarbohydrate() != null) {
            userInfo.setRecommendedCarbohydrate(getRecommendedCarbohydrate().doubleValue());
        }
        if (getRecommendedProtein() != null) {
            userInfo.setRecommendedProtein(getRecommendedProtein().doubleValue());
        }
        if (getRecommendedFat() != null) {
            userInfo.setRecommendedFat(getRecommendedFat().doubleValue());
        }
        if (AgeCalculator.ageToBirthYear(getAge()) != null) {
            userInfo.setBirthYear(AgeCalculator.ageToBirthYear(getAge()));
        }
        if (getGender() != null) {
            userInfo.setGender(getGender());
        }
        if (getHeight() != null) {
            userInfo.setHeight(getHeight());
        }
        if (getWeight() != null) {
            userInfo.setWeight(getWeight());
        }
        if (getStatus() != null) {
            userInfo.setDesiredStatus(getStatus());
        }
        if (getCountBreakfast() != null) {
            userInfo.setCountBreakfast(getCountBreakfast());
        }
        if (getCountWeekend() != null) {
            userInfo.setCountWeekend(getCountWeekend());
        }
        return userInfo;
    }

    public UserInfo dtoToEntity(Long userId, Integer originalAge) {
        ActivityLevel activityLevel = ActivityLevelMapper.mapIntegerToActivityLevel(getActivityLevel());
        Double recommendedCarbohydrate = getRecommendedCarbohydrate() == null ? null
                : getRecommendedCarbohydrate().doubleValue();
        Double recommendedCalorie = getRecommendedCalorie() == null ? null
                : getRecommendedCalorie().doubleValue();
        Double recommendedProtein = getRecommendedProtein() == null ? null
                : getRecommendedProtein().doubleValue();
        Double recommendedFat = getRecommendedFat() == null ? null
                : getRecommendedFat().doubleValue();

        Integer age = getAge() == originalAge ? null : AgeCalculator.ageToBirthYear(getAge());

        return UserInfo.builder()
                .id(userId)
                .birthYear(age)
                .gender(getGender())
                .height(getHeight())
                .weight(getWeight())
                .desiredStatus(getStatus())
                .activityLevel(activityLevel)
                .recommendedCalorie(recommendedCalorie)
                .recommendedCarbohydrate(recommendedCarbohydrate)
                .recommendedProtein(recommendedProtein)
                .recommendedFat(recommendedFat)
                .countBreakfast(getCountBreakfast())
                .countWeekend(getCountWeekend())
                .build();
    }

}
