package com.ssafy.dietre.api.data;

public class FoodData {

    private Long id;

    private Double calorie;

    private Double carbohydrate;

    private Double protein;

    private Double fat;

    private String name;

    public Long getId() {
        return id;
    }

    public Double getCalorie() {
        return calorie;
    }

    public Double getCarbohydrate() {
        return carbohydrate;
    }

    public Double getProtein() {
        return protein;
    }

    public Double getFat() {
        return fat;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCalorie(Double calorie) {
        this.calorie = calorie;
    }

    public void setCarbohydrate(Double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public void setName(String name) {
        this.name = name;
    }
}
