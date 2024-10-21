package com.example.pbl6_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6_android.R;
import com.example.pbl6_android.models.Promotion;

import java.util.List;

public class PromoteAdapter extends RecyclerView.Adapter<PromoteAdapter.ViewHolder>{

    Context context;
    List<Promotion> promotionList;

    public PromoteAdapter(Context context, List<Promotion> promotionList) {
        this.context = context;
        this.promotionList = promotionList;
    }

    @NonNull
    @Override
    public PromoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PromoteAdapter.ViewHolder holder, int position) {
        Promotion promotion = promotionList.get(position);

        holder.content.setText(promotion.getContent());
        holder.discount.setText(String.format("Giảm %s%%", promotion.getPercentage().toString()));
        holder.code.setText(String.format("Mã:%s", promotion.getCode()));

    }

    @Override
    public int getItemCount() {
        return promotionList.size(); // Return the actual size of the list
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView content,discount,code;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.dis_content);
            discount= itemView.findViewById(R.id.dis_per);
            code= itemView.findViewById(R.id.dis_code);
        }
    }
}
