package com.ssafy.dietre.api.response;

import com.ssafy.dietre.api.data.FoodData;
import com.ssafy.dietre.api.data.MealStatistics;
import com.ssafy.dietre.common.response.BaseRes;
import com.ssafy.dietre.common.type.MealTime;

import java.util.List;

public class SpecificMealInfoRes extends BaseRes {
    String date;

    MealTime mealTime;

    Boolean isSingle;

    String mainImageUrl;

    List<FoodData> foodList;

    MealStatistics analysisResult;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MealTime getMealTime() {
        return mealTime;
    }

    public void setMealTime(MealTime mealTime) {
        this.mealTime = mealTime;
    }

    public Boolean getSingle() {
        return isSingle;
    }

    public void setSingle(Boolean single) {
        isSingle = single;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public List<FoodData> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<FoodData> foodList) {
        this.foodList = foodList;
    }

    public MealStatistics getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(MealStatistics analysisResult) {
        this.analysisResult = analysisResult;
    }


}
