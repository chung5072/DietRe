package com.dietre.api.controller;

import com.dietre.api.data.RecommendedNutrientData;
import com.dietre.api.response.MealRecommendRes;
import com.dietre.api.service.UserService;
import com.dietre.common.type.MealTime;
import com.dietre.common.type.MealType;
import com.dietre.common.type.UserDesiredStatus;
import com.dietre.common.util.DtoBuilder;
import com.dietre.common.util.MealRecommender;
import com.dietre.common.util.NutritionRecommender;
import com.dietre.config.auth.PrincipalDetails;
import com.dietre.db.entity.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;


@RestController
@RequestMapping("/recommendation")
@AllArgsConstructor
public class RecommendationController {

    private final DtoBuilder dtoBuilder;
    private final UserService userService;
    private final MealRecommender mealRecommender;


    @GetMapping("/day/{period}/{goal}/{type}")
    public ResponseEntity<MealRecommendRes> recommend(@AuthenticationPrincipal
                                                              PrincipalDetails authentication,
                                                      @PathVariable MealTime period,
                                                      @PathVariable UserDesiredStatus goal,
                                                      @PathVariable MealType type) {

        Long userId = authentication.getUser().getId();

        UserInfo userInfo = userService.getUserInfo(userId);

        RecommendedNutrientData data = NutritionRecommender.buildRecommendation(userInfo, goal);
        boolean count_breakfast = userInfo.getCountBreakfast();

        Integer recommendedCalorie = (int)(data.getRecommendedCalorie() * period.getRatio(userInfo));
        Integer recommendedCarbohydrate = (int)(data.getRecommendedCarbohydrate() * period.getRatio(userInfo));
        Integer recommendedProtein = (int)(data.getRecommendedProtein() * period.getRatio(userInfo));
        Integer recommendedFat = (int)(data.getRecommendedFat() * period.getRatio(userInfo));

//        if(count_breakfast) {
//            //아점저
//            recommendedCalorie = data.getRecommendedCalorie() / 3;
//            recommendedCarbohydrate = data.getRecommendedCarbohydrate() / 3;
//            recommendedProtein = data.getRecommendedProtein() /3;
//            recommendedFat = data.getRecommendedFat() /3;
//
//        }else{
//            //점저
//            recommendedCalorie = data.getRecommendedCalorie() / 2;
//            recommendedCarbohydrate = data.getRecommendedCarbohydrate() / 2;
//            recommendedProtein = data.getRecommendedProtein() /2;
//            recommendedFat = data.getRecommendedFat() /2;
//        }


        Map<String, Object> foodMap = mealRecommender.mealRecommend2(recommendedCalorie, recommendedCarbohydrate, recommendedProtein, recommendedFat, type);

        return ResponseEntity.status(200).body(MealRecommendRes.of(200, "Success", foodMap));
    }


}
