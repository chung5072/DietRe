package com.dietre.api.response;

import com.dietre.api.data.MealStatistics;
import com.dietre.common.model.response.BaseRes;
import com.dietre.common.type.MealTime;
import com.dietre.db.entity.MealPlanner;
import com.dietre.db.entity.UserInfo;
import io.swagger.models.auth.In;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MealAnalysisRes extends BaseRes {
    MealStatistics data = new MealStatistics();

    public static MealAnalysisRes of(int statusCode, String message,
                                     UserInfo userInfo, MealPlanner mealPlanner) {
        MealTime mealTime = mealPlanner.getMealTime();
        MealAnalysisRes mealAnalysisRes = new MealAnalysisRes();
        mealAnalysisRes.setStatusCode(statusCode);
        mealAnalysisRes.setMessage(message);
        mealAnalysisRes.getData().setRecommendations(userInfo, mealTime);
        mealAnalysisRes.getData().setRecords(mealPlanner);

        return mealAnalysisRes;
    }
}
