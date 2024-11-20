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
import com.example.pbl6_android.ReviewProductActivity;
import com.example.pbl6_android.models.Product;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotReviewedProductAdapter  extends RecyclerView.Adapter<NotReviewedProductAdapter.ViewHolder>  {

    Context context;

    public NotReviewedProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    List<Product> productList;

    @NonNull
    @Override
    public NotReviewedProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_product_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull NotReviewedProductAdapter.ViewHolder holder, int position) {
        Product product =  productList.get(position);
        int imageResource = context.getResources().getIdentifier(product.getImageUrl(), "drawable", context.getPackageName());
        Glide.with(context).load(imageResource).into(holder.imageView);

        holder.name.setText(product.getName());
        holder.bind(product);



    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
        }

        public void bind(final Product product) {
            Button buy = itemView.findViewById(R.id.btn_review);
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(itemView.getContext(), ReviewProductActivity.class);
                    i.putExtra("product", product);
                    itemView.getContext().startActivity(i);
                }
            });
        }
    }
}
