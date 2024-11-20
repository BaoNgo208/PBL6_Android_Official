package com.example.pbl6_android.Activity.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.pbl6_android.R;
import com.example.pbl6_android.adapters.NotReviewedProductAdapter;
import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReviewPastProduct extends AppCompatActivity {

    NotReviewedProductAdapter adapter;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";
    private RecyclerView recyclerView;


    List<Product> productList;
    private static final int PAGE_SIZE = 4;

    private void fetchNotReviewProduct(int page) {
        Call<List<Product>> call = retrofitInterface.getProductsNotYetReview(page,PAGE_SIZE);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body();
                    productList.addAll(products);
                    adapter.notifyDataSetChanged(); // Cập nhật dữ liệu trong adapter
                } else {
                    Toast.makeText(ReviewPastProduct.this, "Không có sản phẩm nào", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("API_ERROR", "Error fetching products: " + t.getMessage());
                Toast.makeText(ReviewPastProduct.this, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        productList = new ArrayList<>();
        recyclerView = findViewById(R.id.not_review_product_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotReviewedProductAdapter(this,productList);
        recyclerView.setAdapter(adapter);

        ImageButton back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Lấy dữ liệu ban đầu
        fetchNotReviewProduct(1);

    }
}