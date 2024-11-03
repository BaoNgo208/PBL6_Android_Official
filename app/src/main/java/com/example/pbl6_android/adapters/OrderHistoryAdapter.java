package com.example.pbl6_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pbl6_android.Activity.Product.DetailActivity;
import com.example.pbl6_android.R;
import com.example.pbl6_android.models.Order;
import com.example.pbl6_android.models.Product;

import java.text.DecimalFormat;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    Context context;
    List<Order> orderList;

    Product product;


    public OrderHistoryAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);

        product = order.getOrderDetails().get(0).getProduct();
        int imageResource = context.getResources().getIdentifier(product.getImageUrl(), "drawable", context.getPackageName());

        Glide.with(context).load(imageResource).into(holder.imageView);

        holder.name.setText(product.getName());

        double price = order.getTotalAmount(); // Nếu price là double từ database

        long roundedPrice = (long) price; // Lấy phần nguyên

        String formattedPrice = new DecimalFormat("#,###").format(roundedPrice) + " đồng";


        holder.price.setText(formattedPrice);
        holder.bind(product);


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,price,orderedDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            price= itemView.findViewById(R.id.product_price);
            orderedDate = itemView.findViewById(R.id.product_orderedDate);
//            price = imageView.findViewById(R.id.all_price);

//            Button buy = itemView.findViewById(R.id.btn_rePurchased);

        }

        public void bind(final Product product) {
            Button buy = itemView.findViewById(R.id.btn_rePurchased);
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), DetailActivity.class);
                    i.putExtra("product", product);
                    System.out.println("Product name: " + product.getName());
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}
