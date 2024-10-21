package com.example.pbl6_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pbl6_android.models.CartItem;
import com.example.pbl6_android.models.CartService;
import com.example.pbl6_android.models.Product;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangeListener {
    private RecyclerView recyclerViewCart;
    private TextView totalPriceTextView;
    private Button checkoutButton;
    private List<Product> cartItemList;
    private CartAdapter cartAdapter;

    private CartService cartService;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Liên kết các view
        recyclerViewCart = findViewById(R.id.recyclerView_cart);
        totalPriceTextView = findViewById(R.id.total_price);
        checkoutButton = findViewById(R.id.checkout_button);
        ImageButton backButton =findViewById(R.id.back_button);

        // Khởi tạo danh sách giỏ hàng
        cartItemList = new ArrayList<>();

        // Kết nối với CartService
        Intent intent = new Intent(this, CartService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        // Thiết lập RecyclerView và Adapter
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItemList, this);
        recyclerViewCart.setAdapter(cartAdapter);


        // Xử lý sự kiện nút Thanh toán
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, OrderSummaryActivity.class);
                intent.putExtra("name", "Nguyễn Văn A"); // Thay đổi theo thông tin thực tế
                intent.putExtra("phone", "0123456789"); // Thay đổi theo thông tin thực tế
                intent.putExtra("address", "Hà Nội"); // Thay đổi theo thông tin thực tế
                intent.putExtra("totalPrice", Integer.toString(getTotalPrice())); // Truyền tổng tiền
                startActivity(intent);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CartActivity.this, Home.class);
                startActivity(i);
            }

        });
    }

    // Kết nối với CartService
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("test service");
            CartService.LocalBinder binder = (CartService.LocalBinder) service;
            cartService = binder.getService();
            isBound = true;
            loadCartItems();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("test service fail");

            isBound = false;
        }
    };

    // Tải giỏ hàng từ CartService
    private void loadCartItems() {
        if (isBound && cartService != null) {
            List<Product> products = cartService.getCartItems();
            System.out.println("cart service size:" + products.size());
            if (products != null) {
                cartItemList.clear();  // Xóa danh sách cũ
                cartItemList.addAll(products);  // Thêm danh sách mới
                cartAdapter.notifyDataSetChanged();
                updateTotalPrice();  // Cập nhật tổng tiền
            }
        }
    }

    // Tính tổng tiền
    private void updateTotalPrice() {
        int totalPrice = 0;
        for (Product item : cartItemList) {
            totalPrice += item.getPrice();
        }
        totalPriceTextView.setText("Tổng tiền: " + totalPrice + "đ");
    }

    // Cập nhật tổng tiền khi có sự thay đổi trong giỏ hàng
    @Override
    public void onCartItemChanged() {
        updateTotalPrice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }
    public int getTotalPrice() {
        int totalPrice = 0;
        for (Product item : cartItemList) {
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }
}
