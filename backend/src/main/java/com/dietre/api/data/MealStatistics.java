package com.dietre.api.data;

import com.dietre.common.type.MealTime;
import com.dietre.db.entity.MealPlanner;
import com.dietre.db.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MealStatistics extends StatisticsDto {

    // 하루 2끼 유저일 경우 점심/저녁 5:5, 3끼 유저는 아침/점심/저녁 3:4:3
    public void setRecommendations(UserInfo userInfo, MealTime mealTime) {
        Double ratio = mealTime.getRatio(userInfo);
        setRecommendedCalorie((int)(userInfo.getRecommendedCalorie() * ratio));
        setRecommendedCarbohydrate((int)(userInfo.getRecommendedCarbohydrate() * ratio));
        setRecommendedProtein((int)(userInfo.getRecommendedProtein() * ratio));
        setRecommendedFat((int)(userInfo.getRecommendedFat() * ratio));
    }

    public void setRecords(MealPlanner mealPlanner) {
        setRealCalorie(mealPlanner.getCalorie().intValue());
        setRealCarbohydrate(mealPlanner.getCarbohydrate().intValue());
        setRealProtein(mealPlanner.getProtein().intValue());
        setRealFat(mealPlanner.getFat().intValue());
    }
}
