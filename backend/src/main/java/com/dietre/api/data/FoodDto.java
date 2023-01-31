package com.dietre.api.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodDto {
    Long foodId;
    String name;
    String imgUrl;
    Integer weight;
    Integer calories;
    Integer carbohydrates;
    Integer protein;
    Integer fat;
    Double amount;
}
