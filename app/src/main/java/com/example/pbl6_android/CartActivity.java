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
import android.os.Parcelable;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pbl6_android.models.Cart;
import com.example.pbl6_android.models.CartItem;
import com.example.pbl6_android.models.CartItemReal;
import com.example.pbl6_android.models.CartService;
import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangeListener {
    private RecyclerView recyclerViewCart;
    private TextView totalPriceTextView;
    private Button checkoutButton;

    //nguyen old code
    private List<Product> cartItemList;
    //nguyen old code

    private CartAdapter cartAdapter;


    private boolean isBound = false;


    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";



    private void fetchCartProduct(UUID userId) {
        Call<Cart> call = retrofitInterface.getCartOfUser(userId);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Cart userCart = response.body();
                    SparseIntArray productQuantities = new SparseIntArray();
                    int index = 0;
                    for (CartItemReal cartItem : userCart.getCartItems()) {
                        Product product = cartItem.getProduct();
                        if (product != null) {
                            cartItemList.add(product);
                            productQuantities.put(index, cartItem.getQuantity()); // Đặt quantity cho sản phẩm
                            index++;
                            System.out.println("product quantity:" + cartItem.getQuantity());
                        }
                    }
                    cartAdapter.notifyDataSetChanged();
                    cartAdapter.setProductQuantities(productQuantities);
                    recyclerViewCart.setAdapter(cartAdapter);
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Toast.makeText(CartActivity.this,
                        "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private ArrayList<Integer> convertSparseIntArrayToArrayList(SparseIntArray sparseIntArray) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < sparseIntArray.size(); i++) {
            int key = sparseIntArray.keyAt(i);     // Vị trí sản phẩm
            int value = sparseIntArray.valueAt(i); // Số lượng sản phẩm
            arrayList.add(key);
            arrayList.add(value);
        }
        return arrayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        recyclerViewCart = findViewById(R.id.recyclerView_cart);
        totalPriceTextView = findViewById(R.id.total_price);
        checkoutButton = findViewById(R.id.checkout_button);
        ImageButton backButton =findViewById(R.id.back_button);

        cartItemList = new ArrayList<>();

        fetchCartProduct(UUID.fromString("d4e56743-ff2c-41d3-957d-576e9f574c5d"));


        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(CartActivity.this,cartItemList, this);


        checkoutButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                List<Product> selectedProducts = cartAdapter.getSelectedProducts();
                SparseIntArray productQuantities = cartAdapter.getProductQuantities();
                ArrayList<Integer> quantitiesList = new ArrayList<>();
                for (Product product : selectedProducts) {
                    int position = cartItemList.indexOf(product);
                    if (position != -1) {
                        quantitiesList.add(productQuantities.get(position, 1)); // Default to 1 if not set
                    }
                }
                System.out.println("Cart check quantity:" + quantitiesList.get(0));

                Intent intent = new Intent(CartActivity.this, OrderSummaryActivity.class);
                intent.putExtra("name", "Nguyễn Văn A"); // Thay đổi theo thông tin thực tế
                intent.putExtra("phone", "0123456789"); // Thay đổi theo thông tin thực tế
                intent.putExtra("address", "Hà Nội"); // Thay đổi theo thông tin thực tế
                intent.putExtra("totalPrice", Integer.toString(13000)); // Truyền tổng tiền
                intent.putParcelableArrayListExtra("productItems" , (ArrayList<? extends Parcelable>) selectedProducts);
                intent.putIntegerArrayListExtra("quantities", quantitiesList);
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


    private void updateTotalPrice() {
        int totalPrice = 0;
        SparseIntArray quantities = cartAdapter.getProductQuantities();
//        System.out.println("product quantity:" + quantities);
        List<Product> selectedProducts = cartAdapter.getSelectedProducts();

        for (Product item : selectedProducts) {
            int position = cartItemList.indexOf(item); // Find the position in cartItemList
            int quantity = quantities.get(position, 1); // Default to 1 if not set
            totalPrice += item.getPrice() * quantity;
        }

        totalPriceTextView.setText("Tổng tiền: " + totalPrice + "đ");
    }

    @Override
    public void onCartItemChanged() {
        updateTotalPrice();
    }


}
