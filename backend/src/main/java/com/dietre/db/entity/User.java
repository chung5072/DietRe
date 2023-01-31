package com.dietre.db.entity;

import com.dietre.common.type.SocialLoginType;
import lombok.*;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.REMOVE;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    Long id;

    @Column(nullable = false)
    String socialLoginId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    SocialLoginType type;

    @Column(nullable = false, length = 100)
    String email;

    String nickname;

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "user_info_id")
    UserInfo userInfo;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    List<DailyHistory> dailyHistoryList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    List<MealPlanner> mealPlannerList = new ArrayList<>();
}
