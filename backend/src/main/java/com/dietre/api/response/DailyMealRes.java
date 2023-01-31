package com.dietre.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailyMealRes {

    private Integer recommendedCalorie;
    private Integer realCalorie;
    private Integer recommendedCarbohydrate;
    private Integer realCarbohydrate;
    private Integer recommendedFat;
    private Integer realFat;
    private Integer recommendedProtein;
    private Integer realProtein;
}
