package com.example.pbl6_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.models.Promotion;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import java.io.Console;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderSummaryActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";

    private TextView tvName, tvPhone, tvAddress, tvTotalPrice, tvDiscountedPrice,promotionIdText;
    private EditText discountCodeInput;
    private Button btnSubmit;
    private ImageButton back_button;

    private EditText etDiscountCode;
    private TextView discountCodeResult;

    public String promotionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        discountCodeInput = findViewById(R.id.discount_code);
        discountCodeResult = findViewById(R.id.promote_code_result);

        // Initialize views
        tvName = findViewById(R.id.full_name);
        tvPhone = findViewById(R.id.phone_number);
        tvAddress = findViewById(R.id.address);
        tvTotalPrice = findViewById(R.id.total_price);
        tvDiscountedPrice = findViewById(R.id.discounted_price);
        etDiscountCode = findViewById(R.id.discount_code);
        promotionIdText = findViewById(R.id.promotion_id);
        btnSubmit = findViewById(R.id.order_button);
        back_button = findViewById(R.id.back_button);

        // Disable the submit button initially
//        btnSubmit.setEnabled(false);

        // Get the product from Intent
        Product product = getIntent().getParcelableExtra("product");

        // Get information from Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");
        String totalPrice = intent.getStringExtra("totalPrice");

        // Display order information
        tvName.setText("Họ tên: " + name);
        tvPhone.setText("Số điện thoại: " + phone);
        tvAddress.setText("Địa chỉ giao hàng: " + address);
        tvTotalPrice.setText("Tổng tiền: " + product.getPrice());
        tvDiscountedPrice.setText("Tổng tiền sau giảm: " + product.getPrice() + "đ");

        // Back button event
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderSummaryActivity.this, CartActivity.class);
                startActivity(i);
            }
        });

        // Check promotion code button event
        Button checkPromoteCode = findViewById(R.id.check_discount_buttonn);
        checkPromoteCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDiscountCode(product);
            }
        });

        // Submit button event
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectPayment = new Intent(OrderSummaryActivity.this, SelectPaymentMethodActivity.class);
                selectPayment.putExtra("product", (Parcelable) product);
                System.out.println("promtionCode:" + promotionIdText.getText());
                selectPayment.putExtra("promotionId", promotionIdText.getText()); // Pass the promotionId
                startActivity(selectPayment);
            }
        });
    }

    private void checkDiscountCode(Product product) {
        String codeInput = discountCodeInput.getText().toString();
        if (codeInput.isEmpty()) {
            discountCodeResult.setText("Please enter a discount code.");
            discountCodeResult.setVisibility(View.VISIBLE);
            return;
        }

        Call<Promotion> call = retrofitInterface.getPromotionCode(codeInput);
        call.enqueue(new Callback<Promotion>() {
            @Override
            public void onResponse(Call<Promotion> call, Response<Promotion> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Promotion promotion = response.body();
                    System.out.println("check null:" + response.body().getPromotionId() );
                    if (codeInput.equals(promotion.getCode())) {
                        discountCodeResult.setText("Code exists!");
                        discountCodeResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                        double originalPrice = product.getPrice();
                        double discountPercentage = promotion.getPercentage();
                        promotionId = String.valueOf(promotion.getPromotionId()); // Set promotionId here
                        double discountedPrice = originalPrice * (1 - discountPercentage / 100);
                        tvDiscountedPrice.setText("Tổng tiền sau giảm: " + discountedPrice + "đ");

                        // Enable the submit button after setting the promotion ID
//                        btnSubmit.setEnabled(true);
                    } else {
                        discountCodeResult.setText("Code does not exist.");
                        discountCodeResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    }
                } else {
                    discountCodeResult.setText("Invalid response.");
                    discountCodeResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
                discountCodeResult.setVisibility(View.VISIBLE);
                promotionIdText.setText(response.body().getPromotionId().toString());

            }

            @Override
            public void onFailure(Call<Promotion> call, Throwable t) {
                discountCodeResult.setText("Error: " + t.getMessage());
                discountCodeResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                discountCodeResult.setVisibility(View.VISIBLE);
            }
        });
    }
}
