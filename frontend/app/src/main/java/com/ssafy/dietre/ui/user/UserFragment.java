package com.ssafy.dietre.ui.user;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ssafy.dietre.LoginActivity;
import com.ssafy.dietre.MainActivity;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.DeleteUserApiCall;
import com.ssafy.dietre.api.GetUserInfoApiCall;
import com.ssafy.dietre.api.PatchUserInfoApiCall;
import com.ssafy.dietre.api.request.UserRegisterReq;
import com.ssafy.dietre.common.provider.MainActivityProvider;
import com.ssafy.dietre.common.type.Gender;
import com.ssafy.dietre.common.type.UserDesiredStatus;
import com.ssafy.dietre.databinding.FragmentUserBinding;
import com.ssafy.dietre.ui.filter.InputFilterMinMax;
import com.ssafy.dietre.ui.week.WeekFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserFragment extends Fragment implements View.OnClickListener {

    private FragmentUserBinding binding;
//    static String goalType = ""; // 식사 종류

    TextView nameTextView;
    TextView emailTextView;
    TextView genderTextView;
    TextView ageTextView;
    TextView heightTextView;
    TextView weightTextView;
    TextInputLayout kcalInputLayout;
    TextView carbTextView;
    TextView protTextView;
    TextView fatTextView;
    Button dietGoal;
    Button dietActivity;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    boolean isButtonClicked_acitivty = false;
    boolean isButtonClicked_goal = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        Log.e("프래그먼트 데이터 테스트 - 마이페이지 - 목표 수정", "아직 활동량 및 목표 수정 불가");

        // 화면에서 사용하는 TextView
        // 이름
        nameTextView = (TextView) root.findViewById(R.id.text_user_social_name);
        // 이메일
        emailTextView = (TextView) root.findViewById(R.id.text_user_social_email);
        // 성별
        genderTextView = (TextView) root.findViewById(R.id.text_user_gender);
        String userGender = (String) genderTextView.getText();
        // 나이
        ageTextView = (TextView) root.findViewById(R.id.text_user_age);
        String userAge = (String) ageTextView.getText();
        // 키
        heightTextView = (TextView) root.findViewById(R.id.text_user_height);
        String userHeight = (String) heightTextView.getText();
        // 몸무게
        weightTextView = (TextView) root.findViewById(R.id.text_user_weight);
        String userWeight = (String) weightTextView.getText();
        // 칼로리
        kcalInputLayout = (TextInputLayout) root.findViewById(R.id.textinput_user_kcal);
        // 탄수화물
        carbTextView = (TextView) root.findViewById(R.id.text_user_carb);
        // 단백질
        protTextView = (TextView) root.findViewById(R.id.text_user_prot);
        // 지방
        fatTextView = (TextView) root.findViewById(R.id.text_user_fat);

        // 화면에서 사용하는 버튼
        // 활동량 수정 버튼
        Button activityUpdateBtn = (Button) binding.textbtnUserActivity;
        // 활동량 수정 버튼
        dietActivity = (Button) binding.btnUserActivity;
        // 나의 목표 수정 버튼
        Button goalUpdateBtn = (Button) binding.textbtnUserGoal;
        // 식사 종류 선택
        dietGoal = (Button) binding.btnUserGoal;
        // 정보 수정 버튼
        Button updateBtn = (Button) root.findViewById(R.id.btn_user_update);
        // 정부 수정 후 확인 버튼
        Button confirmBtn = (Button) root.findViewById(R.id.btn_user_confirm);
        // 이번 주 분석 버튼
        Button weekResBtn = (Button) root.findViewById(R.id.btn_user_week);
        // 회원 탈퇴 버튼
        Button withdrawalBtn = (Button) root.findViewById(R.id.textbtn_user_withdrawal);
        // 로그 아웃 버튼
        Button logoutBtn = (Button) root.findViewById(R.id.textbtn_user_logout);

        // 버튼과 이벤트 연결
        // 활동량 수정 버튼
        activityUpdateBtn.setOnClickListener(this);
        // 활동량 종류
        dietActivity.setOnClickListener(this);
        // 목표 수정 버튼
        goalUpdateBtn.setOnClickListener(this);
        // 식사 종류
        dietGoal.setOnClickListener(this);
        // 정보 수정
        updateBtn.setOnClickListener(this);
        // 정보 수정 후 확인 버튼
        confirmBtn.setOnClickListener(this);
        // 이번 주 분석 버튼
        weekResBtn.setOnClickListener(this);
        // 회원 탈퇴
        withdrawalBtn.setOnClickListener(this);
        // 로그아웃
        logoutBtn.setOnClickListener(this);

        GetUserInfoApiCall.call(
                nameTextView, emailTextView, genderTextView, ageTextView,
                heightTextView, weightTextView, kcalInputLayout, carbTextView,
                protTextView, fatTextView,
                dietGoal, dietActivity,
                this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * 수정할 때 생기는 레이아웃에 기존의 TextView의 값을 넣을 수 있도록
     * @param userGender
     * @param userAge
     * @param userHeight
     * @param userWeight
     */
    public void setHiddenInputForUpdate(String userGender, String userAge
            , String userHeight, String userWeight) {
        // 성별
        Button femaleBtn = binding.btnUserFemale;
        Button maleBtn = binding.btnUserMale;
        if (userGender.equals("여자")) {
            femaleBtn.setBackgroundColor(getResources().getColor(R.color.recommend_acc));
            maleBtn.setBackgroundColor(getResources().getColor(R.color.recommend_back));
        } else {
            femaleBtn.setBackgroundColor(getResources().getColor(R.color.recommend_back));
            maleBtn.setBackgroundColor(getResources().getColor(R.color.recommend_acc));
        }

        // 나이
        TextInputLayout ageEditText = (TextInputLayout) binding.textinputUserAge;
        ageEditText.getEditText().setFilters(new InputFilter[]{new InputFilterMinMax("1", "120")});
        ageEditText.getEditText().setText(userAge);

        // 키
        TextInputLayout heightEditText = (TextInputLayout) binding.textinputUserHeight;
        heightEditText.getEditText().setFilters(new InputFilter[]{new InputFilterMinMax("1", "260")});
        heightEditText.getEditText().setText(userHeight);

        // 몸무게
        TextInputLayout weightEditText = (TextInputLayout) binding.textinputUserWeight;
        weightEditText.getEditText().setFilters(new InputFilter[]{new InputFilterMinMax("1", "610")});
        weightEditText.getEditText().setText(userWeight);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 활동량 수정 버튼
            case R.id.textbtn_user_activity: {
                Log.e("프래그먼트 데이터 테스트 - 마이페이지 - 활동량 수정", "이제야 가능함");
                dietActivity.setBackgroundColor(Color.parseColor("#bef1de"));
                dietActivity.setClickable(true);
                isButtonClicked_acitivty = true;
                break;
            }
            case R.id.btn_user_activity: {
                if (!isButtonClicked_acitivty) {
                    dietActivity.setClickable(false);
                    break;
                }
                // menu 드롭다운 띄우기(material 3)
                android.widget.PopupMenu popup = new android.widget.PopupMenu(getContext(), view, Gravity.END, 0, R.style.DietRe_Popup);
                popup.getMenuInflater().inflate(R.menu.activity_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        UserRegisterReq updateActivity = new UserRegisterReq();
                        // 목표 선택
                        switch (menuItem.getItemId()) {
                            // 거의 없다
                            case R.id.activity_one: {
                                // TODO 백으로 값을 전달 - 목표
                                dietActivity.setText(R.string.text_activity_one);
                                dietActivity.setBackgroundColor(Color.parseColor("#f4fffb"));
                                dietActivity.setClickable(false);
                                updateActivity.setActivityLevel(1);
                                isButtonClicked_acitivty = false;
                                Log.e("프래그먼트 데이터 테스트 - 마이페이지 - 활동량 수정", "다시 막음");
                                break;
                            }
                            // 조금 있다
                            case R.id.activity_two: {
                                // TODO 백으로 값을 전달 - 목표
                                dietActivity.setText(R.string.text_activity_two);
                                dietActivity.setBackgroundColor(Color.parseColor("#f4fffb"));
                                dietActivity.setClickable(false);
                                updateActivity.setActivityLevel(2);
                                isButtonClicked_acitivty = false;
                                Log.e("프래그먼트 데이터 테스트 - 마이페이지 - 활동량 수정", "다시 막음");
                                break;
                            }
                            // 보통
                            case R.id.activity_three: {
                                // TODO 백으로 값을 전달 - 목표
                                dietActivity.setText(R.string.text_activity_three);
                                dietActivity.setBackgroundColor(Color.parseColor("#f4fffb"));
                                dietActivity.setClickable(false);
                                updateActivity.setActivityLevel(3);
                                isButtonClicked_acitivty = false;
                                Log.e("프래그먼트 데이터 테스트 - 마이페이지 - 활동량 수정", "다시 막음");
                                break;
                            }
                            // 꽤 있다
                            case R.id.activity_four: {
                                // TODO 백으로 값을 전달 - 목표
                                dietActivity.setText(R.string.text_activity_four);
                                dietActivity.setBackgroundColor(Color.parseColor("#f4fffb"));
                                dietActivity.setClickable(false);
                                updateActivity.setActivityLevel(4);
                                isButtonClicked_acitivty = false;
                                Log.e("프래그먼트 데이터 테스트 - 마이페이지 - 활동량 수정", "다시 막음");
                                break;
                            }
                            // 아주 많다
                            case R.id.activity_five: {
                                // TODO 백으로 값을 전달 - 목표
                                dietActivity.setText(R.string.text_activity_five);
                                dietActivity.setBackgroundColor(Color.parseColor("#f4fffb"));
                                dietActivity.setClickable(false);
                                updateActivity.setActivityLevel(5);
                                isButtonClicked_acitivty = false;
                                Log.e("프래그먼트 데이터 테스트 - 마이페이지 - 활동량 수정", "다시 막음");
                                break;
                            }
                        }
                        PatchUserInfoApiCall.call(
                                updateActivity, kcalInputLayout, carbTextView,
                                protTextView, fatTextView);
                        return UserFragment.super.onOptionsItemSelected(menuItem);
                    }
                });
                popup.show();
                break;
            }
            // 목표 수정 버튼
            case R.id.textbtn_user_goal: {
                Log.e("프래그먼트 데이터 테스트 - 마이페이지 - 목표 수정", "이제야 가능함");
                dietGoal.setBackgroundColor(Color.parseColor("#bef1de"));
                dietGoal.setClickable(true);
                isButtonClicked_goal = true;
                break;
            }
            // 목표
            case R.id.btn_user_goal: {
                if (!isButtonClicked_goal) {
                    dietGoal.setClickable(false);
                    break;
                }
                // menu 드롭다운 띄우기(material 3)
                android.widget.PopupMenu popup = new android.widget.PopupMenu(getContext(), view, Gravity.END, 0, R.style.DietRe_Popup);
                popup.getMenuInflater().inflate(R.menu.goal_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        UserRegisterReq updateGoal = new UserRegisterReq();
                        // 목표 선택
                        switch (menuItem.getItemId()) {
                            // 감소
                            case R.id.goal_decre:
                                // TODO 백으로 값을 전달 - 목표
                                dietGoal.setText(R.string.goal_decre);
                                dietGoal.setBackgroundColor(Color.parseColor("#f4fffb"));
                                dietGoal.setClickable(false);
                                updateGoal.setStatus(UserDesiredStatus.decrease);
                                isButtonClicked_goal = false;
                                Log.e("프래그먼트 데이터 테스트 - 마이페이지 - 목표 수정", "다시 막음");
                                break;
                            // 유지
                            case R.id.goal_keep:
                                // TODO 백으로 값을 전달 - 목표
                                dietGoal.setText(R.string.goal_keep);
                                dietGoal.setBackgroundColor(Color.parseColor("#f4fffb"));
                                dietGoal.setClickable(false);
                                updateGoal.setStatus(UserDesiredStatus.maintain);
                                isButtonClicked_goal = false;
                                Log.e("프래그먼트 데이터 테스트 - 마이페이지 - 목표 수정", "다시 막음");
                                break;
                            // 증가
                            case R.id.goal_incre:
                                // TODO 백으로 값을 전달 - 목표
                                dietGoal.setText(R.string.goal_incre);
                                dietGoal.setBackgroundColor(Color.parseColor("#f4fffb"));
                                dietGoal.setClickable(false);
                                updateGoal.setStatus(UserDesiredStatus.increase);
                                isButtonClicked_goal = false;
                                Log.e("프래그먼트 데이터 테스트 - 마이페이지 - 목표 수정", "다시 막음");
                                break;
                        }
                        PatchUserInfoApiCall.call(
                                updateGoal, kcalInputLayout, carbTextView,
                                protTextView, fatTextView);
                        return UserFragment.super.onOptionsItemSelected(menuItem);
                    }
                });
                popup.show();
                break;
            }
            // 정보 수정
            case R.id.btn_user_update: {
                // 수정 버튼을 누르면 뷰의 변경이 있어야 함
                // 변경되어야 하는 뷰
                // 1. 수정 버튼 -> 수정 확인 버튼
                Button userUpdateBtn
                        = (Button) getActivity().findViewById(R.id.btn_user_update);
                Button userConfirmBtn
                        = (Button) getActivity().findViewById(R.id.btn_user_confirm);

                userUpdateBtn.setVisibility(View.GONE);
                userConfirmBtn.setVisibility(View.VISIBLE);

                // 2. 성별 text -> 성별 선택 버튼
                MaterialButtonToggleGroup genderSelectButton
                        = (MaterialButtonToggleGroup) getActivity().findViewById(R.id.btn_user_gender);
                Button femaleBtn = getActivity().findViewById(R.id.btn_user_female);
                Button maleBtn = getActivity().findViewById(R.id.btn_user_male);
                // 임시 1-2. 해당 제목이 성별 textview 값으로 setText()
                TextView genderText = (TextView) getActivity().findViewById(R.id.text_user_gender);
                genderSelectButton.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
                    @Override
                    public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
                        if (isChecked) {
                            if (checkedId == R.id.btn_user_female) {
                                femaleBtn.setBackgroundColor(getResources().getColor(R.color.recommend_acc));
                                maleBtn.setBackgroundColor(getResources().getColor(R.color.recommend_back));
                                genderText.setText("여자");
                            } else {
                                femaleBtn.setBackgroundColor(getResources().getColor(R.color.recommend_back));
                                maleBtn.setBackgroundColor(getResources().getColor(R.color.recommend_acc));
                                genderText.setText("남자");
                            }
                        }
                    }
                });
                genderText.setVisibility(View.GONE);
                genderSelectButton.setVisibility(View.VISIBLE);

                // 3. 나이 text -> 나이 입력 버튼
                TextView ageText
                        = (TextView) getActivity().findViewById(R.id.text_user_age);
                TextInputLayout ageEditText
                        = (TextInputLayout) getActivity().findViewById(R.id.textinput_user_age);
                keyboardDown(ageEditText);
                ageText.setVisibility(View.GONE);
                ageEditText.setVisibility(View.VISIBLE);

                // 4. 키 text -> 키 입력 버튼
                TextView heightText
                        = (TextView) getActivity().findViewById(R.id.text_user_height);
                TextInputLayout heightEditText
                        = (TextInputLayout) getActivity().findViewById(R.id.textinput_user_height);
                keyboardDown(heightEditText);
                heightText.setVisibility(View.GONE);
                heightEditText.setVisibility(View.VISIBLE);

                // 5. 몸무게 text -> 몸무게 입력 버튼
                TextView weightText
                        = (TextView) getActivity().findViewById(R.id.text_user_weight);
                TextInputLayout weightEditText
                        = (TextInputLayout) getActivity().findViewById(R.id.textinput_user_weight);
                keyboardDown(weightEditText);
                weightText.setVisibility(View.GONE);
                weightEditText.setVisibility(View.VISIBLE);

                break;
            }
            // 정부 수정 후 확인 버튼
            case R.id.btn_user_confirm: {
                // TODO 수정 확인 부분
                UserRegisterReq updatePersonalInfo = new UserRegisterReq();
                // 값이 변경되는 부분
                // 1. 성별
                // 1-1. 성별 버튼이 클릭되면 클릭된 값의 이름?을 불러와야
                // 성별을 고르는 버튼
                MaterialButtonToggleGroup genderSelectButton
                        = (MaterialButtonToggleGroup) getActivity().findViewById(R.id.btn_user_gender);
                // 임시 1-2. 해당 제목이 성별 textview 값으로 setText()
                TextView genderText = (TextView) getActivity().findViewById(R.id.text_user_gender);
                // TODO 1-2. 확인 버튼을 눌렀을 때 백엔드에 전달할 데이터 저장
                // 성별 체크 버튼 아이디가 여자 버튼 아이디와 동일한 경우 여자로 데이터 전달
                if (genderSelectButton.getCheckedButtonId() == R.id.btn_user_female) {
                    updatePersonalInfo.setGender(Gender.female);
                    Log.e("프래그먼트 데이터 확인 : 마이페이지 유저 수정확인", "여자 선택");
                }
                // 성별 체크 버튼 아이디가 남자 버튼 아이디와 동일한 경우 남자로 데이터 전달
                else if (genderSelectButton.getCheckedButtonId() == R.id.btn_user_male) {
                    updatePersonalInfo.setGender(Gender.male);
                    Log.e("프래그먼트 데이터 확인 : 마이페이지 유저 수정확인", "남자 선택");
                }
                // 확인 버튼 이전인 수정하기를 단계에서 버튼을 따로 누르지 않고 확인 버튼을 눌렀을 때
                else {
                    // 페이지에 남겨있던 값이 여자이면 수정 확인 단계에서 여자 정보를 전송
                    if (genderText.getText() == "여자") {
                        updatePersonalInfo.setGender(Gender.female);
                        Log.e("프래그먼트 데이터 확인 : 마이페이지 유저 수정확인", "기존 유저가 여자");
                    }
                    // 페이지에 남겨있던 값이 남자이면 수정 확인 단계에서 남자 정보를 전송
                    else {
                        updatePersonalInfo.setGender(Gender.male);
                        Log.e("프래그먼트 데이터 확인 : 마이페이지 유저 수정확인", "기존 유저가 님자");
                    }
                }


                // 2. 나이
                // 2-1. 나이를 입력한 후 수정 버튼을 눌렀을 때 전달이 되어야
                TextInputLayout ageInput = getActivity().findViewById(R.id.textinput_user_age);
                String age = ageInput.getEditText().getText().toString().trim();
                // 임시 2-2. 해당 입력값이 나이 textview 값으로 setText()
                TextView ageTextView = getActivity().findViewById(R.id.text_user_age);
                ageTextView.setText(age+" 세");
                // TODO 2-2. 확인 버튼을 눌렀을 때 백엔드에 전달할 데이터 저장
                if (age.equals("")) {
                    updatePersonalInfo.setAge(0);
                } else {
                    updatePersonalInfo.setAge(Integer.parseInt(age));
                }

                // 3. 키
                // 3-1. 키를 입력한 후 수정 버튼을 눌렀을 때 전달이 되어야
                TextInputLayout heightInput = getActivity().findViewById(R.id.textinput_user_height);
                String height = heightInput.getEditText().getText().toString().trim();
                // 임시 3-2. 해당 입력값이 키의 textview 값으로 setText()
                TextView heightTextView = getActivity().findViewById(R.id.text_user_height);
                heightTextView.setText(height+" cm");
                // TODO 3-2. 확인 버튼을 눌렀을 때 백엔드에 전달할 데이터 저장
                if (height.equals("")) {
                    updatePersonalInfo.setHeight(0);
                } else {
                    updatePersonalInfo.setHeight(Integer.parseInt(height));
                }

                // 4. 몸무게
                // 4-1. 몸무게를 입력한 후 수정 버튼을 눌렀을 때 전달이 되어야
                TextInputLayout weightInput = getActivity().findViewById(R.id.textinput_user_weight);
                String weight = weightInput.getEditText().getText().toString().trim();
                // 임시 4-2. 해당 입력값이 몸무게의 textview 값으로 setText()
                TextView weightTextView = getActivity().findViewById(R.id.text_user_weight);
                weightTextView.setText(weight+" Kg");
                // TODO 4-2. 확인 버튼을 눌렀을 때 백엔드에 전달할 데이터 저장
                if (weight.equals("")) {
                    updatePersonalInfo.setWeight(0);
                } else {
                    updatePersonalInfo.setWeight(Integer.parseInt(weight));
                }

                // TODO 수정한 값 PatchUserInfoApiCall에 전달
                if (updatePersonalInfo.getAge() == 0
                        || updatePersonalInfo.getHeight() == 0
                        || updatePersonalInfo.getWeight() == 0) {
                    ArrayList<String> notYet = new ArrayList<>(4);
                    if (updatePersonalInfo.getAge() == 0) {
                        notYet.add("나이");
                    }
                    if (updatePersonalInfo.getHeight() == 0) {
                        notYet.add("키");
                    }
                    if (updatePersonalInfo.getWeight() == 0) {
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
                            .setNeutralButton("다시 입력하기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                } else {
                    sendToBack(updatePersonalInfo);

                    // 수정 버튼을 누르면 뷰의 변경이 있어야 함
                    // 변경되어야 하는 뷰
                    // 1. 수정 버튼 -> 수정 확인 버튼
                    Button userUpdateBtn
                            = (Button) getActivity().findViewById(R.id.btn_user_update);
                    Button userConfirmBtn
                            = (Button) getActivity().findViewById(R.id.btn_user_confirm);
                    userUpdateBtn.setVisibility(View.VISIBLE);
                    userConfirmBtn.setVisibility(View.GONE);

                    // 2. 성별 text -> 성별 선택 버튼
                    MaterialButtonToggleGroup genderBtn
                            = (MaterialButtonToggleGroup) getActivity().findViewById(R.id.btn_user_gender);
                    genderText.setVisibility(View.VISIBLE);
                    genderBtn.setVisibility(View.GONE);

                    // 3. 나이 text -> 나이 입력 버튼
                    TextView ageText
                            = (TextView) getActivity().findViewById(R.id.text_user_age);
                    TextInputLayout ageEditText
                            = (TextInputLayout) getActivity().findViewById(R.id.textinput_user_age);
                    ageText.setVisibility(View.VISIBLE);
                    ageEditText.setVisibility(View.GONE);

                    // 4. 키 text -> 키 입력 버튼
                    TextView heightText
                            = (TextView) getActivity().findViewById(R.id.text_user_height);
                    TextInputLayout heightEditText
                            = (TextInputLayout) getActivity().findViewById(R.id.textinput_user_height);
                    heightText.setVisibility(View.VISIBLE);
                    heightEditText.setVisibility(View.GONE);

                    // 5. 몸무게 text -> 몸무게 입력 버튼
                    TextView weightText
                            = (TextView) getActivity().findViewById(R.id.text_user_weight);
                    TextInputLayout weightEditText
                            = (TextInputLayout) getActivity().findViewById(R.id.textinput_user_weight);
                    weightText.setVisibility(View.VISIBLE);
                    weightEditText.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.btn_user_week: {
                // Create new fragment and transaction
                FragmentTransaction transaction
                        = getActivity().getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                // 마이페이지의 외부를 이번주 분석의 프래그먼트로 변경해야 함
                transaction.replace(R.id.layout_user, new WeekFragment());
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
                break;
            }
            case R.id.textbtn_user_withdrawal: {
                new MaterialAlertDialogBuilder(view.getContext(), R.style.AlertDialogTheme)
                        .setTitle(R.string.text_user_withdrawal)
                        // 좌측이 회원 탈퇴
                        .setNeutralButton(R.string.btn_user_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // TODO 로그인했을 때 생성되는 토큰 삭제코드 추가
                                DeleteUserApiCall.call(UserFragment.this);
                            }
                        })
                        // 우측을 누르면 다이얼로그가 꺼짐
                        .setPositiveButton(R.string.btn_user_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                break;
            }
            case R.id.textbtn_user_logout: {
                new MaterialAlertDialogBuilder(view.getContext(), R.style.AlertDialogTheme)
                        .setTitle(R.string.text_user_logout)
                        // 좌측이 회원 탈퇴
                        .setNeutralButton(R.string.btn_user_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // TODO 로그인했을 때 생성되는 토큰 삭제코드 추가
                                SharedPreferences.Editor editor = MainActivityProvider.getActivity()
                                        .getSharedPreferences("jwt", Context.MODE_PRIVATE).edit();
                                editor.remove("jwt");
                                editor.commit();

                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        })
                        // 우측을 누르면 다이얼로그가 꺼짐
                        .setPositiveButton(R.string.btn_user_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                break;
            }
        }
    }

    private void sendToBack(UserRegisterReq updatePersonalInfo) {
        PatchUserInfoApiCall.call(updatePersonalInfo,
                kcalInputLayout, carbTextView, protTextView, fatTextView);
    }

    private void keyboardDown(TextInputLayout text) {
        text.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == keyEvent.KEYCODE_NUMPAD_ENTER || i == 66) {
                    InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(text.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    text.clearFocus();
                    text.setFocusable(false);
                    text.setFocusableInTouchMode(true);
                }
                return false;
            }
        });
    }
}
