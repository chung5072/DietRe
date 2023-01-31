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
public class AllergyToIngredient {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_info_id")
    UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    Ingredient ingredient;

}
