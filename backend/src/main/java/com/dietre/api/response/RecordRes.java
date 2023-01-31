package com.dietre.api.response;

import com.dietre.common.type.MealTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecordRes {
    MealTime mealTime;
    Integer calorie;
    Integer carbohydrate;
    Integer fat;
    Integer protein;
}
