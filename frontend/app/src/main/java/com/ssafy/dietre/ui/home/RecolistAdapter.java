package com.ssafy.dietre.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssafy.dietre.R;
import com.ssafy.dietre.api.data.FoodDto;

import java.util.ArrayList;

public class RecolistAdapter extends RecyclerView.Adapter<RecolistAdapter.RecolistViewHolder> {
    // 추천받은 음식정보 들어간 리스트
    ArrayList<FoodDto> recoList;

    public RecolistAdapter(ArrayList<FoodDto> recoList) {
        this.recoList = recoList;
    }

    @NonNull
    @Override
    public RecolistAdapter.RecolistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.reco_list, parent, false);

        return new RecolistAdapter.RecolistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecolistAdapter.RecolistViewHolder holder, int position) {
        StringBuilder sb = new StringBuilder();
        String recoDietName = sb.append(recoList.get(position).getName()).append(" | ")
                .append(recoList.get(position).getCalories()).append(" kcal")
                .toString();
        holder.recoName.setText(recoDietName);

        sb = new StringBuilder();
        String recoDietNut = sb.append("탄수화물(g) ").append(recoList.get(position).getCarbohydrates()).append(", ")
                .append("단백질(g) ").append(recoList.get(position).getProtein()).append(", ")
                .append("지방(g) ").append(recoList.get(position).getFat()).toString();
        holder.recoInfo.setText(recoDietNut);

        sb = new StringBuilder();
        // 값이 들어있지 않으면 1인분 고정[[[[[[[
        if (recoList.get(position).getAmount() == null) {
            sb.append("1\n인분");
        }
        // 값이 들어있으면 해당 값을 불러와서 몇 인분인지 표시
        else {
            sb.append(recoList.get(position).getAmount()).append("\n인분");
        }
        String amount = sb.toString();
        holder.recoAmount.setText(amount);
    }

    @Override
    public int getItemCount() {
        return recoList.size();
    }

    class RecolistViewHolder extends RecyclerView.ViewHolder {
        TextView recoName;
        TextView recoInfo;
        TextView recoAmount;

        public RecolistViewHolder(@NonNull View itemView) {
            super(itemView);
            recoName = itemView.findViewById(R.id.text_reco_name);
            recoInfo = itemView.findViewById(R.id.text_reco_info);
            recoAmount = itemView.findViewById(R.id.text_reco_amount);
        }
    }
}
