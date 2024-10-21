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

import java.text.DecimalFormat;
import java.util.List;

public class RecommendedProductAdapter extends RecyclerView.Adapter<RecommendedProductAdapter.ViewHolder>{

    Context context;
    List<Product> productList;
    private OnProductClickListener productClickListener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    @NonNull
    @Override
    public RecommendedProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_product,parent,false));
    }

    public RecommendedProductAdapter(Context context, List<Product> productList,OnProductClickListener productClickListener) {
        this.context = context;
        this.productList = productList;
        this.productClickListener = productClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedProductAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);


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

        System.out.println("Giá đã định dạng: " + formattedPrice); // Kết quả sẽ là "20.000 đồn

        holder.price.setText(formattedPrice);


        holder.itemView.setOnClickListener(v -> {
            if (productClickListener != null) {
                productClickListener.onProductClick(product);
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
//            price = imageView.findViewById(R.id.all_price);
        }
    }
}
