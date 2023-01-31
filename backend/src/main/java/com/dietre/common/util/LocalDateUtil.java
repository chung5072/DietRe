package com.dietre.common.util;

import java.time.LocalDate;

public class LocalDateUtil {
    public static Boolean isWeekend(LocalDate date) {
        if (date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7) {
            return true;
        }
        return false;
    }
}
