package com.example.pbl6_android.models;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CartService  extends Service {

    private List<Product> cartItems = new ArrayList<>();

    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        public CartService getService() {
            return CartService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("test blind");

        return binder ;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("test start");

        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals("ADD_TO_CART")) {
                // Nhận sản phẩm từ Intent và thêm vào giỏ hàng
                System.out.println("test action");

                Product product = intent.getParcelableExtra("product");
                if (product != null) {
                    System.out.println("test add");

                    addToCart(product);
                }
            }
        }
        return START_STICKY;  // Để giữ Service hoạt động ngầm
    }
    private void addToCart(Product product) {
        cartItems.add(product);
        Log.d("CartService", "Product added to cart: " + product.getName());

        // Gửi broadcast để thông báo cập nhật giỏ hàng cho UI
        Intent broadcastIntent = new Intent("CART_UPDATED");
        broadcastIntent.putParcelableArrayListExtra("cartItems",  new ArrayList<>(cartItems));
        sendBroadcast(broadcastIntent);
    }

    public List<Product> getCartItems() {
        return cartItems;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("CartService", "Service destroyed");
    }



}
