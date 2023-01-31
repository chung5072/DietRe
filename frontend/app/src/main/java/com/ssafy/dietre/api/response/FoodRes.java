package com.ssafy.dietre.api.response;

import com.ssafy.dietre.common.type.FoodCategory;

public class FoodRes {
    private Long foodId;
    private String name;
    private Integer calorie;
    private Integer carbohydrate;
    private Integer fat;
    private Integer protein;
    private FoodCategory foodType;

    public Long getFoodId() {
        return foodId;
    }

    public String getName() {
        return name;
    }

    public void setFoodId(Long foodId) {
        this.foodId = foodId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }

    public void setCarbohydrate(Integer carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public void setFoodType(FoodCategory foodType) {
        this.foodType = foodType;
    }

    public Integer getCalorie() {
        return calorie;
    }

    public Integer getCarbohydrate() {
        return carbohydrate;
    }

    public Integer getFat() {
        return fat;
    }

    public Integer getProtein() {
        return protein;
    }

    public FoodCategory getFoodType() {
        return foodType;
    }
}
