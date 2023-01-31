package com.dietre.api.response;

import com.dietre.common.type.MealTime;
import com.dietre.db.entity.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class SpecificMealInfoRes {

    private LocalDate date;

    private MealTime mealTime;

    private Boolean isSingle;

    private String mainImageUrl;

    private List<FoodRes> foodList;

    private MealAnalysisRes analysisResult;

    public void setFoodList(List<Food> foodList) {
        ArrayList<FoodRes> list = new ArrayList<>();
        for (Food food : foodList) {
            list.add(new FoodRes(food.getId(),
                            food.getCalorie().intValue(),
                            food.getCarbohydrate().intValue(),
                            food.getProtein().intValue(),
                            food.getFat().intValue(),
                            food.getName()
                    )
            );
        }
        this.foodList = list;
    }

    public void setAnalysisResultWithNutrition(Double recommendedCalorie, Double recommendedCarbohydrate,
                                               Double recommendedFat, Double recommendedProtein,
                                               Double realCalorie, Double realCarbohydrate,
                                               Double realFat, Double realProtein, Double ratio) {
        this.analysisResult = new MealAnalysisRes((int) (recommendedCalorie * ratio), (int) (recommendedCarbohydrate * ratio),
                (int) (recommendedFat * ratio), (int) (recommendedProtein * ratio),
                realCalorie.intValue(), realCarbohydrate.intValue(),
                realFat.intValue(), realProtein.intValue());
    }

    @Getter
    @AllArgsConstructor
    private class FoodRes {

        private Long id;

        private Integer calorie;

        private Integer carbohydrate;

        private Integer protein;

        private Integer fat;

        private String name;

    }

    @Getter
    @AllArgsConstructor
    private class MealAnalysisRes {

        private Integer recommendedCalorie;
        private Integer recommendedCarbohydrate;
        private Integer recommendedFat;
        private Integer recommendedProtein;

        private Integer realCalorie;
        private Integer realCarbohydrate;
        private Integer realFat;
        private Integer realProtein;

    }


}
