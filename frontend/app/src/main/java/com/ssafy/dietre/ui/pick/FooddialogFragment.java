package com.ssafy.dietre.ui.pick;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.textfield.TextInputEditText;
import com.ssafy.dietre.api.ApiCallService;
import com.ssafy.dietre.api.response.FoodRes;
import com.ssafy.dietre.api.response.FoodSearchRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.databinding.FragmentFooddialogBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FooddialogFragment extends DialogFragment {

    private FragmentFooddialogBinding binding;
    private TextInputEditText search; // 검색창
    private ArrayList<FoodItem> searchList = new ArrayList<>(); // 검색결과 리스트
    private static final String name = "FOOD_NAME";
    private RecyclerView foodList;
    private FoodItemAdapter adapter;
    public PickViewModel pickViewModel;

    public FooddialogFragment() {  }

    public static FooddialogFragment getInstance(@Nullable String food, Long position) {
        FooddialogFragment dialog = new FooddialogFragment();
        Bundle args = new Bundle();
        args.putString(name, food);
        args.putLong("position", position);
        if(food != null) {
            Log.d("food 정보 전달", food);
            dialog.setArguments(args);
        } else {
            Log.d("food 정보 X", "음식 추가");
        }
        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFooddialogBinding.inflate(inflater, container, false);
        pickViewModel = new ViewModelProvider(requireActivity()).get(PickViewModel.class);

        View v = binding.getRoot();
        search = (TextInputEditText) binding.inputFood;
        foodList = (RecyclerView) binding.layoutList; // 검색한 음식 보여줄 곳
        if(getArguments() != null) {
            String food = getArguments().getString(name).split(" \\|")[0];
            Long position = getArguments().getLong("position");
            Log.d("food 이름은", food);
            search.setEnabled(false);

            searchFood(food, position);
        } else {
            search.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_ENTER) {
                        searchFood(search.getText().toString(), -1L);
                    } else if(keyCode == KeyEvent.KEYCODE_BACK) {
                        getDialog().dismiss();
                    }
                    return true;
                }
            });
        }

        return v;
    }

    private void searchFood(String food, Long pos) {
        search.setText(food);
        if(food.length() <= 0) {
            String error = "검색할 음식을 입력하세요.";
            search.setError(error);
            Log.e("음식 추가 실패", food);
        } else {
            search.setError(null);

            // 리스트 불러오고 arraylist에 넣어서 layout에 세팅하기
            LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            foodList.setLayoutManager(lm);
            foodList.setHasFixedSize(true);

            ApiCallService service = ApiCallServiceProvider.provide();
            Call<FoodSearchRes> call = service.getFoods(food);
            call.enqueue(new Callback<FoodSearchRes>() {
                @Override
                public void onResponse(Call<FoodSearchRes> call, Response<FoodSearchRes> response) {
                    FoodSearchRes res = response.body();
                    List<FoodRes> list = res.getData();
                    searchList.clear();
                    for (FoodRes foodRes : list) {
                        String name = foodRes.getName() + " | " + foodRes.getCalorie() + "kcal";
                        String info = "탄수화물(g): "+foodRes.getCarbohydrate()+
                                ", 단백질(g): "+foodRes.getProtein()+", 지방(g): "+foodRes.getFat();
                        FoodItem item = new FoodItem(foodRes.getFoodId(), name, info);
                        searchList.add(item);
                    }
                    adapter = new FoodItemAdapter(PickFragment.getInstance(), searchList, false);
                    adapter.setItemClickListener(new FoodItemAdapter.itemClickListener() {
                        @Override
                        public void addFood(FoodItem food, Long position) {
                            //Log.e("addFood 이전", String.valueOf(pickViewModel.getFoodList().getValue().size()));
                            pickViewModel.addFood(food, pos);
                            dismiss();
                            PickFragment.getInstance().listRefresh(pickViewModel);
                        }
                    });
                    foodList.setAdapter(adapter);

                    InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(search.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    search.setText(null);
                    search.clearFocus();
                    search.setFocusable(false);
                    search.setFocusableInTouchMode(true);
                }

                @Override
                public void onFailure(Call<FoodSearchRes> call, Throwable t) {

                }
            });

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int w = (int) (metrics.widthPixels * 0.9);
        int h = (int) (metrics.heightPixels * 0.8);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = w;
        params.height = h;
        window.setAttributes(params);
    }
}
