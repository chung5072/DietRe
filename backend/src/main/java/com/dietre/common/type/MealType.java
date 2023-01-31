package com.dietre.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MealType {
    single(true), combo(false);
    private final boolean isSingle;
}
