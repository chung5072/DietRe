package com.dietre.api.response;

import com.dietre.common.model.response.BaseRes;
import com.dietre.db.entity.Food;
import com.dietre.db.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FoodSearchRes extends BaseRes {

    List<FoodRes> data;

    public static FoodSearchRes of(int statusCode, String message, List<Food> foodList) {
        FoodSearchRes res = new FoodSearchRes(FoodRes.entityToDtoList(foodList));
        res.setStatusCode(statusCode);
        res.setMessage(message);
        return res;
    }
}
