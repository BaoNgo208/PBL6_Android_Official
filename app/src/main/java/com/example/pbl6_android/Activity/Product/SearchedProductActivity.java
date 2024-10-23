package com.example.pbl6_android.Activity.Product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pbl6_android.R;
import com.example.pbl6_android.adapters.RecommendedProductAdapter;
import com.example.pbl6_android.models.PageState;
import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchedProductActivity extends AppCompatActivity implements RecommendedProductAdapter.OnProductClickListener {

    private PageState pageState;
    private PageState newPageState;

    RecyclerView searchedProductRec;
    List<Product> productList;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    RecommendedProductAdapter recommendedProductAdapter;

    private String BASE_URL = "http://10.0.2.2:5273/";

    private static final int PAGE_SIZE = 4;

    private void fetchSearchedProducts(int page) {
        String searchQuery = getIntent().getStringExtra("product");
        Call<List<Product>> call = retrofitInterface.getProductByName(searchQuery,page, PAGE_SIZE);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> fetchedProducts = response.body();

                    productList.addAll(fetchedProducts);
                    recommendedProductAdapter.notifyDataSetChanged();

                    if (fetchedProducts.size() < PAGE_SIZE) {
                        pageState.isLastPage = true;
                    }
                    pageState.isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                pageState.isLoading = false;
            }
        });
    }

    private void loadMoreSearchedItems(PageState state) {
        state.isLoading = true;
        state.currentPage++;
        fetchSearchedProducts(state.currentPage);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_product);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        productList =new ArrayList<>();
        fetchSearchedProducts(1);

        searchedProductRec = findViewById(R.id.searched_product);

        recommendedProductAdapter = new RecommendedProductAdapter(this, productList,this);
        searchedProductRec.setAdapter(recommendedProductAdapter);
        searchedProductRec.setLayoutManager(new GridLayoutManager(this, 2));

        pageState = new PageState();
        pageState.currentPage = 1;  // Set initial page

        searchedProductRec.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!pageState.isLoading && !pageState.isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= PAGE_SIZE) {
                        loadMoreSearchedItems(pageState);
                    }
                }
            }
        });


        ImageView back = findViewById(R.id.searh_result_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("product",product);
        startActivity(intent);
    }
}