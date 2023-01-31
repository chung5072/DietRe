package com.ssafy.dietre.ui.survey;

import com.ssafy.dietre.common.type.Gender;
import com.ssafy.dietre.common.type.UserDesiredStatus;

public class PhysicInfo {
    Integer age; // 나이
    Gender gender; // 성별
    Integer height; // 키
    Integer weight; // 몸무게
    UserDesiredStatus status; // 목표
    Integer activityLevel; // 활동 레벨
    Integer recommendedCalorie;
    Integer recommendedCarbohydrate;
    Integer recommendedProtein;
    Integer recommendedFat;
    Boolean countWeekend; // 주말 포함 여부
    Boolean countBreakfast; // 2끼, 3끼

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

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
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

    public Boolean getCountWeekend() {
        return countWeekend;
    }

    public void setCountWeekend(Boolean countWeekend) {
        this.countWeekend = countWeekend;
    }

    public Boolean getCountBreakfast() {
        return countBreakfast;
    }

    public void setCountBreakfast(Boolean countBreakfast) {
        this.countBreakfast = countBreakfast;
    }
}
