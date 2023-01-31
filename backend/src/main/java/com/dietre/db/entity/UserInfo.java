package com.dietre.db.entity;

import com.dietre.api.data.RecommendedNutrientData;
import com.dietre.common.type.ActivityLevel;
import com.dietre.common.type.Gender;
import com.dietre.common.type.UserDesiredStatus;
import com.dietre.common.util.AgeCalculator;
import lombok.*;

import javax.persistence.*;


import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Gender gender;

    @Column(nullable = false)
    Double height;

    @Column(nullable = false)
    Double weight;

    @Column(nullable = false)
    Integer birthYear;

    Double recommendedCalorie;

    Double recommendedCarbohydrate;

    Double recommendedProtein;

    Double recommendedFat;

    @Column(columnDefinition = "TINYINT(4)")
    Boolean countWeekend;

    @Column(columnDefinition = "TINYINT(4)")
    Boolean countBreakfast;

    @Enumerated(EnumType.STRING)
    ActivityLevel activityLevel;

    @Enumerated(EnumType.STRING)
    UserDesiredStatus desiredStatus;

    @OneToOne(mappedBy = "userInfo")
    User user;


    public void setRecommendations(RecommendedNutrientData data) {
        setRecommendedCalorie(data.getRecommendedCalorie().doubleValue());
        setRecommendedProtein(data.getRecommendedProtein().doubleValue());
        setRecommendedFat(data.getRecommendedFat().doubleValue());
        setRecommendedCarbohydrate(data.getRecommendedCarbohydrate().doubleValue());
    }

    public void buildBasicUserInfo(UserDesiredStatus status, Gender gender,
                                   Integer height, Integer weight,
                                   ActivityLevel activityLevel, Integer age) {
        setDesiredStatus(status);
        setGender(gender);
        setHeight(height.doubleValue());
        setWeight(weight.doubleValue());
        setActivityLevel(activityLevel);
        setBirthYear(AgeCalculator.ageToBirthYear(age));
    }
}
