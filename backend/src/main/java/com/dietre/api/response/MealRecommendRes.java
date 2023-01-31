package com.dietre.api.response;

import com.dietre.api.data.FoodData;
import com.dietre.api.data.FoodDto;
import com.dietre.api.data.FoodStatistics;
import com.dietre.common.model.response.BaseRes;
import com.dietre.db.entity.Food;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class MealRecommendRes extends BaseRes {

    LocalDate date;
    Boolean isSingle;
    FoodDto mainDish;
    FoodDto side1;
    FoodDto side2;
    FoodDto soup;
    FoodDto kimchi;
    FoodStatistics totalStatistics;

    public static MealRecommendRes of(int statusCode, String message,
                                      Map<String, Object> foodMap) {
        MealRecommendRes mealRecommendRes = new MealRecommendRes();
        mealRecommendRes.setDate((LocalDate) foodMap.get("date"));
        mealRecommendRes.setIsSingle((Boolean) foodMap.get("isSingle"));
        mealRecommendRes.setStatusCode(statusCode);
        mealRecommendRes.setMessage(message);
        mealRecommendRes.setMainDish((FoodDto) foodMap.get("mainDish"));
        if (!mealRecommendRes.getIsSingle()) {
            mealRecommendRes.setSide1((FoodDto)foodMap.get("side1"));
            mealRecommendRes.setSide2((FoodDto) foodMap.get("side2"));
            mealRecommendRes.setSoup((FoodDto) foodMap.get("soup"));
            mealRecommendRes.setKimchi((FoodDto) foodMap.get("kimchi"));
        }

        FoodStatistics totalStatistics = (FoodStatistics) foodMap.get("foodStatistics");
        mealRecommendRes.setTotalStatistics(totalStatistics);

        return mealRecommendRes;
    }
}
