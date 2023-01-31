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
public class UserInfoToFood {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "user_info_id")
    UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "food_id")
    Food food;

    @Column(nullable = false, columnDefinition="TINYINT(4)")
    Boolean isPreferred;
}
