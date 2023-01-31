package com.dietre.db.entity;

import com.dietre.api.data.FoodDto;
import com.dietre.common.type.FoodCategory;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Food {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(length = 30, nullable = false)
    String name;

    @Column(nullable = false)
    Double amount;

    @Column(nullable = false)
    Double calorie;

    @Column(nullable = false)
    Double carbohydrate;

    @Column(nullable = false)
    Double protein;

    @Column(nullable = false)
    Double fat;

    @Column
    String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    FoodCategory type;

    @Column(columnDefinition="TINYINT(4)")
    Boolean isSingle;

    public FoodDto mapFoodDto (Double rate) {
        FoodDto foodDto = new FoodDto();

        foodDto.setFoodId(getId());
        foodDto.setCalories(getCalorie().intValue());
        foodDto.setCarbohydrates(getCarbohydrate().intValue());
        foodDto.setProtein(getProtein().intValue());
        foodDto.setFat(getFat().intValue());
        foodDto.setName(getName());
        foodDto.setImgUrl(getImageUrl());
        foodDto.setWeight(getAmount().intValue());
        foodDto.setAmount(rate);

        return foodDto;
    }

}

