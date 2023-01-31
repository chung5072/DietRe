package com.ssafy.dietre.ui.home;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.ssafy.dietre.R;
import com.ssafy.dietre.api.data.FoodDto;
import com.ssafy.dietre.api.response.MealRecommendRes;
import com.ssafy.dietre.databinding.FragmentRecofoodlistBinding;

import java.util.ArrayList;

public class RecolistFragment extends DialogFragment {

    private FragmentRecofoodlistBinding binding;
    RecyclerView recyclerView;
    
    HomeViewModel homeViewModel;

    MealRecommendRes recoInfo;
    ArrayList<FoodDto> recoList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // view model 연결
        homeViewModel =
                new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        recoInfo = homeViewModel.getRecoInfo().getValue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        // Inflate the layout for this fragment
        binding = FragmentRecofoodlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.layoutRecolist;

        // recycleview에 하나씩 띄우기 위한 리스트
        recoList = new ArrayList<>(5);
        if (recoInfo.getSingle()) {
            recoList.add(recoInfo.getMainDish());
        } else {
            recoList.add(recoInfo.getMainDish());
            recoList.add(recoInfo.getSoup());
            recoList.add(recoInfo.getSide1());
            recoList.add(recoInfo.getSide2());
            recoList.add(recoInfo.getKimchi());
        }

        RecolistAdapter adapter = new RecolistAdapter(recoList);
        // 레이아웃 설정
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1);

        // 레아아웃 적용
        recyclerView.setLayoutManager(manager);

        // 어댑터 적용
        recyclerView.setAdapter(adapter);

        return root;
    }

    public static String RecolistTag = "RecolistFragment";

    @Override
    public void onResume() {
        super.onResume();

        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int w = (int) (metrics.widthPixels);
        int h = (int) (metrics.heightPixels * 0.6);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = w;
        params.height = h;
        window.setAttributes(params);
    }
}

