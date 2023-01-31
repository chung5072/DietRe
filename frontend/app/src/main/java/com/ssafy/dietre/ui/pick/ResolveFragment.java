package com.ssafy.dietre.ui.pick;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.ssafy.dietre.R;
import com.ssafy.dietre.api.ApiCallService;
import com.ssafy.dietre.api.data.FoodData;
import com.ssafy.dietre.api.data.MealStatistics;
import com.ssafy.dietre.api.response.MealSearchRes;
import com.ssafy.dietre.api.response.SpecificMealInfoRes;
import com.ssafy.dietre.common.provider.ApiCallServiceProvider;
import com.ssafy.dietre.common.type.MealTime;
import com.ssafy.dietre.databinding.FragmentResolveBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResolveFragment extends Fragment {

    private static String today;
    private FragmentResolveBinding binding;
    private static HorizontalBarChart kcal_bar;
    private static int[] cpfColor;
    private static ArrayList<FoodItem> list;
    private FoodItemAdapter adapter;
    private RecyclerView foodListView;

    public LoadingFragment loading;
    ImageView img;
    String imgUrl;


    public ResolveFragment() {
        // Required empty public constructor
    }

    public static ResolveFragment getInstance(String date, MealTime meal) {
        ResolveFragment resolve = new ResolveFragment();
        Bundle args = new Bundle();
        args.putString("date", date);
        args.putSerializable("MealTime", meal);
        resolve.setArguments(args);
        date = date.replace("-", ".");
        if (meal == MealTime.breakfast) {
            today = date + " " + "아침";
        } else if (meal == MealTime.lunch) {
            today = date + " " + "점심";
        } else {
            today = date + " " + "저녁";
        }
        return resolve;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentResolveBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        loading = LoadingFragment.newInstance("resolve");

        TextView title = (TextView) binding.textToday;
        title.setText(today);
        img = (ImageView) binding.imgMeal;
        foodListView = (RecyclerView) binding.listMenu;

        kcal_bar = (HorizontalBarChart) binding.kcalBar; // kcal 바차트
        cpfColor = new int[]{getResources().getColor(R.color.carbo), getResources().getColor(R.color.protein), getResources().getColor(R.color.fat)};
        HorizontalBarChart rcmd_bar = (HorizontalBarChart) binding.rcmdStackbar; // cpf 바차트 - 권장
        HorizontalBarChart real_bar = (HorizontalBarChart) binding.realStackbar; // cpf 바차트 - 섭취

        // 탄단지 text
        TextView carbo = (TextView) binding.circleCarbo;
        TextView protein = (TextView) binding.circleProtein;
        TextView fat = (TextView) binding.circleFat;
        // 배경색 지정
        GradientDrawable drawable = (GradientDrawable) carbo.getBackground();
        drawable.setColor(getResources().getColor(R.color.carbo));
        carbo.setBackground(drawable);
        drawable = (GradientDrawable) protein.getBackground();
        drawable.setColor(getResources().getColor(R.color.protein));
        protein.setBackground(drawable);
        drawable = (GradientDrawable) fat.getBackground();
        drawable.setColor(getResources().getColor(R.color.fat));
        fat.setBackground(drawable);

        if (getArguments() != null) {
            String axiostoday = getArguments().getString("date");
            MealTime type = (MealTime) getArguments().getSerializable("MealTime");
            System.out.println("조회한 식단은 "+axiostoday+" "+type);

            if(imgUrl == null) {
                loading.show(getActivity().getSupportFragmentManager(), "loading dialog");
            }

            ApiCallService service = ApiCallServiceProvider.provide();
            Call<SpecificMealInfoRes> call = service.searchMealPlanner(axiostoday, type);
            call.enqueue(new Callback<SpecificMealInfoRes>() {
                @Override
                public void onResponse(Call<SpecificMealInfoRes> call, Response<SpecificMealInfoRes> response) {
                    if (response.isSuccessful()) {
                        SpecificMealInfoRes res = response.body();
                        List<FoodData> foodList = res.getFoodList();
                        MealStatistics ans = res.getAnalysisResult();
                        // 먹은 음식 사진
                        if (res.getMainImageUrl() != null) {
                            imgUrl = res.getMainImageUrl();
                            try {
                                checkpolicy();
                                URL url = new URL(imgUrl);
                                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                img.setImageBitmap(bitmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        list = new ArrayList<>();
                        for (FoodData food : foodList) {
                            String name = food.getName() + "  |  " + food.getCalorie().intValue() + "kcal";
                            String info = "탄수화물(g): " + food.getCarbohydrate().intValue() +
                                    ", 단백질(g): " + food.getProtein().intValue() +
                                    ", 지방(g): " + food.getFat().intValue();
                            list.add(new FoodItem(food.getId(), name, info));
                        }
                        adapter = new FoodItemAdapter(list, false, true);
                        foodListView.setAdapter(adapter);
                        // 리스트 불러오고 arraylist에 넣어서 layout에 세팅하기
                        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        foodListView.setLayoutManager(lm);
                        foodListView.setHasFixedSize(true);

                        // 분석결과
                        // 칼로리 비교 그래프
                        setKcalBar(ans.getRecommendedCalorie(), ans.getRealCalorie());
                        // 탄단지 stack graph(2개)
                        int realCpf = ans.getRealCarbohydrate() + ans.getRealProtein() + ans.getRealFat();
                        int rcmdCpf = ans.getRecommendedCarbohydrate() + ans.getRecommendedProtein() + ans.getRecommendedFat();
                        float rcmdCarbo = ans.getRecommendedCarbohydrate() / (float) rcmdCpf * 100;
                        float rcmdProtein = ans.getRecommendedProtein() / (float) rcmdCpf * 100;
                        float rcmdFat = ans.getRecommendedFat() / (float) rcmdCpf * 100;
                        setStackBar(rcmd_bar, rcmdCarbo, rcmdProtein, rcmdFat);
                        float realCarbo = ans.getRealCarbohydrate() / (float) realCpf * 100;
                        float realProtein = ans.getRealProtein() / (float) realCpf * 100;
                        float realFat = ans.getRealFat() / (float) realCpf * 100;
                        setStackBar(real_bar, realCarbo, realProtein, realFat);
                        // 탄단지 g CircleText
                        carbo.setText(ans.getRealCarbohydrate() + "g");
                        protein.setText(ans.getRealProtein() + "g");
                        fat.setText(ans.getRealFat() + "g");

                        if(imgUrl != null || res.getMainImageUrl() == null) {
                            loading.getDialog().dismiss();
                        }

                    }
                }

                @Override
                public void onFailure(Call<SpecificMealInfoRes> call, Throwable t) {
                    LinearLayout layout = (LinearLayout) binding.layoutContent;
                    layout.setVisibility(View.GONE);
                }
            });

        } else {
            Toast.makeText(getActivity(), "분석을 보기 위한 날짜와 종류가 없습니다.", Toast.LENGTH_SHORT);
        }


        return v;
    }

    private void checkpolicy() {
        if (Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private void setStackBar(HorizontalBarChart chart, float carbo, float protein, float fat) {
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawValueAboveBar(false);
        XAxis xaxis = chart.getXAxis();
        xaxis.setDrawLabels(false);
        xaxis.setDrawAxisLine(false);
        xaxis.setGranularity(1f);
        xaxis.setGridLineWidth(20f); // 굵기
        YAxis leftaxis = chart.getAxisLeft(); // 그래프 상단에 표기됨
        leftaxis.setDrawAxisLine(false);
        leftaxis.setDrawGridLines(false);
        leftaxis.setDrawLabels(false); // 범위 볼 수 있음(숫자만)
        leftaxis.setAxisMinimum(0f); // 시작값
        leftaxis.setAxisMaximum(100f); // 최댓값(그래프 끝!)
        leftaxis.setGranularity(1f);
        YAxis rightaxis = chart.getAxisRight(); // 그래프 하단에 표기됨
        rightaxis.setDrawLabels(false);
        rightaxis.setDrawGridLines(false);
        rightaxis.setDrawAxisLine(false);


        // 데이터 설정
        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(0, new float[]{carbo, protein, fat}));
        BarDataSet bar = new BarDataSet(values, "recommend cpf bar");
        bar.setColors(cpfColor); // 순서대로 색깔 적용
        BarData data = new BarData(bar);
        data.setValueTextColor(getResources().getColor(R.color.black));
        data.setValueTextSize(14);

        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value) + "%";
            }
        });

        // 데이터 세팅후 그리기
        chart.setData(data);
        data.setBarWidth(1f);
        chart.invalidate();


    }

    // 칼로리 horizontal bar차트 생성
    private void setKcalBar(int rcmd, int real) {
        // 기본 그래프 설정
        kcal_bar.getDescription().setEnabled(false); // 설명 X
        kcal_bar.setTouchEnabled(false); // 터치 X
        kcal_bar.getLegend().setEnabled(false);// 범례 X
        XAxis xaxis = kcal_bar.getXAxis(); // 행(배경이 되는 기준 그래프)
        xaxis.setDrawAxisLine(false);
        xaxis.setGranularity(1f);
        xaxis.setGridLineWidth(25f); // 굵기
        xaxis.setGridColor(getResources().getColor(R.color.resolve)); // 그래프 색상
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE); // 데이터 표시 위치(kcal)
        xaxis.setXOffset(300); // 강제로 그래프 오른쪽으로 옮김
        xaxis.setTextSize(15f);
        xaxis.setTextColor(getResources().getColor(R.color.resolve_back));
        xaxis.setValueFormatter(new ValueFormatter() { // 섭취 칼로리를 표기
            @Override
            public String getFormattedValue(float value) {
                return (String.valueOf(real)) + " kcal";
            }
        });
        YAxis leftaxis = kcal_bar.getAxisLeft(); // 그래프 상단에 표기됨
        leftaxis.setDrawAxisLine(false);
        leftaxis.setDrawGridLines(false);
        leftaxis.setDrawLabels(false); // 범위 볼 수 있음(숫자만)
        leftaxis.setAxisMinimum(0f); // 시작값
        leftaxis.setAxisMaximum(rcmd); // 최댓값(그래프 끝!)
        leftaxis.setGranularity(1f);
        YAxis rightaxis = kcal_bar.getAxisRight(); // 그래프 하단에 표기됨
        rightaxis.setDrawLabels(false);
        rightaxis.setDrawGridLines(false);
        rightaxis.setDrawAxisLine(false);

        // 그래프 데이터 설정(values에 시작값, 끝값 넣으면 됨 -> 0~섭취량으로 그려짐)
        ArrayList<BarEntry> values = new ArrayList<>();
        values.add(new BarEntry(0f, real));
        BarDataSet bar = new BarDataSet(values, "kcal bar");
        bar.setDrawIcons(false);
        if (rcmd - real > 100) { // 권장보다 적게 섭취, 글씨색도 검정으로 변경
            xaxis.setTextColor(getResources().getColor(R.color.black));
            bar.setColor(getResources().getColor(R.color.lack));
        } else if (rcmd - real < -100) { // 권장보다 많이 섭취
            bar.setColor(getResources().getColor(R.color.over));
        } else { // 권장과 비슷하게 섭취
            bar.setColor(getResources().getColor(R.color.fit));
        }

        // 그래프 그리기(데이터 기반)
        BarData data = new BarData(bar);
        data.setBarWidth(1f); // 1f로 하면 기준 그래프와 굵기 동일하게 나옴
        data.setDrawValues(false);

        kcal_bar.setData(data); // 데이터 넣고 그려줌
        kcal_bar.invalidate(); // 그래프 갱신

    }
}