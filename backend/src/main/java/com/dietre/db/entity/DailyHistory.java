package com.dietre.db.entity;

import com.dietre.api.data.NutrientData;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyHistory {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(nullable = false)
    LocalDate date;

    @Column(nullable = false)
    Double calorie;

    @Column(nullable = false)
    Double carbohydrate;

    @Column(nullable = false)
    Double protein;

    @Column(nullable = false)
    Double fat;

    Integer score;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    public void setNutrients(NutrientData nutrientData) {
        setCalorie(nutrientData.getCalorie());
        setCarbohydrate(nutrientData.getCarbohydrate());
        setProtein(nutrientData.getProtein());
        setFat(nutrientData.getFat());
    }
}
