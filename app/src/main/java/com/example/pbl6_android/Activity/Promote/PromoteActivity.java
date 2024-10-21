package com.example.pbl6_android.Activity.Promote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.pbl6_android.R;
import com.example.pbl6_android.adapters.PromoteAdapter;
import com.example.pbl6_android.models.PageState;
import com.example.pbl6_android.models.Promotion;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PromoteActivity extends AppCompatActivity {


    private PageState pageState;

    RecyclerView discountRec;

    List<Promotion> promotionList;

    TextView viewMoreCode;
    PromoteAdapter promoteAdapter;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";

    private static final int PAGE_SIZE = 4;


    private void fetchRecommendedPromotion(int page) {
        Call<List<Promotion>> call = retrofitInterface.getAllPromotion(page,PAGE_SIZE);
        call.enqueue(new Callback<List<Promotion>>() {
            @Override
            public void onResponse(Call<List<Promotion>> call, Response<List<Promotion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Promotion> fetchedPromtions = response.body();
                    System.out.println("fetch size:"+fetchedPromtions.size());
                    System.out.println("content:"+fetchedPromtions.get(0).getContent());
                    System.out.println("code:"+fetchedPromtions.get(0).getCode());

                    promotionList.addAll(fetchedPromtions);
                    promoteAdapter.notifyDataSetChanged();

                    if (fetchedPromtions.size() < PAGE_SIZE) {
                        pageState.isLastPage = true;
                    }
                    pageState.isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<List<Promotion>> call, Throwable t) {

            }
        });
    }
    private void loadMoreItems(PageState state) {
        state.isLoading = true;
        state.currentPage++;
        fetchRecommendedPromotion(state.currentPage);
    }

    private void setupViewMore(TextView viewMoreButton, PageState state) {
        viewMoreButton.setOnClickListener(v -> {
            if (!state.isLoading && !state.isLastPage) {
                loadMoreItems(state); // Fetch more products on click
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
        promotionList = new ArrayList<>();
        fetchRecommendedPromotion(1);


        discountRec = findViewById(R.id.promote_rec);
        promoteAdapter = new PromoteAdapter(this, promotionList);
        discountRec.setAdapter(promoteAdapter);
        discountRec.setLayoutManager(new GridLayoutManager(this, 1));

        pageState = new PageState();
        pageState.currentPage = 1;  // Set initial page

        viewMoreCode = findViewById(R.id.view_more_promotion);
        setupViewMore(viewMoreCode,pageState);

    }
}