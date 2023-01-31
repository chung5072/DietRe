package com.dietre.api.controller;

import com.dietre.api.request.UserInfoRegisterReq;
import com.dietre.api.request.UserInfoUpdateReq;
import com.dietre.api.response.RecommendedNutrientRes;
import com.dietre.api.response.UserInfoRes;
import com.dietre.api.data.RecommendedNutrientData;
import com.dietre.api.service.UserService;
import com.dietre.common.model.response.BaseRes;
import com.dietre.common.type.ActivityLevel;
import com.dietre.common.type.Gender;
import com.dietre.common.type.UserDesiredStatus;
import com.dietre.common.util.ActivityLevelMapper;
import com.dietre.common.util.NutritionRecommender;
import com.dietre.config.auth.PrincipalDetails;
import com.dietre.db.entity.User;
import com.dietre.db.entity.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping
    public ResponseEntity<BaseRes> delete(@AuthenticationPrincipal
                                                      PrincipalDetails authentication) {
        Long userId = authentication.getUser().getId();
        userService.deleteUser(userId);
        return ResponseEntity.status(200).body(BaseRes.of(200, "Success"));
    }

    @PostMapping("/info")
    public ResponseEntity<RecommendedNutrientRes> registerUserInfo(@AuthenticationPrincipal
                                                                               PrincipalDetails authentication,
                                                                   @Validated
                                                                   @RequestBody
                                                                           UserInfoRegisterReq req,
                                                                   BindingResult bindingResult) {
        Long userId = authentication.getUser().getId();

        User user = userService.buildUser(userId, req);
        UserInfo userInfo = user.getUserInfo();
        RecommendedNutrientData data = NutritionRecommender.buildRecommendation(userInfo);
        userService.registerUser(user);

        return ResponseEntity.status(200).body(RecommendedNutrientRes.of(200, "Success", data));

    }

    @PatchMapping("/info")
    public ResponseEntity<RecommendedNutrientRes> updateUserInfo(@AuthenticationPrincipal
                                                                             PrincipalDetails authentication,
                                                                 @Validated
                                                                 @RequestBody UserInfoUpdateReq req,
                                                                 BindingResult bindingResult) {
        Long userId = authentication.getUser().getId();

        UserInfo userInfo = userService.rebuildUserInfo(userId, req);
        RecommendedNutrientData data = NutritionRecommender.buildRecommendation(userInfo);
        userInfo.setRecommendations(data);
        userService.registerUserInfo(userInfo);

        /* TODO: 실제로 권장 칼로리량 계산하는 부분 있어야됨 */
        return ResponseEntity.status(200).body(RecommendedNutrientRes.of(200, "Success", data));
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoRes> getUserInfo(@AuthenticationPrincipal
                                                               PrincipalDetails authentication) {
        Long userId = authentication.getUser().getId();

        User user = userService.getUserById(userId);

        return ResponseEntity.status(200).body(UserInfoRes.of(200, "Success", user));
    }

    @GetMapping("/info/nut")
    public ResponseEntity<RecommendedNutrientRes> getRecommendedNutrients(@AuthenticationPrincipal
                                                                                      PrincipalDetails authentication,
                                                                          UserDesiredStatus status) {
        Long userId = authentication.getUser().getId();

        UserInfo userInfo = userService.getUserInfo(userId);
        RecommendedNutrientData data = NutritionRecommender.buildRecommendation(userInfo, status);

        return ResponseEntity.status(200).body(RecommendedNutrientRes.of(200, "Success", data));
    }

    @GetMapping("/info/nutrient")
    public ResponseEntity<RecommendedNutrientRes> getRecommendedNutrients(@AuthenticationPrincipal
                                                                          PrincipalDetails authentication,
                                                                          UserDesiredStatus status,
                                                                          Gender gender,
                                                                          Integer height,
                                                                          Integer weight,
                                                                          Integer activityLevel,
                                                                          Integer age) {
        UserInfo userInfo = new UserInfo();
        ActivityLevel level = ActivityLevelMapper.mapIntegerToActivityLevel(activityLevel);
        userInfo.buildBasicUserInfo(status, gender, height, weight, level, age);

        RecommendedNutrientData data = NutritionRecommender.buildRecommendation(userInfo);

        return ResponseEntity.status(200).body(RecommendedNutrientRes.of(200, "Success", data));

    }


}
