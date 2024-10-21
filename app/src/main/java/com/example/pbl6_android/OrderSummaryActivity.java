package com.example.pbl6_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OrderSummaryActivity extends AppCompatActivity {

    private TextView tvName, tvPhone, tvAddress, tvTotalPrice, tvDiscountedPrice;
    private EditText etDiscountCode;
    private Button btnSubmit;
    private ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        // Khởi tạo các biến với view tương ứng
        tvName = findViewById(R.id.full_name);
        tvPhone = findViewById(R.id.phone_number);
        tvAddress = findViewById(R.id.address);
        tvTotalPrice = findViewById(R.id.total_price); // Thêm dòng này để khởi tạo
        tvDiscountedPrice = findViewById(R.id.discounted_price); // Thêm dòng này để khởi tạo
        etDiscountCode = findViewById(R.id.discount_code);
        btnSubmit = findViewById(R.id.order_button);
        back_button=findViewById(R.id.back_button);

        // Lấy thông tin từ Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");
        String totalPrice = intent.getStringExtra("totalPrice"); // Tổng tiền
        String discountedPrice = intent.getStringExtra("discounted_price"); // Tổng tiền sau giảm giá

        // Hiển thị thông tin đơn đặt hàng
        tvName.setText("Họ tên: " + name);
        tvPhone.setText("Số điện thoại: " + phone);
        tvAddress.setText("Địa chỉ giao hàng: " + address);
        tvTotalPrice.setText("Tổng tiền: " + totalPrice);
        tvDiscountedPrice.setText("Tổng tiền sau giảm: " + discountedPrice);

        // Xử lý sự kiện nhấn nút
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discountCode = etDiscountCode.getText().toString();
                // Xử lý mã giảm giá (có thể thêm logic ở đây)
                // Ví dụ: kiểm tra mã giảm giá và cập nhật tổng tiền
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderSummaryActivity.this, CartActivity.class);
                startActivity(i);
            }

        });
    }
}
