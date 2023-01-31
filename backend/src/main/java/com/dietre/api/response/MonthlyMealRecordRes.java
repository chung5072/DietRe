package com.dietre.api.response;

import com.dietre.common.type.MealTime;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MonthlyMealRecordRes {
    LocalDate date;
    Integer totalCalorie = 0;
    Integer heart;
    List<RecordRes> list;

    public MonthlyMealRecordRes(LocalDate date, List<RecordRes> list) {
        this.date = date;
        this.list = list;
    }

    public MonthlyMealRecordRes(LocalDate date, Integer totalCalorie, Integer heart) {
        this.date = date;
        this.totalCalorie = totalCalorie;
        this.heart = heart;
    }
}
