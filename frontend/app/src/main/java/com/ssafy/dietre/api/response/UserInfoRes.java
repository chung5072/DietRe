package com.ssafy.dietre.api.response;

import com.ssafy.dietre.common.response.BaseRes;
import com.ssafy.dietre.common.type.Gender;
import com.ssafy.dietre.common.type.UserDesiredStatus;

public class UserInfoRes extends BaseRes {
    String name;

    String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    Gender gender;

    Integer height;

    Integer weight;

    Integer age;

    Integer recommendedCalorie;

    Integer recommendedCarbohydrate;

    Integer recommendedProtein;

    Integer recommendedFat;

    Integer activityLevel;

    UserDesiredStatus status;


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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public Integer getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(Integer activityLevel) {
        this.activityLevel = activityLevel;
    }

    public UserDesiredStatus getStatus() {
        return status;
    }

    public void setStatus(UserDesiredStatus status) {
        this.status = status;
    }
}
