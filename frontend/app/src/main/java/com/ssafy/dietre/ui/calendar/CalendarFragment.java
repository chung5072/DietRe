package com.ssafy.dietre.ui.calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.ssafy.dietre.MainActivity;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.ApiCallService;
import com.ssafy.dietre.api.GetAnalysisApiCall;
import com.ssafy.dietre.api.GetDailyMealNutApiCall;
import com.ssafy.dietre.api.GetMonthlyMealRecordApiCall;
import com.ssafy.dietre.api.TestApiCall;
import com.ssafy.dietre.api.TestApiCallTwo;
import com.ssafy.dietre.api.data.MealStatistics;
import com.ssafy.dietre.api.response.MonthlyMealRecordRes;
import com.ssafy.dietre.api.response.SpecificMealInfoRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.type.MealTime;
import com.ssafy.dietre.databinding.FragmentCalendarBinding;
import com.ssafy.dietre.ui.gesture.OnSwipeTouchListener;

import org.w3c.dom.Text;
import org.xml.sax.helpers.AttributesImpl;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment implements ClickDate {
    
    StringBuilder sb;
    
    private FragmentCalendarBinding binding;
    CalendarFragment calendarFragment;
    
    // 연, 월 텍스트 뷰
    TextView textCalendarMonth;
    // 하트 별 갯수 텍스트뷰
    TextView lowHeartCnt;
    TextView enoughHeartCnt;
    TextView tooMuchHeartCnt;

    // 데이터가 없는 레이아웃
    LinearLayout noDataLayout;
    LinearLayout hasDataLayout;
    // 버튼 레이아웃
    // 아침
    CoordinatorLayout breakfastLayout;
    ImageView breakfastImg;
    // 점심
    CoordinatorLayout lunchLayout;
    ImageView lunchImg;
    // 저녁
    CoordinatorLayout dinnerLayout;
    ImageView dinnerImg;
    // 날짜보여주는 뷰
    RecyclerView recyclerView;
    // 아래 바텀시트
    FrameLayout bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;

    // 날짜
    private int mYear, mMonth, mDay;
    // 달력
    Calendar cal = Calendar.getInstance();
    // 색이 들어간 배열
    private static int[] cpfColor;
    
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CalendarViewModel dashboardViewModel =
                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 현재 날짜
        CalendarUtil.selectedDate = LocalDate.now();
        // 색상
        cpfColor = new int[]{
                getResources().getColor(R.color.carbo), 
                getResources().getColor(R.color.protein), 
                getResources().getColor(R.color.fat)
        };

        calendarFragment = this;
        // 달력 윗부분 하트 - 적게 먹은 경우
        lowHeartCnt = binding.textCalendarLowcount;
        // 달력 윗부분 하트 - 적절히 먹은 경우
        enoughHeartCnt = binding.textCalendarPropercount;
        // 달력 윗부분 하트 - 너무 많이 먹은 경우
        tooMuchHeartCnt = binding.textCalendarToomuchcount;
        
        // bottom sheet
        bottomSheet = binding.layoutCalendarBottomsheet;
        // 식단 입력 안됐다는 문구
        noDataLayout = binding.layoutCalendarDailyNoData;
        // 데이터 있을시 보이는 파이차트 및 영양소 정보
        hasDataLayout = binding.layoutCalendarDailyHasData;
        // 한 끼 분석 화면으로 이동하는 버튼
        // 아침 레이아웃
        breakfastLayout = binding.layoutCalendarBreakfast;
        // 아침 이미지
        breakfastImg = binding.imgCalendarBreakfast;
        // 점심 레이아웃
        lunchLayout = binding.layoutCalendarLunch;
        // 점심 이미지
        lunchImg = binding.imgCalendarLunch;
        // 저녁 레이아웃
        dinnerLayout = binding.layoutCalendarDinner;
        // 저녁 이미지
        dinnerImg = binding.imgCalendarDinner;

        // 달력 초기화
        textCalendarMonth = (TextView) binding.textCalendarMonth;
        recyclerView = (RecyclerView) binding.recycleCalendarDate;

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setFitToContents(false);
        bottomSheetBehavior.setHalfExpandedRatio(0.65f);

        makeCalendar(root);
        
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * 달력 생성
     * @param root
     */
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void makeCalendar(View root) {
        // 달력을 스와이프하면 전 달, 다음 달을 보여줌
        recyclerView.setOnTouchListener(new OnSwipeTouchListener(this.getContext()) {
            @SuppressLint("ClickableViewAccessibility")
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onSwipeLeft() {
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.plusMonths(1);
                LocalDate lastDayOfMonth = getLastDayOfMonth();
                GetMonthlyMealRecordApiCall.call(lastDayOfMonth, binding, calendarFragment,
                        lowHeartCnt, enoughHeartCnt, tooMuchHeartCnt);
            }

            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeRight() {
                CalendarUtil.selectedDate = CalendarUtil.selectedDate.minusMonths(1);
                LocalDate lastDayOfMonth = getLastDayOfMonth();
                GetMonthlyMealRecordApiCall.call(lastDayOfMonth, binding, calendarFragment,
                        lowHeartCnt, enoughHeartCnt, tooMuchHeartCnt);
            }
        });

        // 달력의 헤더인 월을 클릭하면 datepicker를 띄워서 해당 달로 변경함
        textCalendarMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                // 1. 데이터 피커 띄움
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DietReCalendarSpinner,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // 선택한 연도-월-일로 변경함
                                CalendarUtil.selectedDate = CalendarUtil.selectedDate.withYear(year);
                                CalendarUtil.selectedDate = CalendarUtil.selectedDate.withMonth(monthOfYear + 1);

                                LocalDate lastDayOfMonth = getLastDayOfMonth();
                                GetMonthlyMealRecordApiCall.call(
                                        lastDayOfMonth, binding, calendarFragment,
                                        lowHeartCnt, enoughHeartCnt, tooMuchHeartCnt);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // 화면 설정
        GetMonthlyMealRecordApiCall.call(CalendarUtil.selectedDate, binding, this,
                lowHeartCnt, enoughHeartCnt, tooMuchHeartCnt);
    }

    /**
     * 달력 윗 부분의 헤더 형식
     * 월 연도 순서
     *
     * @param selectedDate
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String monthYearFromDate(LocalDate selectedDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월");
        return selectedDate.format(formatter);
    }

    /**
     * 화면 설정 - 달력
     */
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setMonthView(View view, List<MonthlyMealRecordRes> recordList) {
        // 월-년 텍스트뷰 세팅
        textCalendarMonth.setText(monthYearFromDate(CalendarUtil.selectedDate));
        // 해당 월 날짜 가져오기
        ArrayList<LocalDate> dayList = daysInMonthArray(CalendarUtil.selectedDate);

        // 어댑터 데이터 적용
        CalendarAdapter calendarAdapter = new CalendarAdapter(dayList, recordList, this);

        // 레이아웃 설정
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity().getApplicationContext(), 7);

        // 레아아웃 적용
        recyclerView.setLayoutManager(manager);

        // 어댑터 적용
        recyclerView.setAdapter(calendarAdapter);

        // bottom sheet
        FrameLayout calendarBottom = (FrameLayout) binding.layoutCalendarBottomsheet;
        calendarBottom.setOnTouchListener(new OnSwipeTouchListener(calendarBottom.getContext()) {
            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeLeft() {
                calendarAdapter.swipe(true);
            }

            @SuppressLint("ClickableViewAccessibility")
            public void onSwipeRight() {
                calendarAdapter.swipe(false);
            }
        });
    }

    /**
     * 날짜 생성
     *
     * @param selectedDate
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<LocalDate> daysInMonthArray(LocalDate selectedDate) {
        ArrayList<LocalDate> dayList = new ArrayList<>();

        YearMonth yearMonth = YearMonth.from(selectedDate);

        // 해당 월 마지막 날짜 가져오기
        int lastDay = yearMonth.lengthOfMonth();

        // 해당 월의 첫째날 가져오기
        LocalDate firstDay = selectedDate.withDayOfMonth(1);

        // 첫번째 날의 요일 가져오기
        int dayOfWeek = firstDay.getDayOfWeek().getValue();

        // 날짜 생성
        for (int i = 1; i < 42; i++) {
            if (i <= dayOfWeek || i > lastDay + dayOfWeek) {
                dayList.add(null);
            } else {
                dayList.add(LocalDate.of(selectedDate.getYear(), selectedDate.getMonth(), i - dayOfWeek));
            }
        }

        return dayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void isClicked(boolean click, LocalDate day) {
        // 현재 레이아웃
        View root = binding.getRoot();

        bottomSheetBehavior.setDraggable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        // bottom sheet의 제목
        TextView haruTitle = (TextView) binding.titleHaru;

        int dayOfMonth = day.getDayOfMonth();

        Month monthFromDay = day.getMonth();
        int month = monthFromDay.getValue();

        int year = day.getYear();

        String date = getDate(year, month, dayOfMonth);

        sb = new StringBuilder();
        sb.append(year).append(".")
                .append(month).append(".")
                .append(dayOfMonth).append(" ")
                .append("(").append(date).append(")");

        haruTitle.setText(sb.toString());

        // bottom sheet 내용
        // 차트
        // 옆에 칼로리 내용
        TextView kcalText = (TextView) binding.textCalendarKcal;
        kcalText.setText("칼로리(kcal) " + "1479");
        // 옆에 탄수화물 내용
        TextView carbText = (TextView) binding.textCalendarCarb;
        carbText.setText("탄수화물 " + "184");
        // 옆에 단백질 내용
        TextView protText = (TextView) binding.textCalendarProt;
        protText.setText("단백질 " + "73");
        // 옆에 지방 내용
        TextView fatText = (TextView) binding.textCalendarFat;
        fatText.setText("지방 " + "49");

        // 클릭한 날짜의 정보를 보내서 그 날의 정보를 가져옴
        String sendDate;

        if (month / 10 == 0 && dayOfMonth / 10 == 0) {
            sendDate = year + "-0" + month + "-0" + dayOfMonth;
        }
        else if (month / 10 == 0 && dayOfMonth / 10 != 0) {
            sendDate = year + "-0" + month + "-" + dayOfMonth;
        }
        else if (month / 10 != 0 && dayOfMonth / 10 == 0) {
            sendDate = year + "-" + month + "-0" + dayOfMonth;
        }
        else {
            sendDate = year + "-" + month + "-" + dayOfMonth;
        }

        GetDailyMealNutApiCall.call(sendDate, root,
                kcalText, carbText, protText, fatText,
                this);


        // 아래 버튼
        // 아침 버튼 이미지 변경
        callForCheckKcal(sendDate, MealTime.breakfast);
        callForCheckKcal(sendDate, MealTime.lunch);
        callForCheckKcal(sendDate, MealTime.dinner);

        breakfastLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 한 끼 분석화면으로 이동
                GetAnalysisApiCall.call(sendDate, MealTime.breakfast, (MainActivity) getActivity());
            }
        });
        lunchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 한 끼 분석화면으로 이동
                GetAnalysisApiCall.call(sendDate, MealTime.lunch, (MainActivity) getActivity());
            }
        });
        dinnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 한 끼 분석화면으로 이동
                GetAnalysisApiCall.call(sendDate, MealTime.dinner, (MainActivity) getActivity());
            }
        });
    }

    /**
     * 무슨 요일인지 구하는 함수
     * @param year
     * @param month
     * @param dayOfMonth
     * @return
     */
    private String getDate(int year, int month, int dayOfMonth) {
        int preYear = year - 1;
        int numOfDays = preYear*365 + (preYear/4 - preYear/100 + preYear/400);

        int[] monthArray = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for (int a = 0; a < month - 1; a++ ) {
            numOfDays += monthArray[a];
        }

        if (month >= 3 &&
                (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)) {
            numOfDays++;
        }

        numOfDays += dayOfMonth;

        int dayOfWeek = numOfDays % 7;

        String[] dateArray = {"일", "월", "화", "수", "목", "금", "토"};

        return dateArray[dayOfWeek];
    }

    /**
     * 가장 끝의 날짜에 도달했을 경우, 스와이프를 했을 때 bottom sheet를 내림
     * TODO 이왕이면 다음 달로 넘어가는 것이 좋긴 함
     */
    @Override
    public void isEdgeDate() {
        BottomSheetBehavior<FrameLayout> layoutCalendarBottomsheet = BottomSheetBehavior.from(binding.layoutCalendarBottomsheet);
        layoutCalendarBottomsheet.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    /**
     * bottom sheet의 차트를 띄움
     * @param v
     */
    public void makePieChart(View v, float carbPer, float protPer, float fatPer) {
        PieChart pieChart = v.findViewById(R.id.day_pie); // 월간기록 하루 파이차트
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(-5, -5, -5, -5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawEntryLabels(false);
        pieChart.getLegend().setEnabled(false);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(carbPer, "탄수화물"));
        yValues.add(new PieEntry(protPer, "지방"));
        yValues.add(new PieEntry(fatPer, "단백질"));

        pieChart.animateY(1000, Easing.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(yValues, "탄단지");
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(cpfColor);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(14);
        data.setValueTextColor(getResources().getColor(R.color.textPrimary));
        pieChart.setData(data);
        //pieChart.invalidate();
        pieChart.setTouchEnabled(false);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value) + "%";
            }
        });
    }

    // 해당 식사의 칼로리를 판단해서 버튼의 색상을 변경
    private void callForCheckKcal(String date, MealTime time) {
        ApiCallService service = ApiCallServiceProvider.provide();

        Call<SpecificMealInfoRes> call = service.searchMealPlanner(date, time);
        call.enqueue(new Callback<SpecificMealInfoRes>() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onResponse(Call<SpecificMealInfoRes> call, Response<SpecificMealInfoRes> response) {
                if (response.isSuccessful()) { // 값 받았을때 -> 밥 먹었을때
                    SpecificMealInfoRes res = response.body();
                    MealStatistics log = res.getAnalysisResult();
                    Log.e("프래그먼트 데이터 확인", res.getMealTime()+" - "+log.getRealCalorie() + ", " + log.getRecommendedCalorie());

                    int diff = log.getRealCalorie() - log.getRecommendedCalorie();

                    switch (time) {
                        case breakfast: {
                            breakfastLayout.setVisibility(View.VISIBLE);
                            if (diff < -100) {
                                // 추천보다 실제 먹은 칼로리가 적은 경우 == 너무 적게 먹은 경우
                                breakfastImg.setBackgroundResource(R.drawable.empty);
                                breakfastLayout.setClickable(true);
                            } else if (diff < 100) {
                                // 추천과 실제 먹은 칼로리의 차이가 적은 경우 == 적절히 먹은 경우
                                breakfastImg.setBackgroundResource(R.drawable.full);
                                breakfastLayout.setClickable(true);
                            } else {
                                // 추천과 실제 먹은 칼로리의 차이가 너무 많은 경우 == 너무 많이 먹은 경우
                                breakfastImg.setBackgroundResource(R.drawable.broken);
                                breakfastLayout.setClickable(true);
                            }
                            break;
                        }
                        case lunch: {
                            lunchLayout.setVisibility(View.VISIBLE);
                            if (diff < -100) {
                                // 추천보다 실제 먹은 칼로리가 적은 경우 == 너무 적게 먹은 경우
                                lunchImg.setBackgroundResource(R.drawable.empty);
                                lunchLayout.setClickable(true);
                            } else if (diff < 100) {
                                // 추천과 실제 먹은 칼로리의 차이가 적은 경우 == 적절히 먹은 경우
                                lunchImg.setBackgroundResource(R.drawable.full);
                                lunchLayout.setClickable(true);
                            } else {
                                // 추천과 실제 먹은 칼로리의 차이가 너무 많은 경우 == 너무 많이 먹은 경우
                                lunchImg.setBackgroundResource(R.drawable.broken);
                                lunchLayout.setClickable(true);
                            }
                            break;
                        }
                        case dinner: {
                            dinnerLayout.setVisibility(View.VISIBLE);
                            if (diff < -100) {
                                // 추천보다 실제 먹은 칼로리가 적은 경우 == 너무 적게 먹은 경우
                                dinnerImg.setBackgroundResource(R.drawable.empty);
                                dinnerLayout.setClickable(true);
                            } else if (diff < 100) {
                                // 추천과 실제 먹은 칼로리의 차이가 적은 경우 == 적절히 먹은 경우
                                dinnerImg.setBackgroundResource(R.drawable.full);
                                dinnerLayout.setClickable(true);
                            } else {
                                // 추천과 실제 먹은 칼로리의 차이가 너무 많은 경우 == 너무 많이 먹은 경우
                                dinnerImg.setBackgroundResource(R.drawable.broken);
                                dinnerLayout.setClickable(true);
                            }
                            break;
                        }
                    }
                } else {
                    switch (time) {
                        case breakfast: {
                            breakfastLayout.setVisibility(View.INVISIBLE);
                            breakfastLayout.setClickable(false);
                            break;
                        }
                        case lunch: {
                            lunchLayout.setVisibility(View.INVISIBLE);
                            lunchLayout.setClickable(false);
                            break;
                        }
                        case dinner: {
                            dinnerLayout.setVisibility(View.INVISIBLE);
                            dinnerLayout.setClickable(false);
                            break;
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<SpecificMealInfoRes> call, Throwable t) {
                Log.e("Home 식사 불러오기 실패", "fail");
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDate getLastDayOfMonth() {
        int year = CalendarUtil.selectedDate.getYear();
        int month = CalendarUtil.selectedDate.getMonth().getValue();
        int day = CalendarUtil.selectedDate.getDayOfMonth();

        cal.set(year,month-1,day);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        String beforeParse = "";
        if (month / 10 == 0 && day / 10 == 0) {
            beforeParse = year + "-0" + month + "-0" + lastDay;
        }
        else if (month / 10 == 0 && day / 10 != 0) {
            beforeParse = year + "-0" + month + "-" + lastDay;
        }
        else if (month / 10 != 0 && day / 10 == 0) {
            beforeParse = year + "-" + month + "-0" + lastDay;
        }
        else {
            beforeParse = year + "-" + month + "-" + lastDay;
        }

        LocalDate requireDate = LocalDate.parse(beforeParse, DateTimeFormatter.ISO_DATE);

        return requireDate;
    }
}
