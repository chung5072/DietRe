package com.dietre.db.entity;

import com.dietre.api.data.NutrientData;
import com.dietre.common.type.MealTime;
import lombok.*;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealPlanner {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(nullable = false)
    LocalDate date;

    @Enumerated(EnumType.STRING)
    MealTime mealTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(length = 100)
    String imageName;
    @Column(length = 200)
    String imageUrl;

    @Column(columnDefinition="TINYINT(4)")
    Boolean isSingle;

    @Column(nullable = false)
    Double calorie;

    @Column(nullable = false)
    Double carbohydrate;

    @Column(nullable = false)
    Double protein;

    @Column(nullable = false)
    Double fat;

    @Builder.Default
    @OneToMany(mappedBy = "mealPlanner", cascade = ALL, orphanRemoval = true)
    List<MealPlannerToFood> mealPlannerToFoodList = new ArrayList<>();

    public void setNutrientInfo(NutrientData nutrientData) {
        setCarbohydrate(nutrientData.getCarbohydrate());
        setCalorie(nutrientData.getCalorie());
        setFat(nutrientData.getFat());
        setProtein(nutrientData.getProtein());
    }
}
