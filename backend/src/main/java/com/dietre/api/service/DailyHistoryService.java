package com.dietre.api.service;

import com.dietre.api.data.NutrientData;
import com.dietre.common.util.DtoConverter;
import com.dietre.db.entity.DailyHistory;
import com.dietre.db.entity.MealPlanner;
import com.dietre.db.entity.User;
import com.dietre.db.repository.DailyHistoryRepository;
import com.dietre.db.repository.MealPlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyHistoryService {

    private final DailyHistoryRepository dailyHistoryRepository;
    private final MealPlannerRepository mealPlannerRepository;
    private final DtoConverter dtoConverter;

    @Transactional
    public void updateHistory(User user, MealPlanner mealPlanner) {
        LocalDate mealDate = mealPlanner.getDate();
        List<MealPlanner> mealPlannerList = mealPlannerRepository.findAllByUserAndDate(user, mealPlanner.getDate());
        NutrientData nutrientData = dtoConverter.getNutrientDataOfMealPlanners(mealPlannerList);
        DailyHistory dailyHistory = dailyHistoryRepository.findByUserAndDate(user, mealDate)
                .orElse(DailyHistory
                        .builder()
                        .user(user)
                        .date(mealDate)
                        .build());
        dailyHistory.setNutrients(nutrientData);
        /* TODO: score 설정하는 로직 작성 필요 */
        dailyHistory.setScore(100);
        dailyHistoryRepository.save(dailyHistory);
    }
}
