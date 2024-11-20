package com.example.pbl6_android;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pbl6_android.Activity.Product.DetailActivity;
import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.models.Review;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewProductActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView productName;
    private TextView productPrice;
    private RatingBar ratingBar;
    private TextView tvRatingValue;
    private EditText editTextComments;

    private ImageView productImage;
    private Button sendButton;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";


    private void createReview(Review newReview) {
        Call<Review> call = retrofitInterface.createReview(newReview);
        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if(response.code() == 201 && response.body() != null) {

                }
                else {
                    // Log the response body if it exists
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                    }
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Log.e("API_ERROR", "Failed to create reviews: " + t.getMessage(), t);
            }
        });
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_product); // Thay đổi theo tên file layout của bạn

        Product product = getIntent().getParcelableExtra("product");

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        // Khởi tạo các view
        backButton = findViewById(R.id.back_button);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        ratingBar = findViewById(R.id.ratingBar);
        tvRatingValue = findViewById(R.id.tv_rating_value);
        editTextComments = findViewById(R.id.editText_comments);
        sendButton = findViewById(R.id.send_button);


        productImage = findViewById(R.id.product_image);

        int imageResource = getResources().getIdentifier(
                product.getImageUrl(),
                "drawable",
                getPackageName()
        );


        Glide.with(this) // `this` ở đây là Activity context
                .load(imageResource)
                .into(productImage);



        // Thiết lập thông tin sản phẩm (có thể thay đổi theo nhu cầu của bạn)
        productName.setText(product.getName());
        productPrice.setText(product.getPrice().toString());

        // Thiết lập listener cho RatingBar
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            tvRatingValue.setText("Đánh giá: " + getRatingText(rating));
        });

        // Thiết lập listener cho nút gửi bình luận
        sendButton.setOnClickListener(v -> sendReview(product));

        // Thiết lập listener cho EditText để theo dõi thay đổi bình luận
        editTextComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Thiết lập listener cho nút quay lại
        backButton.setOnClickListener(v -> finish());
    }

    private void sendReview(Product product) {
        // Logic gửi đánh giá và bình luận
        String comment = editTextComments.getText().toString();
        float rating = ratingBar.getRating();
        // Thực hiện việc gửi đánh giá ở đây


        Review newReview = new Review(
                product.getProductId(),
                (int) ratingBar.getRating(),
                editTextComments.getText().toString()
        );

        Toast.makeText(ReviewProductActivity.this, "Review added successfully!", Toast.LENGTH_SHORT).show();
        createReview(newReview);
    }

    private String getRatingText(float rating) {
        if (rating <= 1) return "Rất tệ";
        else if (rating <= 2) return "Tệ";
        else if (rating <= 3) return "Bình thường";
        else if (rating <= 4) return "Tốt";
        else return "Rất tốt";
    }
}
