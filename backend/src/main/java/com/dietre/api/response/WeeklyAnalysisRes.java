package com.dietre.api.response;

import com.dietre.api.data.DailyStatistics;
import com.dietre.api.data.NutrientData;
import com.dietre.api.data.WeeklyStatistics;
import com.dietre.common.model.response.BaseRes;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeeklyAnalysisRes extends BaseRes {

    List<DailyStatistics> dailyStatisticsList;
    WeeklyStatistics weeklyStatistics;


    public static WeeklyAnalysisRes of(int statusCode, String message,
                                       List<DailyStatistics> dailyStatisticsList,
                                       WeeklyStatistics weeklyStatistics) {
        WeeklyAnalysisRes weeklyAnalysisRes = new WeeklyAnalysisRes();

        weeklyAnalysisRes.setDailyStatisticsList(dailyStatisticsList);
        weeklyAnalysisRes.setWeeklyStatistics(weeklyStatistics);
        weeklyAnalysisRes.setStatusCode(statusCode);
        weeklyAnalysisRes.setMessage(message);
        weeklyAnalysisRes.setDailyStatisticsList(dailyStatisticsList);
        weeklyAnalysisRes.setWeeklyStatistics(weeklyStatistics);

        return weeklyAnalysisRes;
    }

}
