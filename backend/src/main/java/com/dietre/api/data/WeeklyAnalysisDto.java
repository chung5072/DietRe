package com.dietre.api.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeeklyAnalysisDto {
    List<DailyStatistics> dailyStatisticsList;
    WeeklyStatistics weeklyStatistics;
}
