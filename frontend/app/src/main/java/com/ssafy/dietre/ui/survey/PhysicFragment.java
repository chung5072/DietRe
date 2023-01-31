package com.ssafy.dietre.ui.survey;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.ssafy.dietre.LoginActivity;
import com.ssafy.dietre.R;
import com.ssafy.dietre.common.provider.MainActivityProvider;
import com.ssafy.dietre.common.type.Gender;
import com.ssafy.dietre.databinding.FragmentPhysicBinding;
import com.ssafy.dietre.ui.filter.InputFilterMinMax;

import java.util.ArrayList;

public class PhysicFragment extends Fragment implements View.OnClickListener {

    private FragmentPhysicBinding fragmentPhysicBinding;

    // 프래그먼트간 데이터 전달을 위한 클래스
    // ViewModel 연결
    PhysicViewModel physicViewModel;
    // 보내야 할 값: 성별, 나이, 키, 몸무게
    PhysicInfo physicInfo;
    // 보내는 위치: DetailFragment

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        physicViewModel = new ViewModelProvider(requireActivity()).get(PhysicViewModel.class);
        physicInfo = new PhysicInfo();

        // Fragment - xml 파일 연결
        fragmentPhysicBinding =
                FragmentPhysicBinding.inflate(inflater, container, false);
        View view = fragmentPhysicBinding.getRoot();

        // 다음 버튼 누르면 키보드 내려가게 - 굳이?
        TextInputLayout ageInput = fragmentPhysicBinding.textinputPhysicAge;
        ageInput.getEditText().setFilters(new InputFilter[]{new InputFilterMinMax("1", "120")});
//        keyboardDown(ageInput);
        TextInputLayout heightInput = fragmentPhysicBinding.textinputPhysicHeight;
        heightInput.getEditText().setFilters(new InputFilter[]{new InputFilterMinMax("1", "260")});
//        keyboardDown(heightInput);
        TextInputLayout weightInput = fragmentPhysicBinding.textinputPhysicWeight;
        weightInput.getEditText().setFilters(new InputFilter[]{new InputFilterMinMax("1", "610")});
//        keyboardDown(weightInput);

        // 화면에서 사용하는 버튼
        // 다음 설문 페이지로 이동하는 버튼
        Button nextButton = (Button) view.findViewById(R.id.btn_physic_next);
        // 성별을 고르는 버튼
        MaterialButtonToggleGroup genderSelectButton = (MaterialButtonToggleGroup) view.findViewById(R.id.btn_physic_gender);
        // 여성 버튼
        Button femaleButton = (Button) view.findViewById(R.id.btn_physic_female);
        // 남성 버튼
        Button maleButton = (Button) view.findViewById(R.id.btn_physic_male);

        // 버튼과 클릭 이벤트 연결
        // 다음 페이지
        nextButton.setOnClickListener(this);
        genderSelectButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.btn_physic_female) {
                        femaleButton.setBackgroundColor(Color.parseColor("#CCE9FF"));
                        maleButton.setBackgroundColor(Color.parseColor("#fafafa"));
                        physicInfo.setGender(Gender.female);
                    } else {
                        femaleButton.setBackgroundColor(Color.parseColor("#fafafa"));
                        maleButton.setBackgroundColor(Color.parseColor("#CCE9FF"));
                        physicInfo.setGender(Gender.male);
                    }
                }
            }
        });

        // 값 입력
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentPhysicBinding = null;
    }

    /**
     * 클릭 이벤트 함수
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_physic_next: {
                // 화면에서 사용하는 edittext
                // 나이
                TextInputLayout ageInput = (TextInputLayout) getActivity().findViewById(R.id.textinput_physic_age);
                String age = ageInput.getEditText().getText().toString().trim();
                if (!age.equals("")) {
                    physicInfo.setAge(Integer.parseInt(age));
                }
                // 키
                TextInputLayout heightInput = (TextInputLayout) getActivity().findViewById(R.id.textinput_physic_height);
                String height = heightInput.getEditText().getText().toString().trim();
                if (!height.equals("")) {
                    physicInfo.setHeight(Integer.parseInt(height));
                }
                // 몸무게
                TextInputLayout weightInput = (TextInputLayout) getActivity().findViewById(R.id.textinput_physic_weight);
                String weight = String.valueOf(weightInput.getEditText().getText()).trim();
                if (!weight.equals("")) {
                    physicInfo.setWeight(Integer.parseInt(weight));
                }
                
                if (physicInfo.getGender() == null || physicInfo.getAge() == null
                        || physicInfo.getHeight() == null || physicInfo.getWeight() == null) {
                    ArrayList<String> notYet = new ArrayList<>(4);
                    if (physicInfo.getGender() == null) {
                        notYet.add("성별");
                    }
                    if (physicInfo.getAge() == null) {
                        notYet.add("나이");
                    }
                    if (physicInfo.getHeight() == null) {
                        notYet.add("키");
                    }
                    if (physicInfo.getWeight() == null) {
                        notYet.add("몸무게");
                    }
                    StringBuilder msg = new StringBuilder();
                    msg.append("입력하지 않은 값:");
                    for (String notInput:
                         notYet) {
                        msg.append(" ").append(notInput);
                    }
                    new MaterialAlertDialogBuilder(view.getContext(), R.style.AlertDialogTheme)
                            .setTitle("모든 값을 입력해주세요")
                            .setMessage(msg.toString())
                            // 우측을 누르면 다이얼로그가 꺼짐
                            .setPositiveButton("다시 입력하기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                } else {
                    // 프래그먼트간 데이터 전달
                    physicViewModel.setPhysicInfo(physicInfo);

                    // 프래그먼트 - 활동 설문 페이지
                    Fragment detailFragment = new DetailFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    // 로그인 페이지 대체
                    fragmentTransaction.replace(R.id.fragment_login, detailFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                break;
            }
        }
    }

//    private void keyboardDown(TextInputLayout text) {
//        text.getEditText().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                if (i == keyEvent.KEYCODE_NUMPAD_ENTER || i == 66) {
//                    InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    manager.hideSoftInputFromWindow(text.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//
//                    text.clearFocus();
//                    text.setFocusable(false);
//                    text.setFocusableInTouchMode(true);
//                }
//                return false;
//            }
//        });
//    }
}