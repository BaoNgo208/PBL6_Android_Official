package com.example.pbl6_android;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReviewProductActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView productName;
    private TextView productPrice;
    private RatingBar ratingBar;
    private TextView tvRatingValue;
    private EditText editTextComments;
    private Button sendButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_product); // Thay đổi theo tên file layout của bạn

        // Khởi tạo các view
        backButton = findViewById(R.id.back_button);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        ratingBar = findViewById(R.id.ratingBar);
        tvRatingValue = findViewById(R.id.tv_rating_value);
        editTextComments = findViewById(R.id.editText_comments);
        sendButton = findViewById(R.id.send_button);

        // Thiết lập thông tin sản phẩm (có thể thay đổi theo nhu cầu của bạn)
        productName.setText("Cơm tấm");
        productPrice.setText("₫103.000");

        // Thiết lập listener cho RatingBar
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            tvRatingValue.setText("Đánh giá: " + getRatingText(rating));
        });

        // Thiết lập listener cho nút gửi bình luận
        sendButton.setOnClickListener(v -> sendReview());

        // Thiết lập listener cho EditText để theo dõi thay đổi bình luận
        editTextComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Có thể làm gì đó khi bình luận thay đổi
            }
        });

        // Thiết lập listener cho nút quay lại
        backButton.setOnClickListener(v -> finish());
    }

    private void sendReview() {
        // Logic gửi đánh giá và bình luận
        String comment = editTextComments.getText().toString();
        float rating = ratingBar.getRating();
        // Thực hiện việc gửi đánh giá ở đây
    }

    private String getRatingText(float rating) {
        if (rating <= 1) return "Rất tệ";
        else if (rating <= 2) return "Tệ";
        else if (rating <= 3) return "Bình thường";
        else if (rating <= 4) return "Tốt";
        else return "Rất tốt";
    }
}
