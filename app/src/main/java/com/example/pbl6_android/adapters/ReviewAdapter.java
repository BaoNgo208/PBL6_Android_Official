package com.example.pbl6_android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pbl6_android.R;
import com.example.pbl6_android.models.Review;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    Context context;
    List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;

    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Review review = reviewList.get(position);
        int imageResource = context.getResources().getIdentifier(review.getProduct().getImageUrl(), "drawable", context.getPackageName());
        Glide.with(context).load(imageResource).into(holder.productImage);

        holder.username.setText(review.getUser().getUserName());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy"); // Định dạng tùy chỉnh
//        String formattedDate = dateFormat.format(review.getReviewDate());
//        holder.reviewDate.setText(formattedDate);

        holder.content.setText(review.getComment());
        holder.ratingBar.setRating((float) review.getRating());

        holder.productName.setText(review.getProduct().getName());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username,reviewDate,content;

        RatingBar ratingBar;


        ImageView productImage;
        TextView productName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            reviewDate = itemView.findViewById(R.id.review_date);
            content = itemView.findViewById(R.id.review_content);
            ratingBar = itemView.findViewById(R.id.ratingBar);

            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);

        }
    }
}
