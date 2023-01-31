package com.ssafy.dietre.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.ssafy.dietre.MainActivity;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.ApiCallService;
import com.ssafy.dietre.api.GetRecommendationMealApiCall;
import com.ssafy.dietre.api.data.FoodDto;
import com.ssafy.dietre.api.data.MealStatistics;
import com.ssafy.dietre.api.response.MealRecommendRes;
import com.ssafy.dietre.api.response.SpecificMealInfoRes;
import com.ssafy.dietre.api.response.UserInfoRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.type.MealTime;
import com.ssafy.dietre.common.type.MealType;
import com.ssafy.dietre.common.type.UserDesiredStatus;
import com.ssafy.dietre.databinding.FragmentHomeBinding;
import com.ssafy.dietre.ui.survey.NutFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private LoadingRecoFragment loadingRecoFragment;

    // 데이터 전달 뷰 모델
    HomeViewModel homeViewModel;

    // 하루 기록
    String axiostoday;
    TextView textToday;
    float real = 0F;
    float rcmd = 0F;
    int todayCnt = 0;
    TextView realKcal;
    TextView rcmdKcal;
    ImageView heart;
    TextView textBreakfastKcal;
    TextView textBreakfastCpf;
    TextView textLunchKcal;
    TextView textLunchCpf;
    TextView textDinnerKcal;
    TextView textDinnerCpf;

    // 레이아웃
    LinearLayout noneLayout;
    LinearLayout trayLayout;
    LinearLayout singleLayout;
    LinearLayout recoTitleLayout;
    CoordinatorLayout recoResultLayout;

    // 단품
    ImageView singleFoodImg;
    // 식판
    // 밥
    ImageView mainFoodImg;
    // 사이드 1
    ImageView sideOneImg;
    // 사이드 2
    ImageView sideTwoImg;
    // 국
    ImageView soupImg;
    // 김치
    ImageView kimchiImg;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        loadingRecoFragment =  new LoadingRecoFragment();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 오늘 날짜
        textToday = (TextView) binding.titleToday;
        textToday.setText(getToday());
        // 섭취, 권장칼로리
        realKcal = (TextView) binding.textRealkcal;
        rcmdKcal = (TextView) binding.textRcmdkcal;
        heart = (ImageView) binding.imgHeart;
        // 아점저 칼로리&탄단지량 텍스트
        textBreakfastKcal = (TextView) binding.textBreakfastkcal;
        textBreakfastCpf = (TextView) binding.textBreakfastcpf;
        textLunchKcal = (TextView) binding.textLunchkcal;
        textLunchCpf = (TextView) binding.textLunchcpf;
        textDinnerKcal = (TextView) binding.textDinnerkcal;
        textDinnerCpf = (TextView) binding.textDinnercpf;

        // axios 연결
        // 식사별 기록
        callTodayLog(MealTime.breakfast);
        callTodayLog(MealTime.lunch);
        callTodayLog(MealTime.dinner);

        // 화면에서 사용하는 레이아웃
        TextInputLayout timeInput = binding.textfieldHomeTime;
        TextInputLayout goalInput = binding.textfieldHomeGoal;
        TextInputLayout kindInput = binding.textfieldHomeKind;
        // 추천 식단 타이틀
        recoTitleLayout = binding.layoutHomeRecoTitle;
        // 추천 받기 전 초기 화면
        noneLayout = binding.layoutHomeNone;
        // 추천 결과 레이아웃
        recoResultLayout = binding.layoutHomeRecoResult;

        // 선택하지 않았을 때는 힌트가 보이고, 선택한 후에는 힌트가 보이지 않음
        timeInput.setHintEnabled(timeInput.getEditText() == null);
        goalInput.setHintEnabled(goalInput.getEditText() == null);
        kindInput.setHintEnabled(kindInput.getEditText() == null);

        // 이미지 뷰 - 음식
        // 밥
        mainFoodImg = binding.imgHomeMain;
        // 국
        soupImg = binding.imgHomeSoup;
        // 사이드1
        sideOneImg = binding.imgHomeSideOne;
        // 사이드2
        sideTwoImg = binding.imgHomeSideTwo;
        // 김치
        kimchiImg = binding.imgHomeKimchi;
        // 단품
        singleFoodImg = binding.imgHomeSingle;

        // 화면에서 사용하는 버튼
        // 식사 종류 버튼 - 아침 점심 저녁
        MaterialAutoCompleteTextView autoTextTimes
                = (MaterialAutoCompleteTextView) binding.autotextHomeTime;
        String [] dietTimesList = {"아침", "점심", "저녁"};
        autoTextTimes.setSimpleItems(dietTimesList);
        // 식단 관리 목표 버튼 - 다이어트, 유지, 벌크업
        MaterialAutoCompleteTextView autoTextGoal
                = (MaterialAutoCompleteTextView) binding.autotextHomeGoal;
        String [] timesList = {"감소", "유지", "증가"};
        autoTextGoal.setSimpleItems(timesList);
        // 식판 혹은 단품 종류
        MaterialAutoCompleteTextView autoTextKind
                = (MaterialAutoCompleteTextView) binding.autotextHomeKind;
        String [] kindList = {"식판", "단품"};
        autoTextKind.setSimpleItems(kindList);

        recoTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = timeInput.getEditText().getText().toString().trim();
                String goal = goalInput.getEditText().getText().toString().trim();
                String kind = kindInput.getEditText().getText().toString().trim();

                // 식판 레이아웃
                trayLayout = (LinearLayout) binding.layoutHomeTray;
                singleLayout = (LinearLayout) binding.layoutHomeSingle;

                if (time.equals("") || goal.equals("") || kind.equals("")) {
                    noneLayout.setVisibility(View.VISIBLE);
                    trayLayout.setVisibility(View.GONE);
                    singleLayout.setVisibility(View.GONE);
                } else {
                    loadingRecoFragment.show(
                            getChildFragmentManager(), LoadingRecoFragment.LoadingTAG);
                    loadingRecoFragment.setCancelable(false);
                    callApiFunc(time, goal, kind);
                }
            }
        });

        recoResultLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if (noneLayout.getVisibility() == View.VISIBLE) {
                    return;
                } else {
                    // 프래그먼트 - 활동 설문 페이지
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    RecolistFragment recolistFragment = new RecolistFragment();
                    recolistFragment.show(fragmentManager, recolistFragment.RecolistTag);
                }
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // 칼로리 기준에 맞게 하트 기록
    private void callKcalHeart() {
        ApiCallService service = ApiCallServiceProvider.provide();
        Call<UserInfoRes> call = service.getUserInfo();
        call.enqueue(new Callback<UserInfoRes>() {
            @Override
            public void onResponse(Call<UserInfoRes> call, Response<UserInfoRes> response) {
                if(response.isSuccessful()) {
                    UserInfoRes res = response.body();
                    rcmd = res.getRecommendedCalorie();

                    realKcal.setText((int)real + " kcal");
                    rcmdKcal.setText((int)rcmd + " kcal");
                    int range = (int) (real/rcmd * 100.0);
                    Log.d("heart", String.valueOf(range));
                    if(range <= 20) {
                        heart.setImageDrawable(getResources().getDrawable(R.drawable.main_h1, null));
                    } else if (range <= 40) {
                        heart.setImageDrawable(getResources().getDrawable(R.drawable.main_h2, null));
                    } else if (range <= 60) {
                        heart.setImageDrawable(getResources().getDrawable(R.drawable.main_h3, null));
                    } else if (range <= 80) {
                        heart.setImageDrawable(getResources().getDrawable(R.drawable.main_h4, null));
                    } else {
                        heart.setImageDrawable(getResources().getDrawable(R.drawable.main_h5, null));
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoRes> call, Throwable t) {
                Log.e("Home UserInfo(권장칼로리) 실패", "fail");
                todayCnt += 1;
            }
        });

    }

    // 오늘의 식단기록 입력
    private void callTodayLog(MealTime type) {
        ApiCallService service = ApiCallServiceProvider.provide();

        Call<SpecificMealInfoRes> call = service.searchMealPlanner(axiostoday, type);
        call.enqueue(new Callback<SpecificMealInfoRes>() {
            @Override
            public void onResponse(Call<SpecificMealInfoRes> call, Response<SpecificMealInfoRes> response) {
                if (response.isSuccessful()) { // 값 받았을때 -> 밥 먹었을때
                    SpecificMealInfoRes res = response.body();
                    MealStatistics log = res.getAnalysisResult();
                    if(res.getDate() != axiostoday) {
                        Log.e("Home 오늘기록 axios error", "날짜 불일치");
                    }
                    int realKcal = log.getRealCalorie();
                    real += realKcal;
                    String carbo = "탄수화물 "+log.getRealCarbohydrate() + "g, ";
                    String protein = "단백질 "+log.getRealProtein() + "g, ";
                    String fat = "지방 "+log.getRealFat()+ "g";
                    StringBuilder sb = new StringBuilder(carbo + protein + fat);
                    switch (res.getMealTime()) {
                        case breakfast:
                            String breakfast = "아침  |  "+realKcal+"kcal";
                            textBreakfastKcal.setText(breakfast);
                            textBreakfastCpf.setText(sb.toString());
                            break;
                        case lunch:
                            String lunch = "점심  |  "+realKcal+"kcal";
                            textLunchKcal.setText(lunch);
                            textLunchCpf.setText(sb.toString());
                            break;
                        case dinner:
                            String dinner = "저녁  |  "+realKcal+"kcal";
                            textDinnerKcal.setText(dinner);
                            textDinnerCpf.setText(sb.toString());
                            break;
                    }
                } else {
                    setNullMealLayout(type);
                }
                todayCnt += 1;
                if(todayCnt == 3) {
                    callKcalHeart();
                }
            }

            @Override
            public void onFailure(Call<SpecificMealInfoRes> call, Throwable t) {
                Log.e("Home 식사 불러오기 실패", "fail");
            }
        });
    }

    // 밥 안 먹었을때
    private void setNullMealLayout(MealTime type) {
        String msg = "아직 입력된 식단이 없습니다.";
        int color = getResources().getColor(R.color.back_gray);
        switch (type) {
            case breakfast:
                textBreakfastKcal.setText(msg);
                textBreakfastKcal.setTextColor(color);
                textBreakfastKcal.setTextSize(16);
                break;
            case lunch:
                textLunchKcal.setText(msg);
                textLunchKcal.setTextColor(color);
                textLunchKcal.setTextSize(16);
                break;
            case dinner:
                textDinnerKcal.setText(msg);
                textDinnerKcal.setTextColor(color);
                textDinnerKcal.setTextSize(16);
                break;
        }
    }

    // 오늘 날짜+요일 불러오기
    private String getToday() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String today = dateFormat.format(date);
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd"); // axios로 보낼 date 형태
        axiostoday = format2.format(new Date(System.currentTimeMillis()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayNo = calendar.get(calendar.DAY_OF_WEEK);
        String[] day = {"일", "월", "화", "수", "목", "금", "토"};

        return today+" ("+day[dayNo-1]+")";
    }

    /**
     * 식단 추천 api 부르는 코드
     * @param time
     * @param goal
     * @param kind
     */
    private void callApiFunc(String time, String goal, String kind) {
        MealTime mealTime = null;
        UserDesiredStatus userDesiredStatus = null;
        MealType mealType = null;

        switch (time) {
            case "아침" : {
                mealTime = MealTime.breakfast;
                break;
            }
            case "점심" : {
                mealTime = MealTime.lunch;
                break;
            }
            case "저녁" : {
                mealTime = MealTime.dinner;
                break;
            }
        }

        switch (goal) {
            case "감소": {
                userDesiredStatus = UserDesiredStatus.decrease;
                break;
            }
            case "유지": {
                userDesiredStatus = UserDesiredStatus.maintain;
                break;
            }
            case "증가": {
                userDesiredStatus = UserDesiredStatus.increase;
                break;
            }
        }

        switch (kind) {
            case "식판": {
                mealType = MealType.combo;
                break;
            }
            case "단품": {
                mealType = MealType.single;
                break;
            }
        }

        /**
         * 식단 추천을 위한 조건 3개 - 언제 먹었는지, 목표는 뭔지, 세트로 먹을 것인지 단품으로 먹을 것인지
         * 추천받은 음식을 화면에 뿌려줄 이미지 뷰
         * 해당 이미지 뷰가 있는 레이아웃
         * 지금 여기 프래그먼트, 다이얼로그에 추천받은 음식 데이터를 보내주기 위한 뷰 모델
         */
        GetRecommendationMealApiCall.call(mealTime, userDesiredStatus, mealType,
                singleFoodImg, mainFoodImg, soupImg, sideOneImg, sideTwoImg, kimchiImg,
                noneLayout, trayLayout, singleLayout, loadingRecoFragment, this, homeViewModel);
    }
}