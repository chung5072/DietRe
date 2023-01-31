package com.ssafy.dietre.ui.survey;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.slider.Slider;
import com.ssafy.dietre.LoginActivity;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.GetNutInfoInSurveyApiCall;
import com.ssafy.dietre.api.PostUserInfoApiCall;
import com.ssafy.dietre.api.request.UserRegisterReq;
import com.ssafy.dietre.common.type.Gender;
import com.ssafy.dietre.common.type.UserDesiredStatus;
import com.ssafy.dietre.databinding.FragmentDetailBinding;

import java.util.ArrayList;

public class DetailFragment extends Fragment implements View.OnClickListener {

    private FragmentDetailBinding fragmentDetailBinding;
    private DetailFragment detailFragment;
    // ViewModel 연결
    PhysicViewModel physicViewModel;

    PhysicInfo physicInfo;

    // 식단 관리 목표
    static String goalType = "";
    // 사람이 끼니 수를 눌렀는지 확인
    boolean isHumanCheckedTimes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        detailFragment = this;

        // ViewModel 연결
        physicViewModel
                = new ViewModelProvider(requireActivity()).get(PhysicViewModel.class);
        physicInfo = new PhysicInfo();

        // Fragment - xml 파일 연결
        fragmentDetailBinding = FragmentDetailBinding.inflate(inflater, container, false);
        View root = fragmentDetailBinding.getRoot();

        // 화면에서 사용하는 슬라이더
        // 슬라이더 텍스트
        TextView actSliderText = (TextView) fragmentDetailBinding.slidertextActivityAmount;
        // 슬라이더
        Slider actSlider = (Slider) fragmentDetailBinding.sliderActivityAmount;
        // 슬라이더에 데이터 연결
        // 슬라이더 텍스트
        // 슬라이더
        actSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                switch ((int) actSlider.getValue()) {
                    case 1 : {
                        actSliderText.setText(R.string.text_activity_one);
                        physicInfo.setActivityLevel(1);
                        break;
                    }
                    case 2 : {
                        actSliderText.setText(R.string.text_activity_two);
                        physicInfo.setActivityLevel(2);
                        break;
                    }
                    case 3 : {
                        actSliderText.setText(R.string.text_activity_three);
                        physicInfo.setActivityLevel(3);
                        break;
                    }
                    case 4 : {
                        actSliderText.setText(R.string.text_activity_four);
                        physicInfo.setActivityLevel(4);
                        break;
                    }
                    case 5 : {
                        actSliderText.setText(R.string.text_activity_five);
                        physicInfo.setActivityLevel(5);
                        break;
                    }
                }
            }
        });
        // 선택하지 않으면 기본적으로 3으로 줌
        if ((int)actSlider.getValue() == 3) {
            actSliderText.setText(R.string.text_activity_three);
            physicInfo.setActivityLevel(3);
        }

        // 화면에서 사용하는 버튼
        // 식단 관리 목표 선택 버튼
        Button goalButton = (Button) fragmentDetailBinding.btnActivityGoal;
        // 이전 설문 페이지로 이동하는 버튼
        Button prevButton = (Button) fragmentDetailBinding.btnActivityPrev;
        // 다음 설문 페이지로 이동하는 버튼
        Button nextButton =(Button) fragmentDetailBinding.btnActivityNext;
        // 하루에 몇 끼를 먹는지 받는 버튼
        MaterialButtonToggleGroup eatTimesGroup
                = (MaterialButtonToggleGroup) fragmentDetailBinding.btnActivityTimes;
        // 2끼 버튼
        Button twoTimesButton = (Button) fragmentDetailBinding.btnActivityTwo;
        // 3끼 버튼
        Button threeTimesButton = (Button) fragmentDetailBinding.btnActivityThree;
        // 주말을 포함하는지 확인하는 버튼
        MaterialSwitch weekendSwitch = (MaterialSwitch) fragmentDetailBinding.switchActivityWeekend;

        // 버튼과 클릭 이벤트 연결
        // 목표 선택
        goalButton.setOnClickListener(this);
        // 이전 페이지
        prevButton.setOnClickListener(this);
        // 다음 페이지
        nextButton.setOnClickListener(this);
        // 몇 끼 먹는지 버튼
        // 사람이 누르지 않았다는 표시
        isHumanCheckedTimes = false;
        // 버그 방지를 위해서 임시로 아침을 먹지 않는다고 설정함
        physicInfo.setCountBreakfast(false);
        eatTimesGroup.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                if (isChecked) {
                    if (checkedId == R.id.btn_activity_two) {
                        twoTimesButton.setBackgroundColor(Color.parseColor("#CCE9FF"));
                        threeTimesButton.setBackgroundColor(Color.parseColor("#fafafa"));
                        physicInfo.setCountBreakfast(false);
                        isHumanCheckedTimes = true;
                    } else {
                        twoTimesButton.setBackgroundColor(Color.parseColor("#fafafa"));
                        threeTimesButton.setBackgroundColor(Color.parseColor("#CCE9FF"));
                        physicInfo.setCountBreakfast(true);
                        isHumanCheckedTimes = true;
                    }
                }
            }
        });
        // 주말을 포함 여부를 사람이 선택하지 않았을 때 기본으로 포함하지 않는다고 설정
        physicInfo.setCountWeekend(false);
        weekendSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                physicInfo.setCountWeekend(b);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentDetailBinding = null;
    }

    /**
     * 클릭 이벤트 함수
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_activity_goal: {
                Button goalBtn = (Button) view;
                // menu 드롭다운 띄우기(material 3)
                PopupMenu popup = new PopupMenu(getContext(), view, Gravity.END, 0, R.style.DietRe_Popup);
                popup.getMenuInflater().inflate(R.menu.goal_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            // 감소
                            case R.id.goal_decre:
                                goalBtn.setText(R.string.goal_decre);
                                goalType = (String) goalBtn.getText();
                                physicInfo.setStatus(UserDesiredStatus.decrease);
                                break;
                            // 유지    
                            case R.id.goal_keep:
                                goalBtn.setText(R.string.goal_keep);
                                goalType = (String) goalBtn.getText();
                                physicInfo.setStatus(UserDesiredStatus.maintain);
                                break;
                            // 증가
                            case R.id.goal_incre:
                                goalBtn.setText(R.string.goal_incre);
                                goalType = (String) goalBtn.getText();
                                physicInfo.setStatus(UserDesiredStatus.increase);
                                break;
                        }
                        return DetailFragment.super.onOptionsItemSelected(menuItem);
                    }
                });
                popup.show();
                break;
            }
            case R.id.btn_activity_prev: {
                // 프래그먼트 - 신체 설문 페이지
                Fragment physicFragment = new PhysicFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // 로그인 페이지 대체
                fragmentTransaction.replace(R.id.fragment_login, physicFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
            }
            case R.id.btn_activity_next: {
                // 백으로 한번 데이터를 전달
                // 앞에서 받아온 데이터
                Gender gender = physicViewModel.getPhysicInfo().getValue().getGender();
                int age = physicViewModel.getPhysicInfo().getValue().getAge();
                int height = physicViewModel.getPhysicInfo().getValue().getHeight();
                int weight = physicViewModel.getPhysicInfo().getValue().getWeight();
                // 여기서 입력한 데이터
                boolean countBreakfast = physicInfo.getCountBreakfast();
                boolean countWeekend = physicInfo.getCountWeekend();
                int activityLevel = physicInfo.getActivityLevel();
                UserDesiredStatus status = physicInfo.getStatus();

                physicInfo.setGender(gender);
                physicInfo.setAge(age);
                physicInfo.setHeight(height);
                physicInfo.setWeight(weight);
                physicInfo.setCountBreakfast(countBreakfast);
                physicInfo.setCountWeekend(countWeekend);
                physicInfo.setActivityLevel(activityLevel);
                physicInfo.setStatus(status);

                // 입력하지 않은 값이 있을 경우 보내지 않음
                if (!isHumanCheckedTimes || physicInfo.getCountWeekend() == null
                        || physicInfo.getActivityLevel() == null || physicInfo.getStatus() == null) {
                    ArrayList<String> notYet = new ArrayList<>(4);
                    if (!isHumanCheckedTimes) {
                        notYet.add("끼니 수");
                    }
                    if (physicInfo.getCountWeekend() == null) {
                        notYet.add("주말 포함");
                    }
                    if (physicInfo.getActivityLevel() == null) {
                        notYet.add("활동량");
                    }
                    if (physicInfo.getStatus() == null) {
                        notYet.add("목표");
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
                    // 추천 칼로리 및 탄단지 값을 반환하는 api로 변경 - 내부에 프래그먼트간 이동 포함
                    GetNutInfoInSurveyApiCall.call(physicInfo, physicViewModel,
                            (LoginActivity) detailFragment.getActivity());
                }
                break;
            }
        }
    }
}