package com.dietre.api.response;

import com.dietre.common.model.response.BaseRes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealRegisterRes extends BaseRes {
    Long mealPlannerId;

    public static MealRegisterRes of(int statusCode, String message, Long mealPlannerId) {
        MealRegisterRes mealRegisterRes = new MealRegisterRes();
        mealRegisterRes.setStatusCode(statusCode);
        mealRegisterRes.setMealPlannerId(mealPlannerId);
        mealRegisterRes.setMessage(message);

        return mealRegisterRes;
    }
}
