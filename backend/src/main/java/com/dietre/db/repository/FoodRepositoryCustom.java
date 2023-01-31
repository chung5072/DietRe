package com.dietre.db.repository;

import com.dietre.common.type.FoodCategory;
import com.dietre.db.entity.Food;

import java.util.List;

public interface FoodRepositoryCustom {
    List<Food> getAllFoods(String foodName, String foodType);
}
