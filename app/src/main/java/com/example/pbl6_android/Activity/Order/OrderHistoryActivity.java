package com.example.pbl6_android.Activity.Order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.pbl6_android.R;
import com.example.pbl6_android.adapters.OrderHistoryAdapter;
import com.example.pbl6_android.models.Order;
import com.example.pbl6_android.models.PageState;
import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.retrofit.RetrofitInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class    OrderHistoryActivity extends AppCompatActivity {
    private PageState pageState;
    List<Order> orderList;

    RecyclerView orderHistoryRec;
    OrderHistoryAdapter orderHistoryAdapter;
    private  String STATUS ;


    //Retrofit fetch api backend
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";


    private static final int PAGE_SIZE = 20;


    private void fetchRecommendedProducts(int page) {
        STATUS= getIntent().getStringExtra("STATUS");

        String DbStatus = null;
        if(Objects.equals(STATUS, "Đang chờ")) {
            DbStatus = "Pending";
        }
        else if(Objects.equals(STATUS,"Đã thanh toán")) {
            DbStatus = "done";
        }
        else {
            DbStatus = "cancelled";
        }

        Call<List<Order>> call = retrofitInterface.getOrderByStatus(DbStatus,page, PAGE_SIZE);

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                System.out.println("check call backend:");
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> fetchedProducts = response.body();
                    System.out.println("order ammount:" + fetchedProducts.get(0).getTotalAmount());

//                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                    String jsonResponse = gson.toJson(response.body());

//                    System.out.println("order object:" +jsonResponse);
                    orderList.addAll(fetchedProducts);
                    orderHistoryAdapter.notifyDataSetChanged();

                    if (fetchedProducts.size() < PAGE_SIZE) {
                        pageState.isLastPage = true;
                    }
                    pageState.isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                pageState.isLoading = false;
                System.err.println("API call failed: fetch history failed " + t.getMessage());
                t.printStackTrace();
                pageState.isLoading = false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        orderList =new ArrayList<>();
        fetchRecommendedProducts(1);

        orderHistoryRec = findViewById(R.id.recyclerView_history);
        orderHistoryAdapter = new OrderHistoryAdapter(this,orderList);

        orderHistoryRec.setAdapter(orderHistoryAdapter);
        orderHistoryRec.setLayoutManager(new GridLayoutManager(this, 1));

        pageState = new PageState();
        pageState.currentPage = 1;  // Set initial page


        ImageView back = findViewById(R.id.history_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}