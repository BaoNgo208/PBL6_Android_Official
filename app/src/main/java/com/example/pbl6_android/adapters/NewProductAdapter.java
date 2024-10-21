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
import com.example.pbl6_android.models.Product;

import java.util.List;

public class NewProductAdapter  extends RecyclerView.Adapter<NewProductAdapter.ViewHolder> {
    Context context;
    List<Product> productList;

    @NonNull
    @Override
    public NewProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_product,parent,false));

    }

    public NewProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public void onBindViewHolder(@NonNull NewProductAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(productList.get(position).getImageUrl()).into(holder.imageView);
        holder.name.setText(productList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name,price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.new_img);
            name = itemView.findViewById(R.id.new_product_name);
//            price = imageView.findViewById(R.id.all_price);
        }
    }
}
