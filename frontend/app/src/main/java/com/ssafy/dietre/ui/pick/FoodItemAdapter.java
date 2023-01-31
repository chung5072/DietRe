package com.ssafy.dietre.ui.pick;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.ssafy.dietre.MainActivity;
import com.ssafy.dietre.R;

import java.util.ArrayList;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.MyViewHolder> {

    ArrayList<FoodItem> list = new ArrayList<>();
    boolean close;
    boolean notTouch = false;
    Context context;
    PickFragment pickFragment;
    private itemClickListener itemListener;
    private xClickListener xClickListener;
    private static Long chooseNo = -1L;

    // pickfragment에서 사용할때
    public FoodItemAdapter(PickFragment pickFragment, ArrayList<FoodItem> list, boolean close) {
        this.pickFragment = pickFragment;
        this.list = list;
        this.close = close;
    }
    // resolve 에서 사용할때
    public FoodItemAdapter(ArrayList<FoodItem> list, boolean close, boolean notTouch) {
        this.list = list;
        this.close = close;
        this.notTouch = notTouch;
    }

    public interface itemClickListener {
        void addFood(FoodItem food, Long position);
    }

    public interface xClickListener {
        void removeFood(int position);
    }

    public void setItemClickListener(itemClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setXClickListener(xClickListener xListener) {
        this.xClickListener = xListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list, parent, false);
        context = parent.getContext();
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.foodNo = holder.getAdapterPosition();
        holder.foodId = list.get(position).getId();
        holder.foodName.setText(list.get(position).getName());
        holder.foodInfo.setText(list.get(position).getInfo());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int foodNo;
        Long foodId;
        TextView foodName;
        TextView foodInfo;
        Button btnX;
        LinearLayout item;
        FooddialogFragment dialog;

        public MyViewHolder(View v) {
            super(v);

            foodName = v.findViewById(R.id.food_name);
            foodInfo = v.findViewById(R.id.food_info);
            btnX = v.findViewById(R.id.btn_x);
            item = v.findViewById(R.id.layout_item);
            if(notTouch) {
                item.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
                });
            } else {
                // 해당칸 누르면 fooddialog 에 값 setting해서 넘김
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(context == PickFragment.context) { // 인식 식단 -> fooddialog 띄우기
                            item.setBackgroundColor(ContextCompat.getColor(context, R.color.recommend));
                            String food = (String) foodName.getText();
                            FragmentManager fm = ((MainActivity)context).getSupportFragmentManager();
                            chooseNo = Long.valueOf(getAdapterPosition());
                            dialog = FooddialogFragment.getInstance(food, chooseNo);
                            dialog.show(fm, "음식 검색 dialog");
                        } else { // food dialog list에서 음식 선택 -> 인식 식단에 추가
                            FoodItem food = new FoodItem(foodId, String.valueOf(foodName.getText()), String.valueOf(foodInfo.getText()));
                            Log.e("누른 음식", food.getName());
                            if(food != null){
                                itemListener.addFood(food, chooseNo);
                                notifyDataSetChanged();
                            }
                        }
                    }
                });
            }

            // close가 true일때만 xbtn 보이고 작동
            if(!close) {
                btnX.setVisibility(View.GONE);
            }
            // x 버튼 누르면 해당 list 지워짐
            btnX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    xClickListener.removeFood(getAdapterPosition());
                    //pickFragment.removeFood(getAdapterPosition());
                    //list.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }

    // pick, fooddialog 에서 이 method를 통해 food item을 add
    public void addItem(FoodItem item) {
        list.add(item);
    }


}
