package com.ssafy.dietre.api.response;

import com.ssafy.dietre.api.data.DailyStatistics;
import com.ssafy.dietre.common.response.BaseRes;

import java.util.List;

public class WeeklyAnalysisRes extends BaseRes {
    List<DailyStatistics> dailyStatisticsList;
    WeeklyStatistics weeklyStatistics;

    public List<DailyStatistics> getDailyStatisticsList() {
        return dailyStatisticsList;
    }

    public void setDailyStatisticsList(List<DailyStatistics> dailyStatisticsList) {
        this.dailyStatisticsList = dailyStatisticsList;
    }

    public WeeklyStatistics getWeeklyStatistics() {
        return weeklyStatistics;
    }

    public void setWeeklyStatistics(WeeklyStatistics weeklyStatistics) {
        this.weeklyStatistics = weeklyStatistics;
    }
}
