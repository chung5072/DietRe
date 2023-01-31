package com.dietre.api.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NutrientData {
    public Double calorie;
    public Double carbohydrate;
    public Double fat;
    public Double protein;
}