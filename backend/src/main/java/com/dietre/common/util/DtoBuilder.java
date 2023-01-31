package com.dietre.common.util;

import com.dietre.api.data.DailyStatistics;
import com.dietre.api.data.FoodDto;
import com.dietre.api.data.FoodStatistics;
import com.dietre.api.data.WeeklyStatistics;
import com.dietre.db.entity.DailyHistory;
import com.dietre.db.entity.UserInfo;
import com.dietre.db.repository.DailyHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DtoBuilder {
    private final DailyHistoryRepository dailyHistoryRepository;

    public DailyStatistics buildDailyStatistics(LocalDate date,
                                                UserInfo userInfo,
                                                DailyHistory dailyHistory) {
        DailyStatistics statistics = new DailyStatistics();
        statistics.buildStatistics(date, userInfo, dailyHistory);

        return statistics;
    }


    public List<DailyStatistics> buildDailyStatisticsList(
            UserInfo userInfo) {

        Integer weekVariable = 7;
        if (!userInfo.getCountWeekend()) {
            weekVariable = 5;
        }

        LocalDate today = LocalDate.now().minusDays(7);
        LocalDate firstDay = today.minusDays(today.getDayOfWeek().getValue() - 1);
        List<DailyStatistics> dailyStatisticsList = new ArrayList<>();
        for (int i = 0; i < weekVariable; i++) {
            LocalDate current = firstDay.plusDays(i);

            DailyHistory dailyHistory = dailyHistoryRepository
                    .findByUserIdAndDate(userInfo.getUser().getId(), current).orElse(null);

            DailyStatistics dailyStatistics;
            if (dailyHistory == null) {
                dailyStatistics = new DailyStatistics();
                dailyStatistics.setDate(current);
            } else {
                dailyStatistics = buildDailyStatistics(current, userInfo, dailyHistory);
            }
            dailyStatisticsList.add(dailyStatistics);
        }

        return dailyStatisticsList;
    }

    public WeeklyStatistics buildWeeklyStatistics(UserInfo userInfo,
                                                  List<DailyStatistics> dailyStatisticsList) {
        WeeklyStatistics weeklyStatistics = new WeeklyStatistics();
        weeklyStatistics.buildStatistics(userInfo, dailyStatisticsList);
        return weeklyStatistics;
    }

    public Map<String, Object> buildFoodMap(LocalDate date, Boolean isSingle) {
        /* TODO: 추천 알고리즘 결정 시 변경 */
        Map<String, Object> foodMap = new HashMap<>();
        foodMap.put("date", date);
        foodMap.put("isSingle", isSingle);

        FoodDto sampleFoodDto = new FoodDto();
        sampleFoodDto.setFoodId(0L);
        sampleFoodDto.setCarbohydrates(0);
        sampleFoodDto.setCalories(0);
        sampleFoodDto.setProtein(0);
        sampleFoodDto.setFat(0);
        sampleFoodDto.setName("sample");
        sampleFoodDto.setImgUrl("sample_url");
        sampleFoodDto.setWeight(0);

        foodMap.put("mainDish", sampleFoodDto);
        if (!isSingle) {
            foodMap.put("side1", sampleFoodDto);
            foodMap.put("side2", sampleFoodDto);
            foodMap.put("soup", sampleFoodDto);
            foodMap.put("kimchi", sampleFoodDto);
            foodMap.put("dessert", sampleFoodDto);
        }

        FoodStatistics sampleFoodStatistics = new FoodStatistics();
        sampleFoodStatistics.setWeight(0);
        sampleFoodStatistics.setFat(0);
        sampleFoodStatistics.setCalorie(0);
        sampleFoodStatistics.setCarbohydrate(0);
        sampleFoodStatistics.setProtein(0);

        foodMap.put("foodStatistics", sampleFoodStatistics);

        return foodMap;
    }
}
