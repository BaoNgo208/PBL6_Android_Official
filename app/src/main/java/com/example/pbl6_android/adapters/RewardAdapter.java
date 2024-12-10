package com.example.pbl6_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pbl6_android.R;

import java.io.File;
import java.util.List;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder> {

    private List<Integer> imagePaths;



    public RewardAdapter(  List<Integer> imagePaths) {
        this.imagePaths = imagePaths;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward_item, parent, false);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
//        holder.rewardTextView.setText(rewards.get(position));
        Integer imagePath = imagePaths.get(position);
        holder.imageView.setImageResource(imagePath);
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public static class RewardViewHolder extends RecyclerView.ViewHolder {
        TextView rewardTextView;

        ImageView imageView;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);
//            rewardTextView = itemView.findViewById(R.id.reward_text);
            imageView = itemView.findViewById(R.id.reward_image_view);
        }
    }
}
