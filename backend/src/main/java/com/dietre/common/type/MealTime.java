package com.dietre.common.type;

import com.dietre.db.entity.UserInfo;

public enum MealTime {
    breakfast, lunch, dinner;

    private Double ratio;

    public Double getRatio(UserInfo userInfo) {
        if (userInfo.getCountBreakfast()) {
            if (this == breakfast) {
                return 0.3;
            }
            else if (this == lunch) {
                return 0.4;
            }
            else {
                return 0.3;
            }
        }
        else {
            if (this == lunch) {
                return 0.5;
            }
            else if (this == dinner) {
                return 0.5;
            }
            else {
                return 0.0;
            }
        }
    }
}
