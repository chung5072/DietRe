package com.dietre.api.request;

import com.dietre.common.type.ActivityLevel;
import com.dietre.common.type.Gender;
import com.dietre.common.type.UserDesiredStatus;
import com.dietre.common.util.ActivityLevelMapper;
import com.dietre.common.util.AgeCalculator;
import com.dietre.db.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserInfoRegisterReq {
    @NotNull @Min(0)
    Integer age;
    @NotNull
    Gender gender;
    @NotNull @Min(0)
    Double height;
    @NotNull @Min(0)
    Double weight;
    UserDesiredStatus status;
    @NotNull @Min(0)
    Integer activityLevel;
    Double recommendedCalorie;
    Double recommendedCarbohydrate;
    Double recommendedProtein;
    Double recommendedFat;
    Boolean countBreakfast;
    Boolean countWeekend;


    public UserInfo dtoToEntity(Long userId) {
        ActivityLevel activityLevel = ActivityLevelMapper.mapIntegerToActivityLevel(getActivityLevel());
        return UserInfo.builder()
                .id(userId)
                .birthYear(AgeCalculator.ageToBirthYear(getAge()))
                .gender(getGender())
                .height(getHeight())
                .weight(getWeight())
                .desiredStatus(getStatus())
                .activityLevel(activityLevel)
                .recommendedCalorie(getRecommendedCalorie())
                .recommendedCarbohydrate(getRecommendedCarbohydrate())
                .recommendedProtein(getRecommendedProtein())
                .recommendedFat(getRecommendedFat())
                .countBreakfast(getCountBreakfast())
                .countWeekend(getCountWeekend())
                .build();
    }

}
