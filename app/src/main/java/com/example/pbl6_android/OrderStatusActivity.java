package com.example.pbl6_android;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class OrderStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_status); // liên kết với tệp XML

        // Liên kết các phần tử giao diện với Java code
        ImageView backArrow = findViewById(R.id.back_arrow);
        TextView title = findViewById(R.id.title);
//        Button viewDetailsButton = findViewById(R.id.view_details_button);
//        TextView storeName = findViewById(R.id.store_name);
//        TextView productDescription = findViewById(R.id.product_description);
//        TextView refundStatus = findViewById(R.id.refund_status);
//        TextView productPrice = findViewById(R.id.product_price);
//        TextView refundAmount = findViewById(R.id.refund_amount);
        TextView tabPickup = findViewById(R.id.tab_pickup);
        TextView tabReturn = findViewById(R.id.tab_return);
        TextView tabDelivered = findViewById(R.id.tab_delivered);

        // Thiết lập hành động khi nhấn vào mũi tên quay lại
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // đóng activity và quay lại màn hình trước đó
            }
        });

        // Thiết lập hành động khi nhấn nút "Xem chi tiết"
//        viewDetailsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(HomeActivity.this, "Xem chi tiết đơn hàng", Toast.LENGTH_SHORT).show();
//            }
//        });

//        // Bạn có thể thiết lập dữ liệu cho các phần tử này nếu cần
//        storeName.setText("Tên cửa hàng");
//        productDescription.setText("Mô tả sản phẩm");
//        refundStatus.setText("Trạng thái hoàn tiền");
//        productPrice.setText("₫103.000");
//        refundAmount.setText("Số tiền hoàn: ₫90.000");


        tabPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabPickup.setTextColor(Color.RED);
                tabReturn.setTextColor(Color.BLACK);  // Đặt lại màu cho các TextView khác
                tabDelivered.setTextColor(Color.BLACK);
            }
        });

        tabReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabReturn.setTextColor(Color.RED);
                tabPickup.setTextColor(Color.BLACK);
                tabDelivered.setTextColor(Color.BLACK);
            }
        });

        tabDelivered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabDelivered.setTextColor(Color.RED);
                tabPickup.setTextColor(Color.BLACK);
                tabReturn.setTextColor(Color.BLACK);
            }
        });

    }
}
