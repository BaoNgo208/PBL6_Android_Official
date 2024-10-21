package com.example.pbl6_android;
import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pbl6_android.models.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItemList;
    private OnCartChangeListener onCartChangeListener;

    private List<Product> productList;

//    public CartAdapter( OnCartChangeListener onCartChangeListener , List<CartItem> cartItemList) {
//        this.cartItemList = cartItemList;
//        this.onCartChangeListener = onCartChangeListener;
//    }

    public CartAdapter( List<Product> productList ,OnCartChangeListener onCartChangeListener) {
        this.onCartChangeListener = onCartChangeListener;
        this.productList = productList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product item = productList.get(position);

        holder.productName.setText(item.getName());
        holder.productPrice.setText("Giá: " + item.getPrice() + "đ");
//        holder.productQuantity.setText(String.valueOf(item.getProductQuantity()));
        holder.productImage.setImageResource(R.drawable.pizza);

        // Nút tăng số lượng



    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage;
        Button btnIncrease, btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productImage = itemView.findViewById(R.id.product_image);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
        }
    }

    public interface OnCartChangeListener {
        void onCartItemChanged();
    }
}