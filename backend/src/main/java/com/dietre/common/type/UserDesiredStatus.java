package com.dietre.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserDesiredStatus {
    decrease(-500), maintain(0), increase(500);

    private final Integer threshold; // 감량 = 정상 - 500까지, 증량 = 정상 + 500까지;

    public Double getCarbohydrateCoefficient() {
        return 0.5;
    }

    public Double getProteinRatio() {
        return 0.2;
    }

    public Double getFatRatio() {
        return 0.3;
    }


}
