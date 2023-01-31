package com.dietre.common.util;

import com.dietre.api.data.FoodStatistics;
import com.dietre.api.service.FoodService;
import com.dietre.common.type.FoodCategory;
import com.dietre.common.type.MealType;
import com.dietre.db.entity.Food;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class MealRecommender {

    private final FoodService foodService;


//    //식단 추천 알고리즘
//    public Map<String, Object> mealRecommend(Integer recommendedCalorie, Integer recommendedCarbohydrate,
//                             Integer recommendedProtein, Integer recommendedFat, MealType type) {
//
//        Double currentCalorie;
//        Double currentCarbohydrate;
//        Double currentProtein;
//        Double currentFat;
//        LocalDate today = LocalDate.now();
//
//        Map<String, Object> foodMap = new HashMap<>();
//
//        foodMap.put("date", today);
//        //DB 내 Food 전체 리스트
//        List<Food> food = foodService.getAllFoods().stream().filter(f -> f.getId() < 401).collect(Collectors.toList());
//        //단품 전체 리스트
//        List<Food> singleList = food.stream().filter(f -> f.getType() == FoodCategory.main).filter(f -> f.getIsSingle()).collect(Collectors.toList());
//        //밥 전체 리스트
//        List<Food> babList = food.stream().filter(f -> f.getType() == FoodCategory.main).filter(f-> !f.getIsSingle()).collect(Collectors.toList());
//        //메인반찬 전체 리스트
//        List<Food> side1List = food.stream().filter(f -> f.getType() == FoodCategory.side1).collect(Collectors.toList());
//        //서브반찬 전체 리스트
//        List<Food> side2List = food.stream().filter(f -> f.getType() == FoodCategory.side2).collect(Collectors.toList());
//        //국 전체 리스트
//        List<Food> soupList = food.stream().filter(f -> f.getType() == FoodCategory.soup).collect(Collectors.toList());
//        //김치 전체 리스트
//        List<Food> kimchiList = food.stream().filter(f -> f.getType() == FoodCategory.kimchi).collect(Collectors.toList());
//
//        //단품일경우
//        if(type.isSingle()){
//
//            foodMap.put("isSingle" , true);
//
//            Food single;
//
//            int i = 1;
//
//            while (true) {
//                //랜덤으로 단품 뽑기
//                single = randompick(singleList);
//
//                //뽑힌 음식 칼탄단지 계산
//                currentCalorie = single.getCalorie();
//                currentCarbohydrate = single.getCarbohydrate();
//                currentProtein = single.getCarbohydrate();
//                currentFat = single.getFat();
//
//                //뽑힌 음식 칼탄단지가 정상 범위 내에 있는지 T/F
//                // 0.8 < 칼로리 < 1.2
//                // 0.5 < 단백질, 탄수화물, 지방 < 2
//                boolean resultCal = compareNut(recommendedCalorie, currentCalorie, 0.8, 1.2);
//                boolean resultCar = compareNut(recommendedCarbohydrate, currentCarbohydrate, 0.5, 2.0);
//                boolean resultPro = compareNut(recommendedProtein, currentProtein, 0.5, 2.0);
//                boolean resultFat = compareNut(recommendedFat, currentFat, 0.5, 2.0);
//
//                //뽑힌 음식 칼로리가 정상 범위에 있다면 BREAK
//                //resultCal && resultCar && resultPro && resultFat
//                if(resultCal) {
//                    break;
//                }else {
//                    i++;
//                    if(i > 5) {
//                        //단품 추천이 안된다면 "오일소스스파게티" 보내기
//                        single = singleList.get(25);
//                        break;
//                    }
//                }
//            }
//
//            foodMap.put("mainDish", single.mapFoodDto(null));
//
//        //한식일경우
//        }else{
//
//            foodMap.put("isSingle" , false);
//
//            //밥, 서브반찬, 김치, 메인반찬, 국
//            Food bab;
//            Food side2;
//            Food kimchi;
//            Food side1;
//            Food soup;
//
//            //밥, 서브반찬, 김치 먼저 뽑기
//            while(true) {
//                //랜덤으로 밥, 서브반찬, 김치 뽑기
//                bab = randompick(babList);
//                side2 = randompick(side2List);
//                kimchi = randompick(kimchiList);
//
//                //뽑힌 음식 칼탄단지 계산
//                //밥+서브반찬+김치
//                currentCalorie = bab.getCalorie() + side2.getCalorie() + kimchi.getCalorie();
//                currentCarbohydrate = bab.getCarbohydrate() + side2.getCarbohydrate() + kimchi.getCarbohydrate();
//                currentProtein = bab.getProtein() + side2.getCarbohydrate() + kimchi.getCarbohydrate();
//                currentFat = bab.getFat() + side2.getFat() + kimchi.getFat();
//
//                //뽑힌 음식 칼탄단지가 정상 범위 내에 있는지 T/F
//                //전체 권장 칼탄단지의 50%를 넘지 않도록 범위 지정
//                boolean resultCal = compareNut(recommendedCalorie, currentCalorie, 0.0, 0.5);
//                boolean resultCar = compareNut(recommendedCarbohydrate, currentCarbohydrate, 0.0, 0.5);
//                boolean resultPro = compareNut(recommendedProtein, currentProtein, 0.0, 0.5);
//                boolean resultFat = compareNut(recommendedFat, currentFat, 0.0, 0.5);
//
//                //뽑힌 음식 칼탄단지가 정상 범위에 있다면 BREAK
//                if(resultCal && resultCar && resultPro && resultFat) {
//                    break;
//                }
//
//            }
//            //메인반찬, 국 뽑기
//            while(true) {
//
//                //랜덤으로 메인반찬, 국 뽑기
//                side1 = randompick(side1List);
//                soup = randompick(soupList);
//
//                //뽑힌 음식 칼탄단지 계산
//                //밥+서브반찬+김치+메인반찬+국
//                currentCalorie = side1.getCalorie() + soup.getCalorie();
//                currentCarbohydrate = side1.getCarbohydrate() + soup.getCarbohydrate();
//                currentProtein = side1.getProtein() + soup.getCarbohydrate();
//                currentFat = side1.getFat() + soup.getFat();
//
//                //뽑힌 음식 칼탄단지가 정상 범위 내에 있는지 T/F
//                // 0.8 < 칼로리 < 1.2
//                // 0.5 < 단백질, 탄수화물, 지방 < 2
//                boolean resultCal = compareNut(recommendedCalorie, currentCalorie, 0.8, 1.2);
//                boolean resultCar = compareNut(recommendedCarbohydrate, currentCarbohydrate, 0.5, 2.0);
//                boolean resultPro = compareNut(recommendedProtein, currentProtein, 0.5, 2.0);
//                boolean resultFat = compareNut(recommendedFat, currentFat, 0.5, 2.0);
//
//                //뽑힌 음식 칼탄단지가 정상 범위에 있다면 BREAK
//                if(resultCal && resultCar && resultPro && resultFat) {
//                    break;
//                }
//
//            }
//            foodMap.put("mainDish", bab.mapFoodDto(null));
//            foodMap.put("side1", side1.mapFoodDto(null));
//            foodMap.put("side2", side2.mapFoodDto(null));
//            foodMap.put("soup", soup.mapFoodDto(null));
//            foodMap.put("kimchi", kimchi.mapFoodDto(null));
//        }
//
//        FoodStatistics foodStatistics = new FoodStatistics();
//        foodStatistics.setWeight(0);
//        foodStatistics.setCalorie(currentCalorie.intValue());
//        foodStatistics.setCarbohydrate(currentCarbohydrate.intValue());
//        foodStatistics.setProtein(currentProtein.intValue());
//        foodStatistics.setFat(currentFat.intValue());
//
//        foodMap.put("foodStatistics", foodStatistics);
//
//        return foodMap;
//    }

    //리스트에서 랜덤으로 하나 뽑기
    public Food randompick(List<Food> foods) {
        int size = foods.size();

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        int i = random.nextInt(size);

        return foods.get(i);
    }
    //뽑힌 음식이 권장의 최소 최대 비율 내에 있는지 확인
    public boolean compareNut(double recommended, double current, double min, double max) {
        if(recommended * min > current) {
            return false;
        }
        if(recommended * max < current) {
            return false;
        }

        return true;
    }




    public Map<String, Object> mealRecommend2(Integer recommendedCalorie, Integer recommendedCarbohydrate,
                                              Integer recommendedProtein, Integer recommendedFat, MealType type) {
        Double currentCalorie;
        Double currentCarbohydrate;
        Double currentProtein;
        Double currentFat;

        LocalDate today = LocalDate.now();

        Map<String, Object> foodMap = new HashMap<>();

        foodMap.put("date", today);
        //DB 내 Food 전체 리스트
        List<Food> food = foodService.getAllFoods().stream().filter(f -> f.getId() < 401).collect(Collectors.toList());
        //단품 전체 리스트
        List<Food> singleList = food.stream().filter(f -> f.getType() == FoodCategory.main).filter(f -> f.getIsSingle()).collect(Collectors.toList());
        //밥 전체 리스트
        List<Food> babList = food.stream().filter(f -> f.getType() == FoodCategory.main).filter(f-> !f.getIsSingle()).collect(Collectors.toList());
        //메인반찬 전체 리스트
        List<Food> side1List = food.stream().filter(f -> f.getType() == FoodCategory.side1).collect(Collectors.toList());
        //서브반찬 전체 리스트
        List<Food> side2List = food.stream().filter(f -> f.getType() == FoodCategory.side2).collect(Collectors.toList());
        //국 전체 리스트
        List<Food> soupList = food.stream().filter(f -> f.getType() == FoodCategory.soup).collect(Collectors.toList());
        //김치 전체 리스트
        List<Food> kimchiList = food.stream().filter(f -> f.getType() == FoodCategory.kimchi).collect(Collectors.toList());

        //단품일경우
        if(type.isSingle()){

            foodMap.put("isSingle" , true);

            Food single;

            int i = 1;

            while (true) {
                //랜덤으로 단품 뽑기
                single = randompick(singleList);

                //뽑힌 음식 칼탄단지 계산
                currentCalorie = single.getCalorie();
                currentCarbohydrate = single.getCarbohydrate();
                currentProtein = single.getCarbohydrate();
                currentFat = single.getFat();

                //뽑힌 음식 칼탄단지가 정상 범위 내에 있는지 T/F
                // 0.8 < 칼로리 < 1.2
                // 0.5 < 단백질, 탄수화물, 지방 < 2
                boolean resultCal = compareNut(recommendedCalorie, currentCalorie, 0.8, 1.2);
                boolean resultCar = compareNut(recommendedCarbohydrate, currentCarbohydrate, 0.5, 2.0);
                boolean resultPro = compareNut(recommendedProtein, currentProtein, 0.5, 2.0);
                boolean resultFat = compareNut(recommendedFat, currentFat, 0.5, 2.0);

                //뽑힌 음식 칼로리가 정상 범위에 있다면 BREAK
                //resultCal && resultCar && resultPro && resultFat
                if(resultCal) {
                    break;
                }else {
                    i++;
                    if(i > 5) {
                        //단품 추천이 안된다면 "오일소스스파게티" 보내기
                        single = singleList.get(25);
                        break;
                    }
                }
            }

            foodMap.put("mainDish", single.mapFoodDto(null));

            //한식일경우
        }else{

            foodMap.put("isSingle" , false);

            //밥, 서브반찬, 김치, 메인반찬, 국
            Food bab = null;
            Food side2 = null;
            Food kimchi = null;
            Food side1 = null;
            Food soup = null;

            Double rate = 1.0;

            side2 = randompick(side2List);
            kimchi = randompick(kimchiList);
            currentCalorie = side2.getCalorie() + kimchi.getCalorie();
            currentCarbohydrate = side2.getCarbohydrate() + kimchi.getCarbohydrate();
            currentProtein = side2.getCarbohydrate() + kimchi.getCarbohydrate();
            currentFat = side2.getFat() + kimchi.getFat();


            while (true) {
                Double maxScore = -1.0;


                Double curCal = null;
                for (int i = 0; i < 500000; i++) {
                    double currentScore = 0.0;
                    Double cal = 0.0;
                    Double car = 0.0;
                    Double protein = 0.0;
                    Double fat = 0.0;
                    bab = randompick(babList);
                    side1 = randompick(side1List);
                    soup = randompick(soupList);
                    cal = currentCalorie + bab.getCalorie() * rate + side1.getCalorie() * rate + soup.getCalorie() * rate;
//                    car = currentCarbohydrate + bab.getCarbohydrate() * rate + side1.getCarbohydrate() * rate  + soup.getCarbohydrate() * rate;
//                    protein = currentProtein + bab.getProtein() * rate + side1.getCalorie() * rate + soup.getCalorie() * rate;
//                    fat = currentFat + bab.getFat() * rate + side1.getCalorie() * rate + soup.getCalorie() * rate;


                    currentScore += ScoreCalculator.calculateEachPart(40.0, cal, recommendedCalorie.doubleValue());
//                    currentScore += ScoreCalculator.calculateEachPart(20.0, car, recommendedCarbohydrate.doubleValue());
//                    currentScore += ScoreCalculator.calculateEachPart(20.0, protein, recommendedProtein.doubleValue());
//                    currentScore += ScoreCalculator.calculateEachPart(20.0, fat, recommendedFat.doubleValue());
                    if (currentScore > maxScore) {
                        foodMap.put("mainDish", bab.mapFoodDto(rate == 1.0 ? null : rate));
                        foodMap.put("side1", side1.mapFoodDto(rate == 1.0 ? null : rate));
                        foodMap.put("side2", side2.mapFoodDto(null));
                        foodMap.put("soup", soup.mapFoodDto(rate == 1.0 ? null : rate));
                        foodMap.put("kimchi", kimchi.mapFoodDto(null));
                        maxScore = currentScore;
                        curCal = cal;
                    }
                }

                if (maxScore < 30.0) {
                    rate -= 0.1;
                } else {
                    break;
                }
            }

        }

        FoodStatistics foodStatistics = new FoodStatistics();
        foodStatistics.setWeight(0);
        foodStatistics.setCalorie(currentCalorie.intValue());
        foodStatistics.setCarbohydrate(currentCarbohydrate.intValue());
        foodStatistics.setProtein(currentProtein.intValue());
        foodStatistics.setFat(currentFat.intValue());

        foodMap.put("foodStatistics", foodStatistics);

        return foodMap;
    }


}