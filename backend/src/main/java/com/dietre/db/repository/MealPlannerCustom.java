package com.dietre.db.repository;

import com.dietre.api.response.DailyMealRes;
import com.dietre.api.response.MonthlyMealRecordRes;
import com.dietre.api.response.SpecificMealInfoRes;
import com.dietre.common.type.MealTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MealPlannerCustom {
    public List<MonthlyMealRecordRes> getMonthlyMealPlanner(Long userId, LocalDate date);

    public DailyMealRes getDailyMealPlanner(Long userId, LocalDate date);

    public SpecificMealInfoRes getSpecificMealInfo(Long userId, LocalDate date, MealTime mealTime);
}
