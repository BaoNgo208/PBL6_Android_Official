package com.example.pbl6_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6_android.Activity.Order.OrderHistoryActivity;
import com.example.pbl6_android.OrderStatusActivity;
import com.example.pbl6_android.R;
import com.example.pbl6_android.models.OrderStatus;

import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder>  {
    Context context;
    List<OrderStatus> orderStatusList;

    public OrderStatusAdapter(Context context, List<OrderStatus> orderStatusList) {
        this.context = context;
        this.orderStatusList = orderStatusList;
    }



    @NonNull
    @Override
    public OrderStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_horizontal_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderStatusAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(orderStatusList.get(position).getImage());
        holder.name.setText(orderStatusList.get(position).getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderHistoryActivity.class);
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return orderStatusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;


        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.hor_img);
            name = itemView.findViewById(R.id.hor_text);
        }
    }
}
