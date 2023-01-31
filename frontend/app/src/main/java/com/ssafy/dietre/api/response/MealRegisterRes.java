package com.ssafy.dietre.api.response;

import com.ssafy.dietre.common.response.BaseRes;

public class MealRegisterRes extends BaseRes {
    Long mealPlannerId;

    public Long getMealPlannerId() {
        return mealPlannerId;
    }

    public void setMealPlannerId(Long mealPlannerId) {
        this.mealPlannerId = mealPlannerId;
    }
}
