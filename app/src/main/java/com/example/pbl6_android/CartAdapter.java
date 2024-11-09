package com.example.pbl6_android;
import androidx.annotation.NonNull;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.SparseBooleanArray;
import com.bumptech.glide.Glide;
import com.example.pbl6_android.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    Context context;
    private List<CartItem> cartItemList;
    private OnCartChangeListener onCartChangeListener;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private SparseIntArray productQuantities = new SparseIntArray();

    private List<Product> productList;

//    public CartAdapter( OnCartChangeListener onCartChangeListener , List<CartItem> cartItemList) {
//        this.cartItemList = cartItemList;
//        this.onCartChangeListener = onCartChangeListener;
//    }

    public CartAdapter( Context context,List<Product> productList ,OnCartChangeListener onCartChangeListener) {
        this.onCartChangeListener = onCartChangeListener;
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }
    public SparseBooleanArray getSelectedItems() {
        return selectedItems;
    }
    public SparseIntArray getProductQuantities() {
        return productQuantities;
    }


    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product item = productList.get(position);
        int quantity = productQuantities.get(position, 1); // Default quantity is 1
        holder.productQuantity.setText(String.valueOf(quantity));
        updateProductPrice(holder, item.getPrice(), quantity);

        holder.checkbox.setOnCheckedChangeListener(null);  // Reset listener
        holder.checkbox.setChecked(selectedItems.get(position, false)); // Get selection state

        holder.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            selectedItems.put(position, isChecked);  // Save selection state
            onCartChangeListener.onCartItemChanged();  // Notify to update total price

        });

        int imageResource = context.getResources().getIdentifier(item.getImageUrl(), "drawable", context.getPackageName());

        // Use Glide to load the image by resource ID
        Glide.with(context).load(imageResource).into(holder.productImage);

        holder.productName.setText(item.getName());
        holder.productPrice.setText("Giá: " + item.getPrice() + "đ");

        // Increase button listener
        holder.btnIncrease.setOnClickListener(v -> {
            // Retrieve the updated quantity from productQuantities
            int currentQuantity = productQuantities.get(position, 1);
            int newQuantity = currentQuantity + 1;
            productQuantities.put(position, newQuantity);
            holder.productQuantity.setText(String.valueOf(newQuantity));



            if (holder.checkbox.isChecked()) {
                onCartChangeListener.onCartItemChanged(); // Update total price if selected
            }
        });


        // Decrease button listener
        holder.btnDecrease.setOnClickListener(v -> {
            // Retrieve the updated quantity from productQuantities
            int currentQuantity = productQuantities.get(position, 1);
            if (currentQuantity > 1) {
                int newQuantity = currentQuantity - 1;
                productQuantities.put(position, newQuantity);
                holder.productQuantity.setText(String.valueOf(newQuantity));

                if (holder.checkbox.isChecked()) {
                    onCartChangeListener.onCartItemChanged(); // Update total price if selected
                }
            }
        });

//        holder.productQuantity.setText(String.valueOf(item.getProductQuantity()));





    }

    public void setProductQuantities(SparseIntArray productQuantities) {
        this.productQuantities = productQuantities;
        notifyDataSetChanged(); // Cập nhật lại RecyclerView khi dữ liệu thay đổi
    }

    private void updateProductPrice(CartViewHolder holder, double unitPrice, int quantity) {
        int totalPrice = (int) (unitPrice * quantity);
        holder.productPrice.setText("Giá: " + totalPrice + "đ");
    }
    public List<Product> getSelectedProducts() {
        List<Product> selectedProducts = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            if (selectedItems.get(i, false)) { // Check if the item is selected
                selectedProducts.add(productList.get(i));
            }
        }
        return selectedProducts;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;
        ImageView productImage;
        AppCompatImageButton btnIncrease, btnDecrease;
        CheckBox checkbox;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            productImage = itemView.findViewById(R.id.product_image);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            checkbox = itemView.findViewById(R.id.product_checkbox);
        }
    }

    public interface OnCartChangeListener {
        void onCartItemChanged();
    }
}