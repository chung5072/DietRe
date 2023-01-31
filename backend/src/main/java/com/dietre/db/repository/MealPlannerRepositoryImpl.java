package com.dietre.db.repository;

import com.dietre.api.response.DailyMealRes;
import com.dietre.api.response.MonthlyMealRecordRes;
import com.dietre.api.response.RecordRes;
import com.dietre.api.response.SpecificMealInfoRes;
import com.dietre.common.type.MealTime;
import com.dietre.db.entity.Food;
import com.dietre.db.entity.MealPlanner;
import com.dietre.db.entity.User;
import com.dietre.db.entity.UserInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

import static com.dietre.db.entity.QFood.food;
import static com.dietre.db.entity.QMealPlanner.mealPlanner;
import static com.dietre.db.entity.QMealPlannerToFood.mealPlannerToFood;
import static com.dietre.db.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class MealPlannerRepositoryImpl implements MealPlannerCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MonthlyMealRecordRes> getMonthlyMealPlanner(Long userId, LocalDate date) {
        List<MealPlanner> mealPlanners = jpaQueryFactory.select(mealPlanner)
                .from(mealPlanner)
                .where(mealPlanner.user.id.eq(userId).and(mealPlanner.date.month().eq(date.getMonthValue())))
                .orderBy(mealPlanner.date.asc())
                .fetch();

        User myUser = jpaQueryFactory
                .select(user)
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne();

        UserInfo userInfo = myUser.getUserInfo();
        Double recommendedCalorie = userInfo.getRecommendedCalorie();

        Map<LocalDate, MonthlyMealRecordRes> map = new HashMap<>();

        for (MealPlanner planner : mealPlanners) {
            System.out.println(planner.getMealTime() + " " + planner.getCalorie() + " " + planner.getDate());
            MonthlyMealRecordRes monthlyMealRecordRes;
            if (map.get(planner.getDate()) == null) {
                monthlyMealRecordRes = new MonthlyMealRecordRes(planner.getDate(), new ArrayList<>());
            } else {
                monthlyMealRecordRes = map.get(planner.getDate());
            }
            monthlyMealRecordRes.getList().add(RecordRes.builder()
                    .mealTime(planner.getMealTime())
                    .calorie(planner.getCalorie().intValue())
                    .carbohydrate(planner.getCarbohydrate().intValue())
                    .fat(planner.getFat().intValue())
                    .protein(planner.getProtein().intValue())
                    .build());
            monthlyMealRecordRes.setTotalCalorie((int)(monthlyMealRecordRes.getTotalCalorie() + planner.getCalorie()));
            if (monthlyMealRecordRes.getTotalCalorie() > recommendedCalorie + 100) {
                monthlyMealRecordRes.setHeart(2);
            } else if (monthlyMealRecordRes.getTotalCalorie() > recommendedCalorie - 100) {
                monthlyMealRecordRes.setHeart(1);
            } else {
                monthlyMealRecordRes.setHeart(0);
            }
            map.put(planner.getDate(), monthlyMealRecordRes);
        }

        List<MonthlyMealRecordRes> list = new ArrayList<>();
        for (int i = 1; i <= date.getDayOfMonth(); i++) {
            if (map.get(LocalDate.of(date.getYear(), date.getMonthValue(), i)) == null) {
                map.put(LocalDate.of(date.getYear(), date.getMonthValue(), i), new MonthlyMealRecordRes(LocalDate.of(date.getYear(), date.getMonthValue(), i), 0, -1));
            }
            list.add(map.get(LocalDate.of(date.getYear(), date.getMonthValue(), i)));
        }

        return list;
    }

    @Override
    public DailyMealRes getDailyMealPlanner(Long userId, LocalDate date) {
//        return jpaQueryFactory.select(mealPlanner, mealPlannerToFood, food)
//                .from(mealPlanner)
//                .join(mealPlannerToFood)
//                .on(mealPlanner.id.eq(mealPlannerToFood.id))
//                .join(food)
//                .on(mealPlannerToFood.food.id.eq(food.id))
//                .where(mealPlanner.user.id.eq(userId).and(mealPlanner.date.eq(date)))
//                .fetchAll().fetch();

        List<MealPlanner> mealPlanners = jpaQueryFactory
                .select(mealPlanner)
                .from(mealPlanner)
                .where(mealPlanner.user.id.eq(userId).and(mealPlanner.date.eq(date)))
                .fetch();

        User myUser = jpaQueryFactory.select(user)
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne();

        Double realProtein = 0.0;
        Double realCalorie = 0.0;
        Double realCarbohydrate = 0.0;
        Double realFat = 0.0;

        for (MealPlanner planner : mealPlanners) {
            realCarbohydrate += planner.getCarbohydrate();
            realFat += planner.getFat();
            realProtein += planner.getProtein();
            realCalorie += planner.getCalorie();
        }

        UserInfo userInfo = myUser.getUserInfo();
        Double recommendedProtein = userInfo.getRecommendedProtein();
        Double recommendedCarbohydrate = userInfo.getRecommendedCarbohydrate();
        Double recommendedFat = userInfo.getRecommendedFat();
        Double recommendedCalorie = userInfo.getRecommendedCalorie();

        return DailyMealRes.builder()
                .realCalorie(realCalorie.intValue())
                .realCarbohydrate(realCarbohydrate.intValue())
                .realProtein(realProtein.intValue())
                .realFat(realFat.intValue())
                .recommendedCalorie(recommendedCalorie.intValue())
                .recommendedCarbohydrate(recommendedCarbohydrate.intValue())
                .recommendedProtein(recommendedProtein.intValue())
                .recommendedFat(recommendedFat.intValue())
                .build();
    }

    @Override
    public SpecificMealInfoRes getSpecificMealInfo(Long userId, LocalDate date, MealTime mealTime) {

        MealPlanner myMealPlanner = jpaQueryFactory.select(mealPlanner)
                .from(mealPlanner)
                .where(user.id.eq(userId).and(mealPlanner.date.eq(date).and(mealPlanner.mealTime.eq(mealTime))))
                .fetchOne();

        List<Food> foods = jpaQueryFactory.select(food)
                .from(food)
                .join(mealPlannerToFood)
                .on(food.id.eq(mealPlannerToFood.food.id))
                .where(mealPlannerToFood.mealPlanner.id.eq(
                        myMealPlanner.getId()
                ))
                .fetch();

        User myUser = jpaQueryFactory
                .select(user)
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne();


        return getSpecificMealInfoRes(date, mealTime, myMealPlanner, foods, myUser);
    }

    private static SpecificMealInfoRes getSpecificMealInfoRes(LocalDate date, MealTime mealTime, MealPlanner myMealPlanner, List<Food> foods, User myUser) {
        UserInfo userInfo = myUser.getUserInfo();

        SpecificMealInfoRes res = SpecificMealInfoRes.builder()
                .date(date)
                .mealTime(mealTime)
                .isSingle(myMealPlanner.getIsSingle())
                .mainImageUrl(myMealPlanner.getImageUrl())
                .build();

        res.setFoodList(foods);
        res.setAnalysisResultWithNutrition(userInfo.getRecommendedCalorie(), userInfo.getRecommendedCarbohydrate(),
                userInfo.getRecommendedFat(), userInfo.getRecommendedProtein(),
                myMealPlanner.getCalorie(), myMealPlanner.getCarbohydrate(),
                myMealPlanner.getFat(), myMealPlanner.getProtein(),
                mealTime.getRatio(userInfo)
        );
        return res;
    }

}
