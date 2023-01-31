package com.ssafy.dietre.api.response;

import com.ssafy.dietre.api.data.FoodDto;
import com.ssafy.dietre.api.data.FoodStatistics;
import com.ssafy.dietre.common.response.BaseRes;

public class MealRecommendRes extends BaseRes {
    String date;
    Boolean isSingle;
    FoodDto mainDish;
    FoodDto side1;
    FoodDto side2;
    FoodDto soup;
    FoodDto kimchi;
    FoodDto desert;
    FoodStatistics totalStatistics;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getSingle() {
        return isSingle;
    }

    public void setSingle(Boolean single) {
        isSingle = single;
    }

    public FoodDto getMainDish() {
        return mainDish;
    }

    public void setMainDish(FoodDto mainDish) {
        this.mainDish = mainDish;
    }

    public FoodDto getSide1() {
        return side1;
    }

    public void setSide1(FoodDto side1) {
        this.side1 = side1;
    }

    public FoodDto getSide2() {
        return side2;
    }

    public void setSide2(FoodDto side2) {
        this.side2 = side2;
    }

    public FoodDto getSoup() {
        return soup;
    }

    public void setSoup(FoodDto soup) {
        this.soup = soup;
    }

    public FoodDto getKimchi() {
        return kimchi;
    }

    public void setKimchi(FoodDto kimchi) {
        this.kimchi = kimchi;
    }

    public FoodDto getDesert() {
        return desert;
    }

    public void setDesert(FoodDto desert) {
        this.desert = desert;
    }

    public FoodStatistics getTotalStatistics() {
        return totalStatistics;
    }

    public void setTotalStatistics(FoodStatistics totalStatistics) {
        this.totalStatistics = totalStatistics;
    }
}
