package com.dietre.common.util;

import com.dietre.common.type.Gender;
import com.dietre.db.entity.UserInfo;

public class HarrisBenedictCalculator {
    public static Double calculate(UserInfo userInfo) {
        Double BMR;
        Double weight = userInfo.getWeight();
        Double height = userInfo.getHeight();
        Integer age = AgeCalculator.calculate(userInfo);


        if (userInfo.getGender() == Gender.male) {
            BMR = 66.5 + (13.75 * weight) + (5.003 * height) - (6.75 * age);
        }
        else {
            BMR = 655.1 + (9.563 * weight) + (1.85 * height) - (4.676 * age);
        }
        return BMR;
    }
}
