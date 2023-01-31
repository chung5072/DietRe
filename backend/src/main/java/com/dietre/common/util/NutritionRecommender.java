package com.dietre.common.util;

import com.dietre.api.data.RecommendedNutrientData;
import com.dietre.common.type.UserDesiredStatus;
import com.dietre.db.entity.UserInfo;

public class NutritionRecommender {
    public static RecommendedNutrientData buildRecommendation(UserInfo userInfo, UserDesiredStatus status) {

        return build(userInfo, status);
    }

    public static RecommendedNutrientData buildRecommendation(UserInfo userInfo) {
        UserDesiredStatus status = userInfo.getDesiredStatus();

        return build(userInfo, status);
    }

    private static RecommendedNutrientData build(UserInfo userInfo, UserDesiredStatus status) {
        Double activityRate = userInfo.getActivityLevel().getRate();
        Double recommendedCalorie = HarrisBenedictCalculator.calculate(userInfo) * activityRate + status.getThreshold();

        Double recommendedProtein = recommendedCalorie * status.getProteinRatio() / 4.0;
        Double recommendedCarbohydrate = recommendedCalorie * status.getCarbohydrateCoefficient() / 4.0;
        Double recommendedFat = recommendedCalorie * status.getFatRatio() / 9.0;

        return RecommendedNutrientData
                .builder()
                .recommendedCalorie(recommendedCalorie.intValue())
                .recommendedProtein(recommendedProtein.intValue())
                .recommendedFat(recommendedFat.intValue())
                .recommendedCarbohydrate(recommendedCarbohydrate.intValue())
                .build();
    }
}
