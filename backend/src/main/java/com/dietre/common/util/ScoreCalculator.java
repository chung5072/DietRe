package com.dietre.common.util;

import com.dietre.api.data.WeeklyStatistics;

public class ScoreCalculator {
    public static Integer calculateScore(WeeklyStatistics weeklyStatistics) {
        Double score = 0.0;
        score += calculateEachPart(40.0, weeklyStatistics
                .getRealCalorie()
                .doubleValue() / weeklyStatistics.getWeekVariable(),
                weeklyStatistics
                .getRecommendedCalorie()
                .doubleValue());

        score += calculateEachPart(20.0, weeklyStatistics
                .getRealCarbohydrate()
                .doubleValue() / weeklyStatistics.getWeekVariable(),
                weeklyStatistics
                .getRecommendedCarbohydrate()
                .doubleValue());

        score += calculateEachPart(20.0, weeklyStatistics
                .getRealProtein()
                .doubleValue() / weeklyStatistics.getWeekVariable(),
                weeklyStatistics
                .getRecommendedProtein()
                .doubleValue());

        score += calculateEachPart(20.0, weeklyStatistics
                .getRealFat()
                .doubleValue() / weeklyStatistics.getWeekVariable(),
                weeklyStatistics
                .getRecommendedFat()
                .doubleValue());

        return score.intValue();
    }

    public static Double calculateEachPart(Double weight, Double realValue, Double recommendedValue) {
        Double score = weight - Math.abs(realValue - recommendedValue) / recommendedValue * weight;
        if (score < 0.0) {
            return 0.0;
        }
        return score;
    }

    public static Integer getRelativeScore() {
        return 1;
    }
}
