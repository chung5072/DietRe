package com.dietre.api.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StatisticsDto {
    Integer realCalorie = 0;
    Integer realCarbohydrate = 0;
    Integer realProtein = 0;
    Integer realFat = 0;
    Integer recommendedCarbohydrate = 0;
    Integer recommendedFat = 0;
    Integer recommendedProtein = 0;
    Integer recommendedCalorie = 0;
}
