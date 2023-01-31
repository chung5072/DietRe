package com.dietre.api.data;

import com.dietre.common.util.ScoreCalculator;
import com.dietre.db.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeeklyStatistics extends StatisticsDto {
    Integer weekVariable = 7;

    /* TODO: score 계산 로직 작성 필요 */
    Integer score = 95;
    Integer relativeScore = 5;

    public void buildStatistics(UserInfo userInfo, List<DailyStatistics> dailyStatisticsList) {
        setRecommendations(userInfo);
        setRecords(dailyStatisticsList);
        setScores();
    }

    public void setScores() {
        setScore(ScoreCalculator.calculateScore(this));
        setRelativeScore(ScoreCalculator.getRelativeScore());
    }

    public void setRecommendations(UserInfo userInfo) {
        if (!userInfo.getCountWeekend()) {
            setWeekVariable(5);
        }
        setRecommendedCalorie(userInfo.getRecommendedCalorie().intValue());
        setRecommendedCarbohydrate(userInfo.getRecommendedCarbohydrate().intValue());
        setRecommendedProtein(userInfo.getRecommendedProtein().intValue());
        setRecommendedFat(userInfo.getRecommendedFat().intValue());
    }

    public void setRecords(List<DailyStatistics> dailyStatistics) {
        NutrientData nutrientData = aggregateWeeklyNutrients(dailyStatistics);
        setRealCalorie(nutrientData.getCalorie().intValue());
        setRealCarbohydrate(nutrientData.getCarbohydrate().intValue());
        setRealProtein(nutrientData.getProtein().intValue());
        setRealFat(nutrientData.getFat().intValue());
    }

    public NutrientData aggregateWeeklyNutrients(List<DailyStatistics> dailyMealPlanners) {
        Double realCalorie = 0.0;
        Double realCarbohydrate = 0.0;
        Double realProtein = 0.0;
        Double realFat = 0.0;

        for (DailyStatistics dailyStatistics : dailyMealPlanners) {
            realCalorie = realCalorie + dailyStatistics.getRealCalorie();
            realCarbohydrate = realCarbohydrate + dailyStatistics.getRealCarbohydrate();
            realFat = realFat + dailyStatistics.getRealFat();
            realProtein = realProtein + dailyStatistics.getRealProtein();
        }

        return new NutrientData(realCalorie, realCarbohydrate, realFat, realProtein);
    }
}
