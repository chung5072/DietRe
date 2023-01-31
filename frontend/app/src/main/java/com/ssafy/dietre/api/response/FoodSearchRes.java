package com.ssafy.dietre.api.response;

import com.ssafy.dietre.common.response.BaseRes;

import java.util.List;

public class FoodSearchRes extends BaseRes {
    List<FoodRes> data;

    public List<FoodRes> getData() {
        return data;
    }

    public void setData(List<FoodRes> data) {
        this.data = data;
    }
}
