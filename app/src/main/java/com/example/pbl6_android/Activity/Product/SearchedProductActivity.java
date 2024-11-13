package com.example.pbl6_android.Activity.Product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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



    @SuppressLint("ClickableViewAccessibility")
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


//        EditText editText = findViewById(R.id.searched_result_search_bar);
//
//
//        editText.setOnTouchListener((v, event) -> {
//            if (event.getAction() == MotionEvent.ACTION_UP) {
//                Drawable leftDrawable = editText.getCompoundDrawables()[0]; // Left drawable
//                if (leftDrawable != null) {
//                    int drawableWidth = leftDrawable.getBounds().width();
//                    int clickArea = editText.getPaddingLeft() + drawableWidth;
//
//                    if (event.getX() <= clickArea) {
//                        String searchQuery = editText.getText().toString().trim();
//
//                        if (!searchQuery.isEmpty()) {
//                            Intent intent = new Intent(editText.getContext(), SearchedProductActivity.class);
//                            intent.putExtra("product", searchQuery);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//                            ((Activity) editText.getContext()).finish();
//
//                            editText.getContext().startActivity(intent);
//
//                            return true;
//                        }
//                    }
//                }
//            }
//            return false;
//        });


        SearchView searchView = findViewById(R.id.searched_result_search_bar);
        boolean[] isActivityStarted = {false}; // Use an array or a boolean reference to modify within lambda

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!isActivityStarted[0] && !query.trim().isEmpty()) { // Check if activity not already started
                    isActivityStarted[0] = true; // Set flag to true

                    Intent intent = new Intent(searchView.getContext(), SearchedProductActivity.class);
                    intent.putExtra("product", query.trim());
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    searchView.clearFocus(); // Clear focus to avoid reopening

                    ((Activity) searchView.getContext()).finish();
                    searchView.getContext().startActivity(intent);

                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false; // No action on text change
            }
        });

// Reset the flag when returning to this activity
        searchView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                isActivityStarted[0] = false; // Reset the flag when focus is lost
            }
        });

        ImageView back = findViewById(R.id.search_result_back);
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