package com.ssafy.dietre.api.response;

import com.ssafy.dietre.api.data.StatisticsDto;

public class WeeklyStatistics extends StatisticsDto {
    Integer weekVariable;
    Integer score;
    Integer relativeScore;

    public Integer getWeekVariable() {
        return weekVariable;
    }

    public void setWeekVariable(Integer weekVariable) {
        this.weekVariable = weekVariable;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getRelativeScore() {
        return relativeScore;
    }

    public void setRelativeScore(Integer relativeScore) {
        this.relativeScore = relativeScore;
    }
}
