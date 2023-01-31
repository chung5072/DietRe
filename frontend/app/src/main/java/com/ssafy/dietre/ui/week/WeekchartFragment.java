package com.ssafy.dietre.ui.week;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.ssafy.dietre.R;
import com.ssafy.dietre.databinding.FragmentWeekchartBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    public class WeekchartFragment extends Fragment {

        private static FragmentWeekchartBinding binding;
        private static WeekViewModel weekViewModel;
        private String chart_title = null;
        private int chart_no = 0;
        private static int wDay = 0;
        private ArrayList<Integer> chart_data = null;
        private static List<ArrayList<Integer>> listData;

    public WeekchartFragment() {
        // Required empty public constructor
    }

    public static WeekchartFragment getInstance(String title, int idx) {
        WeekchartFragment chart = new WeekchartFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("index", idx);
        if(title!=null && idx>=0 && idx<4) {
            chart.setArguments(args);
            Log.d("Week Chart 저장", title);
        }

        return chart;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 입력받은 차트 데이터 이름과 list data 세팅
        if (getArguments() != null) {
            chart_title = getArguments().getString("title");
            chart_no = getArguments().getInt("index");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WeekViewModel weekViewModel = new ViewModelProvider(requireActivity()).get(WeekViewModel.class);
        binding = FragmentWeekchartBinding.inflate(inflater, container, false);
        View v = binding.getRoot();

        TextView title = (TextView) binding.textNutchart;
        CombinedChart chart = (CombinedChart) binding.weekChart;

        title.setText(chart_title);
        title.setTextColor(getResources().getColor(R.color.resolve_acc));
        // TODO 일주일치 kcal, carbo, protein, fat 데이터 정리해서 넣어주기
        wDay = weekViewModel.getWeekLength();
        listData = Arrays.asList(weekViewModel.getKcalArr(), weekViewModel.getCarboArr(),
                weekViewModel.getProteinArr(), weekViewModel.getFatArr());
        setCombinedChart(weekViewModel, chart, listData.get(chart_no));

        return v;
    }

    private void setCombinedChart(WeekViewModel weekViewModel, CombinedChart chart, ArrayList<Integer> data) {
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setDrawGridBackground(false);

        // line 뒤에 bar 그리기
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 아래에만 표기
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(12);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return weekViewModel.getWeekDay()[(int) value];
            }
        });

        YAxis leftAxis = chart.getAxisLeft(); // 왼쪽
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        YAxis rightAxis = chart.getAxisRight(); // 오른쪽
        rightAxis.setAxisMinimum(0f);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        CombinedData cData = new CombinedData();
        cData.setData(setLineData(data));
        cData.setData(setBarData(data));

        xAxis.setAxisMinimum(cData.getXMin() - 0.3f);
        xAxis.setAxisMaximum(cData.getXMax() + 0.3f);
        chart.setData(cData);
        chart.invalidate();

    }

    private BarData setBarData(ArrayList<Integer> data) {
        ArrayList<BarEntry> values = new ArrayList<>();
        for(int i=0; i<wDay; i++) {
            values.add(new BarEntry(i, data.get(i+1)));
        }

        BarDataSet set = new BarDataSet(values, "섭취량");
        set.setDrawValues(false);
        // 나중에 범위별로 그래프 색깔 바꾸기 추가
        set.setColor(getResources().getColor(R.color.resolve));

        BarData bar = new BarData(set);
        bar.setBarWidth(0.45f);

        return bar;
    }

    private LineData setLineData(ArrayList<Integer> data) {
        LineData line = new LineData();
        ArrayList<Entry> values = new ArrayList<>();
        for(int i=0; i<wDay; i++) {
            values.add(new Entry(i, data.get(0)));
        }
        LineDataSet set = new LineDataSet(values, "기준선");
        set.setColor(getResources().getColor(R.color.resolve_acc));
        set.setLineWidth(2.5f);
        set.setCircleColor(getResources().getColor(R.color.resolve_acc));
        set.setCircleRadius(5f);
        set.setFillColor(getResources().getColor(R.color.resolve_acc));
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        line.addDataSet(set);
        return line;
    }

}