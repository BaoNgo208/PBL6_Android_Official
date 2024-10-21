package com.example.pbl6_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pbl6_android.R;
import com.example.pbl6_android.models.Order;
import com.example.pbl6_android.models.Product;

import java.text.DecimalFormat;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    Context context;
    List<Order> orderList;


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

        Product product = order.getOrderDetails().get(0).getProduct();
        // Get the image resource ID dynamically using the image name
        int imageResource = context.getResources().getIdentifier(product.getImageUrl(), "drawable", context.getPackageName());

        // Use Glide to load the image by resource ID
        Glide.with(context).load(imageResource).into(holder.imageView);

        holder.name.setText(product.getName());

        double price = product.getPrice(); // Nếu price là double từ database

        // Chuyển đổi giá thành long để loại bỏ phần thập phân
        long roundedPrice = (long) price; // Lấy phần nguyên

        // Định dạng giá trị thành chuỗi với dấu phẩy
        String formattedPrice = new DecimalFormat("#,###").format(roundedPrice) + " đồng";
        // Kiểm tra và loại bỏ 2 ký tự cuối nếu có


        holder.price.setText(formattedPrice);



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
        }
    }
}
