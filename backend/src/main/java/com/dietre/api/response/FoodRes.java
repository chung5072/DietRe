package com.dietre.api.response;

import com.dietre.common.type.FoodCategory;
import com.dietre.db.entity.Food;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class FoodRes {

    private Long foodId;
    private String name;
    private Integer calorie;
    private Integer carbohydrate;
    private Integer fat;
    private Integer protein;
    private FoodCategory foodType;


    public static List<FoodRes> entityToDtoList(List<Food> foods) {
        List<FoodRes> list = new ArrayList<>();
        for (Food food : foods) {
            list.add(FoodRes.entityToDto(food));
        }
        return list;
    }

    private static FoodRes entityToDto(Food food) {
        return FoodRes.builder()
                .foodId(food.getId())
                .calorie(food.getCalorie().intValue())
                .name(food.getName())
                .carbohydrate(food.getCarbohydrate().intValue())
                .fat(food.getFat().intValue())
                .protein(food.getProtein().intValue())
                .foodType(food.getType())
                .build();
    }
}
