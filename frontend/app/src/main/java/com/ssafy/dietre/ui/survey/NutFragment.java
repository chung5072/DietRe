package com.ssafy.dietre.ui.survey;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;
import com.ssafy.dietre.LoginActivity;
import com.ssafy.dietre.MainActivity;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.GetRecommendedNutrientApiCall;
import com.ssafy.dietre.api.PatchUserInfoApiCall;
import com.ssafy.dietre.api.PostUserInfoApiCall;
import com.ssafy.dietre.api.request.UserRegisterReq;
import com.ssafy.dietre.common.type.UserDesiredStatus;
import com.ssafy.dietre.databinding.FragmentNutBinding;
import com.ssafy.dietre.ui.filter.InputFilterMinMax;

public class NutFragment extends Fragment implements View.OnClickListener {

    private FragmentNutBinding fragmentNutBinding;

    // ViewModel 연결
    PhysicViewModel physicViewModel;
    PhysicInfo physicInfo;

    TextInputLayout kcalInput;
    TextView carbText;
    TextView protText;
    TextView fatText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // ViewModel 연결
        physicViewModel
                = new ViewModelProvider(requireActivity()).get(PhysicViewModel.class);
        physicInfo = new PhysicInfo();

        // Fragment - xml 파일 연결
        fragmentNutBinding = FragmentNutBinding.inflate(inflater, container, false);
        View root = fragmentNutBinding.getRoot();

        // ViewModel의 값을 여기서 setText로 세팅
        // 화면에서 사용할 inputlayout
        kcalInput = (TextInputLayout) fragmentNutBinding.textinputNutKcal;
        kcalInput.getEditText().setFilters(new InputFilter[]{new InputFilterMinMax("1", "10000")});
        kcalInput.getEditText().setText(String.valueOf(physicViewModel.getPhysicInfo().getValue().getRecommendedCalorie()));

        // 화면에서 사용할 TextView
        // 추천 탄수화물
        carbText = (TextView) fragmentNutBinding.textNutCarb;
        carbText.setText(String.valueOf(physicViewModel.getPhysicInfo().getValue().getRecommendedCarbohydrate()));

        // 추천 단백질
        protText = (TextView) fragmentNutBinding.textNutProt;
        protText.setText(String.valueOf(physicViewModel.getPhysicInfo().getValue().getRecommendedProtein()));

        // 추천 지방
        fatText = (TextView) fragmentNutBinding.textNutFat;
        fatText.setText(String.valueOf(physicViewModel.getPhysicInfo().getValue().getRecommendedFat()));

        // 화면에서 사용하는 슬라이더
        // 탄수화물
        // 슬라이더 텍스트
        TextView carbSliderText = (TextView) fragmentNutBinding.slidertextNutCarb;
        // 슬라이더
        Slider carbSlider = (Slider) fragmentNutBinding.sliderNutCarb;
        // 단백질
        // 슬라이더 텍스트
        TextView protSliderText = (TextView) fragmentNutBinding.slidertextNutProt;
        // 슬라이더
        Slider protSlider = (Slider) fragmentNutBinding.sliderNutProt;
        // 지방
        // 슬라이더 텍스트
        TextView fatSliderText = (TextView) fragmentNutBinding.slidertextNutFat;
        // 슬라이더
        Slider fatSlider = (Slider) fragmentNutBinding.sliderNutFat;

        // 슬라이더에 데이터 연결
        // 탄수화물
        // 슬라이더 텍스트
        carbSliderText.setText(String.valueOf((int) carbSlider.getValue()));
        // 단백질
        // 슬라이더 텍스트
        protSliderText.setText(String.valueOf((int) protSlider.getValue()));
        // 지방
        // 슬라이더 텍스트
        fatSliderText.setText(String.valueOf((int) fatSlider.getValue()));

        // 화면에서 사용하는 버튼
        // 이전 설문 페이지로 이동하는 버튼
        Button prevButton = (Button) fragmentNutBinding.btnNutPrev;
        // 다음 설문 페이지로 이동하는 버튼
        Button startButton = (Button) fragmentNutBinding.btnNutStart;

        // 버튼과 클릭 이벤트 연결
        // 이전 페이지
        prevButton.setOnClickListener(this);
        // 메인 컨텐츠로 이동하는 페이지
        startButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                // 목표 칼로리 값
                String goalKcal = kcalInput.getEditText().getText().toString().trim();
                // 칼로리값이 빈값이면 이동하지 못하도록 막음
                if (goalKcal.equals("")) {
                    new MaterialAlertDialogBuilder(view.getContext(), R.style.AlertDialogTheme)
                            .setTitle("모든 값을 입력해주세요")
                            .setMessage("입력하지 않은 값: 칼로리")
                            // 우측을 누르면 다이얼로그가 꺼짐
                            .setPositiveButton("다시 입력하기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
                else {
                    UserRegisterReq req = new UserRegisterReq(
                            physicViewModel.getPhysicInfo().getValue().getAge(),
                            physicViewModel.getPhysicInfo().getValue().getGender(),
                            physicViewModel.getPhysicInfo().getValue().getHeight(),
                            physicViewModel.getPhysicInfo().getValue().getWeight(),
                            physicViewModel.getPhysicInfo().getValue().getStatus(),
                            physicViewModel.getPhysicInfo().getValue().getActivityLevel(),
                            Integer.parseInt(kcalInput.getEditText().getText().toString().trim()),
                            physicViewModel.getPhysicInfo().getValue().getRecommendedCarbohydrate(),
                            physicViewModel.getPhysicInfo().getValue().getRecommendedProtein(),
                            physicViewModel.getPhysicInfo().getValue().getRecommendedFat(),
                            physicViewModel.getPhysicInfo().getValue().getCountBreakfast(),
                            physicViewModel.getPhysicInfo().getValue().getCountWeekend()
                    );

                    // PostApiCall을 통해서 값 넣고 activity 이동
                    PostUserInfoApiCall.call(req, (LoginActivity) getActivity());
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentNutBinding = null;
    }

    /**
     * 클릭 이벤트 함수
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_nut_prev: {
                // 프래그먼트 - 신체 설문 페이지
                Fragment activityFragment = new DetailFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // 로그인 페이지 대체
                fragmentTransaction.replace(R.id.fragment_login, activityFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }
        }
    }
}