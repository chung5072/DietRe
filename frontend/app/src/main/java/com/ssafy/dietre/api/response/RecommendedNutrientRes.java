package com.ssafy.dietre.api.response;

import com.ssafy.dietre.api.data.RecommendedNutrientData;
import com.ssafy.dietre.common.response.BaseRes;

public class RecommendedNutrientRes extends BaseRes {
    RecommendedNutrientData data;

    public RecommendedNutrientData getData() {
        return data;
    }

    public void setData(RecommendedNutrientData data) {
        this.data = data;
    }
}
