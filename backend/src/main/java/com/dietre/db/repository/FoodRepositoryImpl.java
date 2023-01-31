package com.dietre.db.repository;

import com.dietre.common.type.FoodCategory;
import com.dietre.db.entity.Food;
import com.dietre.db.entity.QFood;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class FoodRepositoryImpl implements FoodRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    private QFood food = QFood.food;

    @Override
    public List<Food> getAllFoods(String foodName, String foodType) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!StringUtils.isBlank(foodName)) {
            builder.and(food.name.contains(foodName));
        }

        if (!StringUtils.isBlank(foodType)) {
            builder.and(food.type.eq(FoodCategory.valueOf(foodType)));
        }

        return jpaQueryFactory.selectFrom(food).where(builder).fetch();
    }
}
