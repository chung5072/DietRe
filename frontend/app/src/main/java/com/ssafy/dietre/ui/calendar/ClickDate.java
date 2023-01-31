package com.ssafy.dietre.ui.calendar;

import java.time.LocalDate;

public interface ClickDate {
    /**
     * 클릭했는지 확인하여 bottom sheet를 올림
     * @param click
     * @param day
     */
    void isClicked(boolean click, LocalDate day);

    /**
     * 마지막 날인지 확인하여 마지막 날이면 bottom sheet를 내림
     */
    void isEdgeDate();
}
