package com.ssafy.dietre.api.data;

import com.ssafy.dietre.common.type.MealTime;

import java.util.List;

public class MealData {
    public MealData(String date, MealTime mealTime, String imageUrl, List<Long> foodList) {
        this.date = date;
        this.mealTime = mealTime;
        this.imageUrl = imageUrl;
        this.foodList = foodList;
    }

    String date;
    MealTime mealTime;
    String imageUrl;
    List<Long> foodList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public MealTime getMealTime() {
        return mealTime;
    }

    public void setMealTime(MealTime mealTime) {
        this.mealTime = mealTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Long> getFoodList() {
        return foodList;
    }

    public void setFoodList(List<Long> foodList) {
        this.foodList = foodList;
    }
}
