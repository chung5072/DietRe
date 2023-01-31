package com.dietre.api.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RecommendedNutrientData {
    public Integer recommendedCalorie;
    public Integer recommendedCarbohydrate;
    public Integer recommendedFat;
    public Integer recommendedProtein;
}
