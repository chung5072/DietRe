package com.dietre.api.response;

import com.dietre.api.data.FoodData;
import com.dietre.api.data.MealStatistics;
import com.dietre.common.model.response.BaseRes;
import com.dietre.common.type.MealTime;
import com.dietre.db.entity.MealPlanner;
import com.dietre.db.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MealSearchRes extends BaseRes {
    LocalDate date;
    MealTime mealTime;
    Boolean isSingle;
    String mainImageUrl;
    List<FoodData> foodList;
    MealStatistics analysisResult = new MealStatistics();

    public static MealSearchRes of(int statusCode, String message,
                                   MealPlanner mealPlanner, List<FoodData> foodDataList) {
        UserInfo userInfo = mealPlanner.getUser().getUserInfo();
        MealTime mealTime = mealPlanner.getMealTime();

        MealSearchRes mealSearchRes = new MealSearchRes();
        mealSearchRes.setStatusCode(statusCode);
        mealSearchRes.setMessage(message);
        mealSearchRes.setDate(mealPlanner.getDate());
        mealSearchRes.setMealTime(mealPlanner.getMealTime());
        mealSearchRes.setIsSingle(mealPlanner.getIsSingle());
        mealSearchRes.setMainImageUrl(mealPlanner.getImageUrl());
        mealSearchRes.setFoodList(foodDataList);
        mealSearchRes.getAnalysisResult().setRecommendations(userInfo, mealTime);
        mealSearchRes.getAnalysisResult().setRecords(mealPlanner);

        return mealSearchRes;
    }
}
