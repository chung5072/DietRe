package com.dietre.api.service;

import com.dietre.common.type.FoodCategory;
import com.dietre.db.entity.Food;
import com.dietre.db.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public List<Food> getAllFoods(String foodName, String foodCategory) {
        return foodRepository.getAllFoods(foodName, foodCategory);
    }

    public List<Food> getAllFoods() {
        return  foodRepository.findAll();
    }

    public Food getFoodById(Long foodId) {

        return foodRepository.findById(foodId).get();
    }
}
