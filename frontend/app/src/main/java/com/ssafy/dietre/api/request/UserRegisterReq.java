package com.ssafy.dietre.api.request;

import com.ssafy.dietre.common.type.Gender;
import com.ssafy.dietre.common.type.UserDesiredStatus;

public class UserRegisterReq {
    Integer age;
    Gender gender;
    Integer height;
    Integer weight;
    UserDesiredStatus status;
    Integer activityLevel;
    Integer recommendedCalorie;
    Integer recommendedCarbohydrate;
    Integer recommendedProtein;
    Integer recommendedFat;
    Boolean countBreakfast;
    Boolean countWeekend;

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Boolean getCountBreakfast() {
        return countBreakfast;
    }

    public void setCountBreakfast(Boolean countBreakfast) {
        this.countBreakfast = countBreakfast;
    }

    public Boolean getCountWeekend() {
        return countWeekend;
    }

    public void setCountWeekend(Boolean countWeekend) {
        this.countWeekend = countWeekend;
    }

    public UserRegisterReq() {
    }

    public UserRegisterReq(Integer age, Gender gender, Integer height, Integer weight, UserDesiredStatus status, Integer activityLevel, Boolean countBreakfast, Boolean countWeekend) {
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.status = status;
        this.activityLevel = activityLevel;
        this.countBreakfast = countBreakfast;
        this.countWeekend = countWeekend;
    }

    public UserRegisterReq(Integer age, Gender gender, Integer height, Integer weight, UserDesiredStatus status, Integer activityLevel, Integer recommendedCalorie, Boolean countBreakfast, Boolean countWeekend) {
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.status = status;
        this.activityLevel = activityLevel;
        this.recommendedCalorie = recommendedCalorie;
        this.countBreakfast = countBreakfast;
        this.countWeekend = countWeekend;
    }

    public UserRegisterReq(Integer age, Gender gender, Integer height, Integer weight, UserDesiredStatus status, Integer activityLevel, Integer recommendedCalorie, Integer recommendedCarbohydrate, Integer recommendedProtein, Integer recommendedFat, Boolean countBreakfast, Boolean countWeekend) {
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.status = status;
        this.activityLevel = activityLevel;
        this.recommendedCalorie = recommendedCalorie;
        this.recommendedCarbohydrate = recommendedCarbohydrate;
        this.recommendedProtein = recommendedProtein;
        this.recommendedFat = recommendedFat;
        this.countBreakfast = countBreakfast;
        this.countWeekend = countWeekend;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public UserDesiredStatus getStatus() {
        return status;
    }

    public void setStatus(UserDesiredStatus status) {
        this.status = status;
    }

    public Integer getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(Integer activityLevel) {
        this.activityLevel = activityLevel;
    }

    public Integer getRecommendedCalorie() {
        return recommendedCalorie;
    }

    public void setRecommendedCalorie(Integer recommendedCalorie) {
        this.recommendedCalorie = recommendedCalorie;
    }

    public Integer getRecommendedCarbohydrate() {
        return recommendedCarbohydrate;
    }

    public void setRecommendedCarbohydrate(Integer recommendedCarbohydrate) {
        this.recommendedCarbohydrate = recommendedCarbohydrate;
    }

    public Integer getRecommendedProtein() {
        return recommendedProtein;
    }

    public void setRecommendedProtein(Integer recommendedProtein) {
        this.recommendedProtein = recommendedProtein;
    }

    public Integer getRecommendedFat() {
        return recommendedFat;
    }

    public void setRecommendedFat(Integer recommendedFat) {
        this.recommendedFat = recommendedFat;
    }
}
