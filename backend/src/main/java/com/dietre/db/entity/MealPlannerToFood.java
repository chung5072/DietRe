package com.dietre.db.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealPlannerToFood {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "mealPlannerId")
    MealPlanner mealPlanner;

    @ManyToOne
    @JoinColumn(name = "foodId")
    Food food;
}
