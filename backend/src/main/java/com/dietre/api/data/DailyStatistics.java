package com.dietre.api.data;

import com.dietre.db.entity.DailyHistory;
import com.dietre.db.entity.MealPlanner;
import com.dietre.db.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DailyStatistics extends StatisticsDto {
    /* TODO: score 계산 로직 작성 필요 */
    Integer score = 0;
    Integer relativeScore = 0;
    LocalDate date;

    public void buildStatistics(LocalDate date, UserInfo userInfo, DailyHistory dailyHistory) {
        setDate(date);
        setRecommendations(userInfo);
        setRecords(dailyHistory);
    }

    public void setRecommendations(UserInfo userInfo) {
        setRecommendedCalorie(userInfo.getRecommendedCalorie().intValue());
        setRecommendedCarbohydrate(userInfo.getRecommendedCarbohydrate().intValue());
        setRecommendedProtein(userInfo.getRecommendedProtein().intValue());
        setRecommendedFat(userInfo.getRecommendedFat().intValue());
    }

    public void setRecords(DailyHistory dailyHistory) {
        setRealCalorie(dailyHistory.getCalorie().intValue());
        setRealProtein(dailyHistory.getProtein().intValue());
        setRealCarbohydrate(dailyHistory.getCarbohydrate().intValue());
        setRealFat(dailyHistory.getFat().intValue());
    }

//    public void setRecords(List<MealPlanner> dailyMealPlanners) {
//        NutrientData nutrientData = aggregateDailyNutrients(dailyMealPlanners);
//        setRealCalorie(nutrientData.getCalorie().intValue());
//        setRealCarbohydrate(nutrientData.getCarbohydrate().intValue());
//        setRealProtein(nutrientData.getProtein().intValue());
//        setRealFat(nutrientData.getFat().intValue());
//    }
//
//    public NutrientData aggregateDailyNutrients(List<MealPlanner> dailyMealPlanners) {
//        // 하루치 섭취 영양소를 종합한다.
//
//        Double realCalorie = 0.0;
//        Double realCarbohydrate = 0.0;
//        Double realProtein = 0.0;
//        Double realFat = 0.0;
//
//        for (MealPlanner mealPlanner : dailyMealPlanners) {
//            realCalorie = realCalorie + mealPlanner.getCalorie();
//            realCarbohydrate = realCarbohydrate + mealPlanner.getCarbohydrate();
//            realFat = realFat + mealPlanner.getFat();
//            realProtein = realProtein + mealPlanner.getProtein();
//        }
//
//        return new NutrientData(realCalorie, realCarbohydrate, realFat, realProtein);
//    }
}
