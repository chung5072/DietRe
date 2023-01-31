package com.ssafy.dietre.api;

import com.ssafy.dietre.api.data.MealData;
import com.ssafy.dietre.api.request.UserLoginReq;
import com.ssafy.dietre.api.request.UserRegisterReq;
import com.ssafy.dietre.api.response.DailyMealRes;
import com.ssafy.dietre.api.response.DetectionRes;
import com.ssafy.dietre.api.response.FoodSearchRes;
import com.ssafy.dietre.api.response.LoginRes;
import com.ssafy.dietre.api.response.MealAnalysisRes;
import com.ssafy.dietre.api.response.MealRecommendRes;
import com.ssafy.dietre.api.response.MealRegisterRes;
import com.ssafy.dietre.api.response.MealSearchRes;
import com.ssafy.dietre.api.response.MonthlyMealRecordRes;
import com.ssafy.dietre.api.response.RecommendedNutrientRes;
import com.ssafy.dietre.api.response.SpecificMealInfoRes;
import com.ssafy.dietre.api.response.UserInfoRes;
import com.ssafy.dietre.api.response.WeeklyAnalysisRes;
import com.ssafy.dietre.common.response.BaseRes;
import com.ssafy.dietre.common.type.Gender;
import com.ssafy.dietre.common.type.MealTime;
import com.ssafy.dietre.common.type.MealType;
import com.ssafy.dietre.common.type.UserDesiredStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiCallService {
    @GET("api/test/test")
    Call<BaseRes> testCall();

    @POST("api/login")
    Call<LoginRes> login(@Body UserLoginReq req);

    @DELETE("api/user")
    Call<BaseRes> deleteUser();

    @POST("api/user/info")
    Call<RecommendedNutrientRes> registerUserInfo(@Body UserRegisterReq req);

    @PATCH("api/user/info")
    Call<RecommendedNutrientRes> updateUserInfo(@Body UserRegisterReq req);

    @GET("api/user/info")
    Call<UserInfoRes> getUserInfo();

    @GET("api/user/info/nut?")
    Call<RecommendedNutrientRes> getRecommendedNutrients(@Query("status") UserDesiredStatus status);

    @GET("api/meal-planner/{date}/{mealTime}")
    Call<SpecificMealInfoRes> searchMealPlanner(@Path("date") String date, @Path("mealTime") MealTime mealTime);

    @POST("api/meal-planner")
    Call<MealRegisterRes> registerMealPlanner(@Body MealData mealData);

    @PATCH("api/meal-planner/{mealPlannerId}")
    Call<MealRegisterRes> updateMealPlanner(@Path("mealPlannerId") Long mealPlannerId, @Body MealData mealData);

    @GET("api/meal-planner/analysis/{date}/{mealTime}")
    Call<MealAnalysisRes> getAnalysis(@Path("date") String date, @Path("mealTime") MealTime mealTime);

    @GET("api/meal-planner/analysis/week")
    Call<WeeklyAnalysisRes> getWeeklyAnalysis();

    @GET("api/recommendation/day/{mealTime}/{goal}/{type}")
    Call<MealRecommendRes> getRecommendation(@Path("mealTime") MealTime mealTime,
                                             @Path("goal") UserDesiredStatus goal,
                                             @Path("type") MealType mealType);

    @GET("api/food/list?")
    Call<FoodSearchRes> getFoods(@Query("foodName") String foodName);

    @GET("api/user/info/nutrient?")
    Call<RecommendedNutrientRes> getRecommendedNutrients(@Query("status") UserDesiredStatus status,
                                                         @Query("gender") Gender gender,
                                                         @Query("height") Integer height,
                                                         @Query("weight") Integer weight,
                                                         @Query("activityLevel") Integer activityLevel,
                                                         @Query("age") Integer age);

    @GET("api/meal-planner/day/{date}")
    Call<DailyMealRes> getDailyMealNut(@Path("date") String date);

    @GET("api/meal-planner/month/{date}")
    Call<List<MonthlyMealRecordRes>> getMonthlyMealRecords(@Path("date") LocalDate date);

    @Multipart
    @POST("internal/detection")
    Call<DetectionRes> getDetectedRecords(@Part MultipartBody.Part file);
}
