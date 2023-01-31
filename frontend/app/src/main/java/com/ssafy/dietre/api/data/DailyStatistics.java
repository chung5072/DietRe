package com.ssafy.dietre.api.data;

public class DailyStatistics extends StatisticsDto {
    Integer score;
    Integer relativeScore;
    String date;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
