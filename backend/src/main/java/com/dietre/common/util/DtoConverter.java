package com.dietre.common.util;

import com.dietre.api.data.FoodData;
import com.dietre.api.data.MealData;
import com.dietre.api.data.NutrientData;
import com.dietre.common.exception.IllegalMealTimeException;
import com.dietre.common.type.MealTime;
import com.dietre.db.entity.Food;
import com.dietre.db.entity.MealPlanner;
import com.dietre.db.entity.MealPlannerToFood;
import com.dietre.db.entity.User;
import com.dietre.db.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DtoConverter {

    private final FoodRepository foodRepository;

    public List<MealPlannerToFood> foodListMapper(List<Long> foodList, MealPlanner mealPlanner) {
        return foodList
                .stream()
                .map(foodId -> MealPlannerToFood
                        .builder()
                        .mealPlanner(mealPlanner)
                        .food(foodRepository
                                .findById(foodId)
                                .get())
                        .build())
                .collect(Collectors.toList());
    }

    public NutrientData getTotalNutrientsFromMealPlannerToFoods(List<MealPlannerToFood> mealPlannerToFoodList) {
        Double totalCalorie = 0.0;
        Double totalProtein = 0.0;
        Double totalCarbohydrate = 0.0;
        Double totalFat = 0.0;
        for (MealPlannerToFood mealPlannerToFood : mealPlannerToFoodList) {
            Food food = mealPlannerToFood.getFood();
            totalCalorie += food.getCalorie();
            totalProtein += food.getProtein();
            totalCarbohydrate += food.getCarbohydrate();
            totalFat += food.getFat();
        }

        return NutrientData
                .builder()
                .calorie(totalCalorie)
                .fat(totalFat)
                .protein(totalProtein)
                .carbohydrate(totalCarbohydrate)
                .build();
    }


    public MealPlanner updateMealPlannerByMealData(MealPlanner mealPlanner, MealData mealData) {
        List<Long> foodList = mealData.getFoodList();
        // 조인 테이블에도 데이터를 삽입하기 위해 식단에 포함된 foodId와 mealDataId를 채워준다.
        List<MealPlannerToFood> mealPlannerToFoodList = foodListMapper(foodList, mealPlanner);

        // 영양정보를 채워준다.
        NutrientData nutrientData = getTotalNutrientsFromMealPlannerToFoods(mealPlannerToFoodList);
        mealPlanner.setNutrientInfo(nutrientData);
        mealPlanner.getMealPlannerToFoodList().clear();
        mealPlanner.getMealPlannerToFoodList().addAll(mealPlannerToFoodList);

        return mealPlanner;
    }


    public MealPlanner buildMealPlanner(User user, MealData mealData, MealPlanner oldMealPlanner) {
        if(!user.getUserInfo().getCountBreakfast() && (mealData.getMealTime() == MealTime.breakfast)) {
            throw new IllegalMealTimeException();
        }

        if (!user.getUserInfo().getCountWeekend() && LocalDateUtil.isWeekend(mealData.getDate())) {
            throw new IllegalMealTimeException();
        }

        MealPlanner result;
        if (oldMealPlanner == null) {
            result = MealPlanner
                    .builder()
                    .user(user)
                    .date(mealData.getDate())
                    .mealTime(mealData.getMealTime())
                    .isSingle(mealData.getIsSingle())
                    .imageUrl(mealData.getImageUrl())
                    .build();
        } else {
            result = oldMealPlanner;
            result.setImageUrl(mealData.getImageUrl());
        }


        return updateMealPlannerByMealData(result, mealData);
    }

    public NutrientData getNutrientDataOfMealPlanners(List<MealPlanner> mealPlannerList) {
        List<MealPlannerToFood> mealPlannerToFoodList = new ArrayList<>();
        for (MealPlanner currentMealPlanner : mealPlannerList) {
            mealPlannerToFoodList.addAll(currentMealPlanner.getMealPlannerToFoodList());
        }

        return getTotalNutrientsFromMealPlannerToFoods(mealPlannerToFoodList);
    }

    public List<FoodData> getFoodDataListFromMealPlanner(MealPlanner mealPlanner) {
        List<MealPlannerToFood> mealPlannerToFoodList = mealPlanner.getMealPlannerToFoodList();

        return mealPlannerToFoodList
                .stream()
                .map(m -> new FoodData(m.getFood().getId(), m.getFood().getImageUrl()))
                .collect(Collectors.toList());
    }
}
