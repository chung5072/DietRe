package com.dietre.db.repository;

import com.dietre.common.type.MealTime;
import com.dietre.db.entity.MealPlanner;
import com.dietre.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealPlannerRepository extends JpaRepository<MealPlanner, Long>,MealPlannerCustom {
    List<MealPlanner> findAllByUserAndDate(User user, LocalDate date);
    MealPlanner findByUserIdAndDateAndMealTime(Long userId, LocalDate date, MealTime mealTime);
}
