package com.dietre.api.response;

import com.dietre.api.data.RecommendedNutrientData;
import com.dietre.common.model.response.BaseRes;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendedNutrientRes extends BaseRes {
    RecommendedNutrientData data;
    public static RecommendedNutrientRes of(int statusCode, String message, RecommendedNutrientData data) {
        RecommendedNutrientRes recommendedNutrientRes = new RecommendedNutrientRes();
        recommendedNutrientRes.setMessage(message);
        recommendedNutrientRes.setStatusCode(statusCode);
        recommendedNutrientRes.setData(data);

        return recommendedNutrientRes;
    }
}
