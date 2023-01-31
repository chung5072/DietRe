package com.dietre.common.util;

import com.dietre.db.entity.UserInfo;

import java.time.LocalDate;

public class AgeCalculator {
    public static int calculate(UserInfo userInfo) {
        int birthYear = userInfo.getBirthYear();
        int nowYear = LocalDate.now().getYear();

        return nowYear - birthYear;
    }

    public static Integer ageToBirthYear(Integer age) {
        if (age == null) {
            return null;
        }
        int nowYear = LocalDate.now().getYear();

        return nowYear - age;
    }
}
