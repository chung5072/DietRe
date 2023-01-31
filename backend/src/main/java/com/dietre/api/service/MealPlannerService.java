package com.dietre.api.service;

import com.dietre.api.response.DailyMealRes;
import com.dietre.api.response.MonthlyMealRecordRes;
import com.dietre.api.response.SpecificMealInfoRes;
import com.dietre.db.entity.MealPlanner;
import com.dietre.db.repository.MealPlannerRepository;
import com.dietre.db.repository.MealPlannerToFoodRepository;

import com.dietre.api.data.MealData;
import com.dietre.common.type.MealTime;
import com.dietre.common.util.DtoConverter;
import com.dietre.db.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealPlannerService {

    private final MealPlannerRepository mealPlannerRepository;
    private final MealPlannerToFoodRepository mealPlannerToFoodRepository;
    private final DtoConverter dtoConverter;

    public List<MonthlyMealRecordRes> getMonthlyMealPlanner(Long userId, LocalDate date) {
        System.out.println(userId);
        System.out.println(date.getMonthValue());
        return mealPlannerRepository.getMonthlyMealPlanner(userId, date);
//        return null;
    }

    public DailyMealRes getDailyMealPlanner(Long userId, LocalDate date) {
        return mealPlannerRepository.getDailyMealPlanner(userId, date);
//        return null;
    }


    public MealPlanner register(User user, MealData req) {
        MealPlanner oldMealPlanner = mealPlannerRepository.findByUserIdAndDateAndMealTime(
                user.getId(), req.getDate(), req.getMealTime());

        MealPlanner newMealPlanner = dtoConverter.buildMealPlanner(user, req, oldMealPlanner);

        return mealPlannerRepository.save(newMealPlanner);

    }

    public MealPlanner search(Long userId, LocalDate date, MealTime mealTime) {
        return mealPlannerRepository.findByUserIdAndDateAndMealTime(userId, date, mealTime);
    }

    public SpecificMealInfoRes getSpecificMealInfo(Long userId, LocalDate date, MealTime mealTime) {
        return mealPlannerRepository.getSpecificMealInfo(userId, date, mealTime);
    }


    public MealPlanner update(Long mealPlannerId, MealData req) {
        MealPlanner oldMealPlanner = mealPlannerRepository.findById(mealPlannerId).get();
//        List<MealPlannerToFood> oldMealPlannerToFoodList = oldMealPlanner.getMealPlannerToFoodList();
//        mealPlannerToFoodRepository.deleteAll(oldMealPlannerToFoodList);
        MealPlanner newMealPlanner = dtoConverter.updateMealPlannerByMealData(oldMealPlanner, req);
        return mealPlannerRepository.save(newMealPlanner);
    }
}
