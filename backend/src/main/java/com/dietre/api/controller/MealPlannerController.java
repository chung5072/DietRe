package com.dietre.api.controller;

import com.dietre.api.response.*;
import com.dietre.api.service.MealPlannerService;
import com.dietre.config.auth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import com.dietre.api.data.DailyStatistics;
import com.dietre.api.data.FoodData;
import com.dietre.api.data.MealData;
import com.dietre.api.data.WeeklyStatistics;
import com.dietre.api.service.DailyHistoryService;
import com.dietre.api.service.UserService;
import com.dietre.common.type.MealTime;
import com.dietre.common.util.DtoBuilder;
import com.dietre.common.util.DtoConverter;
import com.dietre.db.entity.MealPlanner;
import com.dietre.db.entity.User;
import com.dietre.db.entity.UserInfo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meal-planner")
@RequiredArgsConstructor
public class MealPlannerController {

    private final MealPlannerService mealPlannerService;
    private final UserService userService;
    private final DailyHistoryService dailyHistoryService;
    private final DtoConverter dtoConverter;
    private final DtoBuilder dtoBuilder;

    @GetMapping("/month/{date}")
    public ResponseEntity getMonthlyMealPlanner(@AuthenticationPrincipal PrincipalDetails authentication,
                                                @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Long userId = authentication.getUser().getId();
        List<MonthlyMealRecordRes> monthlyMealPlanner = mealPlannerService.getMonthlyMealPlanner(userId, date);
        return ResponseEntity.ok(monthlyMealPlanner);
    }

    @GetMapping("/day/{date}")
    public ResponseEntity getDailyMealPlanner(@AuthenticationPrincipal PrincipalDetails authentication,
                                              @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        Long userId = authentication.getUser().getId();
        return ResponseEntity.ok(mealPlannerService.getDailyMealPlanner(userId, date));
    }

    @PostMapping
    public ResponseEntity<MealRegisterRes> register(@AuthenticationPrincipal
                                                    PrincipalDetails authentication,
                                                    @RequestBody MealData req) {

        Long userId = authentication.getUser().getId();

        // 한끼 정보 등록 및 하루 기록 갱신
        User user = userService.getUserById(userId);
        MealPlanner mealPlanner = mealPlannerService.register(user, req);
        Long mealPlannerId = mealPlanner.getId();
        dailyHistoryService.updateHistory(user, mealPlanner);

        return ResponseEntity.status(200)
                .body(MealRegisterRes.of(200, "Success", mealPlannerId));
    }

    @PatchMapping("/{mealPlannerId}")
    public ResponseEntity<MealRegisterRes> update(@AuthenticationPrincipal
                                                  PrincipalDetails authentication,
                                                  @PathVariable Long mealPlannerId,
                                                  @RequestBody MealData req) {
        Long userId = authentication.getUser().getId();

        User user = userService.getUserById(userId);
        MealPlanner mealPlanner = mealPlannerService.update(mealPlannerId, req);
        dailyHistoryService.updateHistory(user, mealPlanner);

        return ResponseEntity.status(200)
                .body(MealRegisterRes.of(200, "Success", mealPlannerId));
    }

//    @GetMapping("/{date}/{mealTime}")
//    public ResponseEntity<MealSearchRes> search(@AuthenticationPrincipal
//                                                PrincipalDetails authentication,
//                                                @PathVariable
//                                                @DateTimeFormat(pattern = "yyyy-MM-dd")
//                                                LocalDate date,
//                                                @PathVariable MealTime mealTime) {
//
//        Long userId = authentication.getUser().getId();
//
//        MealPlanner mealPlanner = mealPlannerService.search(userId, date, mealTime);
//        List<FoodData> foodDataList = dtoConverter.getFoodDataListFromMealPlanner(mealPlanner);
//
//        /* TODO: mealPlanner의 converter를 등록 */
//        return ResponseEntity.status(200).body(MealSearchRes.of(200, "Success", mealPlanner, foodDataList));
//    }

    @GetMapping("/analysis/{date}/{mealTime}")
    public ResponseEntity<MealAnalysisRes> mealAnalysis(@AuthenticationPrincipal
                                                        PrincipalDetails
                                                                authentication,
                                                        @PathVariable
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                        LocalDate date,
                                                        @PathVariable MealTime mealTime) {
        Long userId = authentication.getUser().getId();

        UserInfo userInfo = userService.getUserInfo(userId);
        MealPlanner mealPlanner = mealPlannerService.search(userId, date, mealTime);
        MealAnalysisRes mealAnalysisRes =
                MealAnalysisRes.of(200, "Success", userInfo, mealPlanner);

        return ResponseEntity.status(200).body(mealAnalysisRes);
    }

    @GetMapping("/{date}/{mealTime}")
    public ResponseEntity<SpecificMealInfoRes> getSpecificMealInfo(@AuthenticationPrincipal
                                                    PrincipalDetails authentication,
                                                    @PathVariable
                                                    @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                    LocalDate date,
                                                    @PathVariable MealTime mealTime) {

        Long userId = authentication.getUser().getId();

        /* TODO: mealPlanner의 converter를 등록 */
        return ResponseEntity.ok(mealPlannerService.getSpecificMealInfo(userId, date, mealTime));
    }


    @GetMapping("/analysis/week")
    public ResponseEntity<WeeklyAnalysisRes> weekAnalysis(@AuthenticationPrincipal
                                                          PrincipalDetails authentication) {

        Long userId = authentication.getUser().getId();

        UserInfo userInfo = userService.getUserInfo(userId);
        List<DailyStatistics> dailyStatisticsList = dtoBuilder.buildDailyStatisticsList(userInfo);
        WeeklyStatistics weeklyStatistics = dtoBuilder.buildWeeklyStatistics(userInfo, dailyStatisticsList);

        WeeklyAnalysisRes weeklyAnalysisRes = WeeklyAnalysisRes
                .of(200, "Success", dailyStatisticsList, weeklyStatistics);

        return ResponseEntity.status(200).body(weeklyAnalysisRes);
    }

}
