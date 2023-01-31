package com.ssafy.dietre.ui.calendar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.ssafy.dietre.R;
import com.ssafy.dietre.api.response.MonthlyMealRecordRes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    ArrayList<LocalDate> dayList;
    // 달력에 들어가는 모든 holder의 리스트트
    ArrayList<CalendarViewHolder> holderlist;
    // 하트 정보가 들어간 리스트
    List<MonthlyMealRecordRes> recordList;
    // 프래그먼트랑 통신하는 값
    ClickDate clickDate;
    // 날짜
    LocalDate checkedDate;
    // position 제어하는 변수
    int contPos;

    public CalendarAdapter(ArrayList<LocalDate> dayList, List<MonthlyMealRecordRes> recordList,
                           ClickDate clickDate) {
        holderlist = new ArrayList<>();
        this.dayList = dayList;
        this.clickDate = clickDate;
        // 하트 정보가 들어간 리스트
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.calendar_cell, parent, false);

        return new CalendarViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        // 날짜 변수에 담기
        LocalDate day = dayList.get(position);

        // position 제어하는 변수
        // 빈 날짜 제외외
        if (day == null){
            holder.dayText.setText("");
            holder.dayImgForRate.setVisibility(View.GONE);
            contPos += 1;
        }
        else {
            // 포함하지 않은 holder를 리스트에 저장
            if(!holderlist.contains(holder)){
                holderlist.add(holder);
            }
            // 해당 일자를 넣음
            holder.dayText.setText(String.valueOf(day.getDayOfMonth()));

            // 현재 날짜에 색을 더함
            if (day.equals(CalendarUtil.selectedDate)) {
                holder.dayText.setTextColor(Color.parseColor("#fafafa"));
                holder.dayText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                holder.textLayout.setBackgroundColor(Color.parseColor("#ffd400"));
            }

            // 월 날짜별로 데이터를 받아서 해당 조건에 맞는 이미지 연결
            // 값이 들어오지 않았으면 holder.dayImgForRate.setVisibility(View.GONE); 이걸로 안보이게
            try {
                if (recordList.get(position-contPos).getHeart() == -1) {
                    holder.dayImgForRate.setVisibility(View.GONE);
                }
                else if (recordList.get(position-contPos).getHeart() == 0) {
                    holder.dayImgForRate.setImageResource(R.drawable.empty);
                }
                else if (recordList.get(position-contPos).getHeart() == 1) {
                    holder.dayImgForRate.setImageResource(R.drawable.full);
                }
                else {
                    holder.dayImgForRate.setImageResource(R.drawable.broken);
                }
            } catch (IndexOutOfBoundsException outE) {
                holder.dayImgForRate.setVisibility(View.GONE);
            }
        }

        // 텍스트 색상 지정
        // 토요일 파랑
        if ((position + 1) % 7 == 0) {
            holder.dayText.setTextColor(Color.BLUE);
        }
        // 일요일 빨강
        else if ((position == 0) || (position % 7 == 0)) {
            holder.dayText.setTextColor(Color.RED);
        }

        // 날짜 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if (day != null) {
                    checkedDate = day;
                    // 년
                    int iYear = day.getYear();
                    // 월
                    int iMonth = day.getMonthValue();
                    // 일
                    int iDay = day.getDayOfMonth();

                    String yearMonDay = iYear + "년 " + iMonth + "월 " + iDay + "일";

                    // 클릭한 날짜의 글씨 색을 다르게 해줌
                    holder.parentView.setBackgroundColor(Color.parseColor("#ffd400"));
                    holder.innerView.setBackgroundColor(Color.parseColor("#fafafa"));
                    // 선택한 날짜를 제외한 나머지 날짜는 원래의 색상대로로
                    checkDateForTextColor(holder);

                    // 여기서 클릭해서 bottom sheet의 내용이 변경되도록
                    clickDate.isClicked(true, day);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    /**
     * 선택한 날짜를 제외한 나머지 날짜는 기본 색상으로
     * @param holder
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkDateForTextColor(@NonNull CalendarViewHolder holder) {
        for (int i = 0; i < holderlist.size(); i++) {
            if (holderlist.get(i).dayText.getText() != null &&
                    holderlist.get(i).dayText.getText() != holder.dayText.getText()) {
                holderlist.get(i).parentView.setBackgroundColor(Color.parseColor("#fafafa"));
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void swipe(boolean left) {
        if (!left) {
            if (checkedDate.getDayOfMonth() == 1) {
                clickDate.isEdgeDate();
                return;
            }
            LocalDate minusOneDay = checkedDate.minusDays(1);
            checkedDate = minusOneDay;
            int minusPos = minusOneDay.getDayOfMonth()-1;
            holderlist.get(minusPos).parentView.setBackgroundColor(Color.parseColor("#ffd400"));
            holderlist.get(minusPos).innerView.setBackgroundColor(Color.parseColor("#fafafa"));
            checkDateForTextColor(holderlist.get(minusPos));
        } else {
            if (checkedDate.getDayOfMonth() == holderlist.size()) {
                clickDate.isEdgeDate();
                return;
            }
            LocalDate plusOneDay = checkedDate.plusDays(1);
            checkedDate = plusOneDay;
            int plusPos = plusOneDay.getDayOfMonth()-1;
            holderlist.get(plusPos).parentView.setBackgroundColor(Color.parseColor("#ffd400"));
            holderlist.get(plusPos).innerView.setBackgroundColor(Color.parseColor("#fafafa"));
            checkDateForTextColor(holderlist.get(plusPos));
        }
        clickDate.isClicked(true, checkedDate);
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder {
        View parentView;
        View innerView;
        LinearLayout textLayout;
        // 날짜
        TextView dayText;
        // 달성률 표시 하트
        ImageView dayImgForRate;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.text_date);
            dayImgForRate = itemView.findViewById(R.id.img_date);
            parentView = itemView.findViewById(R.id.layout_date_outer);
            innerView = itemView.findViewById(R.id.layout_date_inner);
            textLayout = itemView.findViewById(R.id.layout_ceil);
        }
    }
}
