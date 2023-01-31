package com.dietre.api.controller;

import com.dietre.api.response.FoodSearchRes;
import com.dietre.api.response.RecommendedNutrientRes;
import com.dietre.api.service.FoodService;
import com.dietre.common.type.FoodCategory;
import com.dietre.db.entity.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @GetMapping("/list")
    public ResponseEntity<FoodSearchRes> getAllFoods(@RequestParam(required = false) String foodName,
                                                     @RequestParam(required = false) String foodType) {
        List<Food> foods = foodService.getAllFoods(foodName, foodType);
        return ResponseEntity.status(200).body(FoodSearchRes.of(200, "Success", foods));
    }
}
