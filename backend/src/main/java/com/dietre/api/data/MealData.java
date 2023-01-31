package com.dietre.api.data;

import com.dietre.common.type.MealTime;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MealData {
    LocalDate date;
    MealTime mealTime;
    Boolean isSingle;
    String imageUrl;
    List<Long> foodList;
}
