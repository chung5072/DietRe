package com.ssafy.dietre.ui.week;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.ssafy.dietre.MainActivity;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.ApiCallService;
import com.ssafy.dietre.api.data.DailyStatistics;
import com.ssafy.dietre.api.response.WeeklyAnalysisRes;
import com.ssafy.dietre.api.response.WeeklyStatistics;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.databinding.FragmentWeekBinding;
import com.ssafy.dietre.ui.PagerAdapter;
import com.ssafy.dietre.ui.user.UserFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeekFragment extends Fragment {

    private FragmentWeekBinding binding;
    public WeekViewModel weekViewModel;

    private static LinearLayout layout;
    private static NestedScrollView scroll;
    private static HorizontalBarChart scoreBar;
    private ArrayList<Integer> kcal = new ArrayList<>();
    private ArrayList<Integer> carbo = new ArrayList<>();
    private ArrayList<Integer> protein = new ArrayList<>();
    private ArrayList<Integer> fat = new ArrayList<>();
    private String period;
    private int dayCnt = 0;
    private int score = 0;
    private int percent = 0;
    private String[] day = {"월", "화", "수", "목", "금", "토", "일"};

    private int page = 4;

    public static WeekFragment newInstance() {
        return new WeekFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        weekViewModel = new ViewModelProvider(requireActivity()).get(WeekViewModel.class);

        binding = FragmentWeekBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        scroll = (NestedScrollView) binding.layoutScroll;
        layout = (LinearLayout) binding.layoutContent;
        TextView date = (TextView) binding.titleDate;
        ViewPager2 pager = (ViewPager2) binding.viewpagerWeek;
        CircleIndicator3 indicator = (CircleIndicator3) binding.indicator;
        TextView textScore = (TextView) binding.textScore;
        scoreBar = (HorizontalBarChart) binding.scoreBar;
        TextView textPercent = (TextView) binding.textPercent;
        Button end = (Button) binding.btnEnd;

        // axios 호출로 weekItem에 값 세팅
        ApiCallService service = ApiCallServiceProvider.provide();
        Call<WeeklyAnalysisRes> call = service.getWeeklyAnalysis();
        call.enqueue(new Callback<WeeklyAnalysisRes>() {
            @Override
            public void onResponse(Call<WeeklyAnalysisRes> call, Response<WeeklyAnalysisRes> response) {
                if(response.isSuccessful()) {
                    WeeklyStatistics weekStats = response.body().getWeeklyStatistics();
                    List<DailyStatistics> dailyStats = response.body().getDailyStatisticsList();
                    score = weekStats.getScore();
                    percent = weekStats.getRelativeScore();
                    // 일별 합계 칼로리, 탄단지가 필요함!!!
                    dayCnt = dailyStats.size(); // 이번주 5, 7일 여부
                    period = dailyStats.get(0).getDate().replace("-", ".")+" ~ "+dailyStats.get(dayCnt-1).getDate().replace("-", ".");
                    // get(0) 는 추천량
                    kcal.add(weekStats.getRecommendedCalorie());
                    carbo.add(weekStats.getRecommendedCarbohydrate());
                    protein.add(weekStats.getRecommendedProtein());
                    fat.add(weekStats.getRecommendedFat());
                    // 날짜만큼 반복문 돌면서 arraylist 채우기
                    for(int i=0; i<dayCnt; i++) {
                        kcal.add(dailyStats.get(i).getRealCalorie());
                        carbo.add(dailyStats.get(i).getRealCarbohydrate());
                        protein.add(dailyStats.get(i).getRealProtein());
                        fat.add(dailyStats.get(i).getRealFat());
                    }
                    // 모두 채우고 나면 ViewModel에 세팅
                    weekViewModel.setWeek(kcal, carbo, protein, fat, Arrays.copyOfRange(day, 0, dayCnt));

                    // 페이지 구성
                    date.setText(period); // 날짜 세팅
                    // TODO 일주일치 kcal, carbo, protein, fat 데이터로 차트 그리기
                    PagerAdapter adapter = new PagerAdapter(getActivity(), page);
                    pager.setAdapter(adapter);
                    indicator.setViewPager(pager);
                    indicator.createIndicators(page, 0); // 0페이지부터 indicator표기
                    pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL); // 가로 슬라이드

                    // TODO 표그리기: 데이터 개수만큼 행 생성해서 넣기
                    TableLayout table = (TableLayout) binding.tableWeek;
                    for(int i=1; i<weekViewModel.getWeekLength()+1; i++) {
                        TableRow row = new TableRow(getActivity());
                        row.setLayoutParams(new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                        ));
                        setRowText(weekViewModel, row, i);
                        table.addView(row);
                    }

                    // TODO 점수 표시하기: 점수 표기, 그래프 그리기, %별로 string 불러와서 setText
                    StringBuilder sb = new StringBuilder();
                    textScore.setText(score+"점");
                    setScoreBar(score);
                    sb.append("상위 "+percent+"%");
                    if(percent/10 <= 10) {
                        sb.append(getResources().getString(R.string.text_up10));
                    } else if(percent/10 <= 30) {
                        sb.append(getResources().getString(R.string.text_up30));
                    } else if(percent/10 <= 50) {
                        sb.append(getResources().getString(R.string.text_up50));
                    } else {
                        sb.append(getResources().getString(R.string.text_up100));
                    }
                    textPercent.setText(sb.toString());

                    // 확인 누르면 이전 페이지로 뒤로가기
                    end.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //getActivity().getSupportFragmentManager().beginTransaction().remove(newInstance()).commit();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    });

                } else {
                    if(response.code() != 200) { // 오는 값 없음 -> view reset하고 데이터 없다고 보여줌
                        scroll.removeAllViewsInLayout();
                        scroll.setBackgroundColor(getResources().getColor(R.color.main));
                        TextView text = new TextView(getActivity());
                        text.setText("지난주 저장된 식단이 없습니다.");
                        text.setGravity(Gravity.CENTER);
                        text.setTextSize(30);
                        text.setTextColor(getResources().getColor(R.color.textPrimary));

                        text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        scroll.addView(text);
                    }
                }
            }

            @Override
            public void onFailure(Call<WeeklyAnalysisRes> call, Throwable t) {
                Log.e("Week api call 실패", t.toString());
            }
        });

        return root;
    }

    private void setRowText(WeekViewModel weekViewModel, TableRow row, int d) {
        TextView day = new TextView(getActivity());
        day.setText(weekViewModel.getWeekDay()[d-1]);
        setTableTextTheme(day);
        TextView kcal = new TextView(getActivity());
        kcal.setText(weekViewModel.getKcalArr().get(d).toString());
        setTableTextTheme(kcal);
        TextView carbo = new TextView(getActivity());
        carbo.setText(weekViewModel.getCarboArr().get(d).toString());
        setTableTextTheme(carbo);
        TextView protein = new TextView(getActivity());
        protein.setText(weekViewModel.getProteinArr().get(d).toString());
        setTableTextTheme(protein);
        TextView fat = new TextView(getActivity());
        fat.setText(weekViewModel.getFatArr().get(d).toString());
        setTableTextTheme(fat);

        row.addView(day);
        row.addView(kcal);
        row.addView(carbo);
        row.addView(protein);
        row.addView(fat);

    }

    public void setTableTextTheme(TextView text) {
        text.setGravity(Gravity.CENTER);
        text.setTextSize(20);
        text.setBackgroundColor(getResources().getColor(R.color.resolve_back));
        text.setPadding(0, 5, 0, 5);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // score horizontal bar차트 생성
    private void setScoreBar(int score) {
        // 기본 그래프 설정
        scoreBar.getDescription().setEnabled(false); // 설명 X
        scoreBar.setTouchEnabled(false); // 터치 X
        scoreBar.getLegend().setEnabled(false);// 범례 X
        scoreBar.setExtraOffsets(0, -5, 0, -5);
        XAxis xaxis = scoreBar.getXAxis(); // 행(배경이 되는 기준 그래프)
        xaxis.setDrawAxisLine(false);
        xaxis.setGranularity(1f);
        xaxis.setDrawLabels(false);
        xaxis.setGridLineWidth(25f); // 굵기
        xaxis.setGridColor(getResources().getColor(R.color.resolve)); // 그래프 색상

        YAxis leftaxis = scoreBar.getAxisLeft(); // 그래프 상단에 표기됨
        leftaxis.setDrawAxisLine(false);
        leftaxis.setDrawGridLines(false);
        leftaxis.setDrawLabels(false); // 범위 볼 수 있음(숫자만)
        leftaxis.setAxisMinimum(0f); // 시작값
        leftaxis.setAxisMaximum(100f); // 최댓값(그래프 끝!)
        leftaxis.setGranularity(1f);
        YAxis rightaxis = scoreBar.getAxisRight(); // 그래프 하단에 표기됨
        rightaxis.setDrawLabels(false);
        rightaxis.setDrawGridLines(false);
        rightaxis.setDrawAxisLine(false);

        // 그래프 데이터 설정(values에 시작값, 끝값 넣으면 됨 -> 0~섭취량으로 그려짐)
        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(0f, score));
        BarDataSet bar = new BarDataSet(values, "kcal bar");
        bar.setDrawIcons(false);
        bar.setColor(getResources().getColor(R.color.resolve_acc));

        // 그래프 그리기(데이터 기반)
        BarData data = new BarData(bar);
        data.setBarWidth(0.9f);

        data.setDrawValues(false);

        scoreBar.setData(data); // 데이터 넣고 그려줌
        scoreBar.invalidate(); // 그래프 갱신

    }
}