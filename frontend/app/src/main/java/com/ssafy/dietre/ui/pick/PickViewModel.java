package com.ssafy.dietre.ui.pick;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.ssafy.dietre.common.type.MealTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PickViewModel extends ViewModel {

    private MutableLiveData<ArrayList<FoodItem>> tmpList; // 입력된 사진으로 불러온 임시 음식 리스트
    private MutableLiveData<ArrayList<FoodItem>> confirmList; // 사용자가 눌러서 지정한 확정 음식 리스트
    private MutableLiveData<ArrayList<Long>> check; // 확정 여부 확인하는 리스트(true: 확정(confirm)에 존재, false: 아직 확정 X)
    private MutableLiveData<String> pick; // 식단 날짜
    private MutableLiveData<MealTime> type; // 식단 종류(아점저)
    private MutableLiveData<ArrayList<FoodItem>> foodList; // 최종 확정된 리스트
    private RecyclerView recyclerView;

    public PickViewModel() {
        tmpList = new MutableLiveData<>();
        confirmList = new MutableLiveData<>();
        check = new MutableLiveData<>();
        pick = new MutableLiveData<>();
        type = new MutableLiveData<>();
    }

    // 저장하면 foodlist 비워줌
    public void clearFoodList() {
        if(foodList != null) {
            foodList.getValue().clear();
            tmpList.getValue().clear();
            confirmList.getValue().clear();
            check.getValue().clear();
        }
    }


    // 현재 foodList 불러오기(식단입력에서 보여줄 화면) -> 기본은 tmp, 확정된 것만 confirm food로 바꿔줌
    public LiveData<ArrayList<FoodItem>> getFoodList() {
        foodList = tmpList;
        System.out.println("tmpList 길이: "+tmpList.getValue().size());
        if(tmpList.getValue() == null) {
            return null;
        }
        Log.e("FoodList - check: ", String.valueOf(check.getValue().size()));
        for(int i=0; i<check.getValue().size(); i++) {
            if(check.getValue().get(i) != -1L) { // -1 아니면 id 받아왔다는 의미
                foodList.getValue().get(i).setName(confirmList.getValue().get(i).getName());
                foodList.getValue().get(i).setInfo(confirmList.getValue().get(i).getInfo());
            } // false면 그냥 tmplist 계속 보여주면 됨
        }
        return foodList;
    }

    // 처음 리스트 세팅하기
    public void setFoodList(ArrayList<String> foodList) {
        Log.e("food list 세팅", String.valueOf(foodList.size()));
        ArrayList<FoodItem> listFood = new ArrayList<>();
        ArrayList<FoodItem> listConfirm = new ArrayList<>();
        ArrayList<Long> listCheck = new ArrayList<>();

        for(String name: foodList) {
            System.out.println(name);
            System.out.println(tmpList.getValue());
            listFood.add(new FoodItem(-1L, name, ""));
            listConfirm.add(new FoodItem(-1L, name, ""));
            listCheck.add(-1L);
        }
        tmpList.setValue(listFood);
        confirmList.setValue(listConfirm);
        check.setValue(listCheck);
    }

    // 음식 추가
    public void addFood(FoodItem food, Long position) {
        ArrayList<FoodItem> listTmp = new ArrayList<>();
        ArrayList<FoodItem> listConfirm = new ArrayList<>();
        ArrayList<Long> listCheck = new ArrayList<>();

        if(check.getValue() != null) {
            listTmp = tmpList.getValue();
            System.out.println(listTmp);
            listConfirm = confirmList.getValue();
            System.out.println(listConfirm);
            listCheck = check.getValue();
            System.out.println(listCheck);
        }

        if(check.getValue() == null) {
            tmpList.setValue(new ArrayList<>());
            confirmList.setValue(new ArrayList<>());
            check.setValue(new ArrayList<>());
        }

        int no = food.getId().intValue();
        if(position == -1L) { // 일반 음식추가
            Log.e("일반 음식 추가", String.valueOf(no));
            check.getValue().add(food.getId());
            tmpList.getValue().add(food);
            if(confirmList.getValue() != null) {
                listConfirm = confirmList.getValue();
            }
            listConfirm.add(food);
            confirmList.setValue(listConfirm);
            Log.e("추가 완료", String.valueOf(tmpList.getValue().size()));
        } else { // 음식 확정
            Log.e("음식 확정 추가", String.valueOf(position.intValue()));
            listCheck.set(position.intValue(), food.getId());
            listConfirm.set(position.intValue(), food);
            check.setValue(listCheck);
            confirmList.setValue(listConfirm);
        }
    }

    // 음식 삭제
    public void removeFood(int position) {
        System.out.println(position);
        tmpList.getValue().remove(position);
        confirmList.getValue().remove(position);
        check.getValue().remove(position);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    // FoodId 리스트
    public List<Long> getFoodIdList() {
        return check.getValue();
    }

    public void setPick(String time) {
        String date = time.replace(".", "-");
        pick.setValue("20"+date);
    }

    public String getPick() {
        return pick.getValue();
    }

    public void setType(MealTime meal) {
        type.setValue(meal);
    }

    public MealTime getType() {
        return type.getValue();
    }

    // 유효성 검사
    public boolean checkAllData() {
        if(check.getValue().contains(-1L)) { // 아직 확정 안된 데이터 있으면 X
            Log.e("리스트 확정 남음", "check");
            return false;
        } else if(pick.getValue()==null || type.getValue()==null) { // 시간, 종류 확정 안되면 X
            Log.e("날짜, 타입 확정 남음", "check");
            return false;
        } else { // 모든 데이터 입력 완료되면 통과
            return true;
        }
    }

    public PickViewModel getViewModel() {
        return this;
    }
}