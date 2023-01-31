package com.dietre.common.util;

import com.dietre.common.exception.IllegalActivityLevelException;
import com.dietre.common.type.ActivityLevel;

public class ActivityLevelMapper {
    public static ActivityLevel mapIntegerToActivityLevel(Integer level) {
        try {
            if (level == 1) {
                return ActivityLevel.little;
            }
            else if (level == 2) {
                return ActivityLevel.light;
            }
            else if (level == 3) {
                return ActivityLevel.moderate;
            }
            else if (level == 4) {
                return ActivityLevel.hard;
            }
            else if (level == 5) {
                return ActivityLevel.very_hard;
            }
            else {
                throw new IllegalActivityLevelException();
            }
        } catch (NullPointerException e) {
            return null;
        }

    }
}
