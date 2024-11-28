package com.example.pbl6_android.adapters;

import android.content.Context;
import android.graphics.Paint;
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

import java.text.DecimalFormat;
import java.util.List;

public class SuggestedByCategoryAdapter  extends RecyclerView.Adapter<SuggestedByCategoryAdapter.ViewHolder>{
    Context context;

    List<Product> productList;

    private OnSuggestedProductClickListener productClickListener;
    public interface OnSuggestedProductClickListener {
        void onSuggestedProductClick(Product product);
    }

    public SuggestedByCategoryAdapter(Context context, List<Product> productList, OnSuggestedProductClickListener productClickListener) {
        this.context = context;
        this.productList = productList;
        this.productClickListener = productClickListener;
    }

    @NonNull
    @Override
    public SuggestedByCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_product,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedByCategoryAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);


        int imageResource = context.getResources().getIdentifier(product.getImageUrl(), "drawable", context.getPackageName());

        Glide.with(context).load(imageResource).into(holder.imageView);

        holder.name.setText(product.getName());

        double price = product.getPrice();
        long roundedPrice = (long) price;

        String formattedPrice = new DecimalFormat("#,###").format(roundedPrice) + " đồng";

        System.out.println("Giá đã định dạng: " + formattedPrice); // Kết quả sẽ là "20.000 đồn

        holder.price.setText(formattedPrice);


        holder.itemView.setOnClickListener(v -> {
            if (productClickListener != null) {
                productClickListener.onSuggestedProductClick(product);
            }
        });
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
            imageView = itemView.findViewById(R.id.all_img);
            name = itemView.findViewById(R.id.all_product_name);
            price= itemView.findViewById(R.id.dollar);

            TextView dollarDis = itemView.findViewById(R.id.dollar_dis);
            dollarDis.setPaintFlags(dollarDis.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }
}
