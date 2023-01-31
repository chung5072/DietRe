package com.ssafy.dietre.api.response;

import com.ssafy.dietre.api.data.MealStatistics;
import com.ssafy.dietre.common.response.BaseRes;

public class MealAnalysisRes extends BaseRes {
    MealStatistics data;

    public MealStatistics getData() {
        return data;
    }

    public void setData(MealStatistics data) {
        this.data = data;
    }
}
