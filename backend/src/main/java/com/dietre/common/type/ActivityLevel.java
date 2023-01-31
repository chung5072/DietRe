package com.dietre.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ActivityLevel {
    little(1.2, 1), light(1.375, 2), moderate(1.55, 3), hard(1.725,4), very_hard(1.9, 5);

    private final Double rate;
    private final int order;
}
