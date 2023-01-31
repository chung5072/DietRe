package com.ssafy.dietre.api.response;

import java.time.LocalDate;
import java.util.List;

public class MonthlyMealRecordRes {
    String date;
    Integer totalCalorie = 0;
    Integer heart;
    List<RecordRes> list;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTotalCalorie() {
        return totalCalorie;
    }

    public void setTotalCalorie(Integer totalCalorie) {
        this.totalCalorie = totalCalorie;
    }

    public Integer getHeart() {
        return heart;
    }

    public void setHeart(Integer heart) {
        this.heart = heart;
    }

    public List<RecordRes> getList() {
        return list;
    }

    public void setList(List<RecordRes> list) {
        this.list = list;
    }
}
