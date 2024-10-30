package com.example.pbl6_android.Activity.Product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pbl6_android.R;
import com.example.pbl6_android.SelectPaymentMethodActivity;
import com.example.pbl6_android.adapters.ReviewAdapter;
import com.example.pbl6_android.models.CartService;
import com.example.pbl6_android.models.PageState;
import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.models.Review;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";


    private PageState pageState;
    RecyclerView reviewRec;
    ReviewAdapter reviewAdapter;
    List<Review> reviewList;
    Button viewMoreButton;
    private static final int PAGE_SIZE = 5;

    UUID productId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Product product = getIntent().getParcelableExtra("product");
        productId = product.getProductId();


        reviewList = new ArrayList<>();
        fetchReviews(1);
        reviewRec = findViewById(R.id.review_recycler_view);


        reviewAdapter = new ReviewAdapter(this,reviewList);
        reviewRec.setAdapter(reviewAdapter);
        reviewRec.setLayoutManager(new GridLayoutManager(this,1));
        pageState = new PageState();
        pageState.currentPage = 1;  // Set initial page


        TextView title = findViewById(R.id.detail_title);
        title.setText(product.getName());

        double priceDB = product.getPrice();


        long roundedPrice = (long) priceDB;


        String formattedPrice = new DecimalFormat("#,###").format(roundedPrice) + " đồng";





        TextView price = findViewById(R.id.detail_price);
        price.setText(formattedPrice );

        TextView descript = findViewById(R.id.detail_des);
        descript.setText(product.getDescription());

        ImageView productImage = findViewById(R.id.detail_image);
        int imageResource = getResources().getIdentifier(product.getImageUrl(), "drawable", getPackageName());

        Glide.with(this).load(imageResource).into(productImage);

        //nut back ve trang truoc
        ImageView back = findViewById(R.id.detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //nut mua hang(check out)
        Button  checkoutButton  =  findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, SelectPaymentMethodActivity.class);
                intent.putExtra("product", (Parcelable) product);
                startActivity(intent);
            }
        });

        ImageView addToCart = findViewById(R.id.addToCart_btn);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailActivity.this, CartService.class);
                intent.setAction("ADD_TO_CART");
                intent.putExtra("product", product);
                startService(intent);

                Toast.makeText(DetailActivity.this,
                        "Đã thêm vào giỏ hàng",
                        Toast.LENGTH_SHORT).show();
            }
        });


        viewMoreButton = findViewById(R.id.view_more_button); // Initialize the button
        viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pageState.isLoading && !pageState.isLastPage) {
                    loadMoreReviews(pageState); // Load more reviews when button is clicked
                } else if (pageState.isLastPage) {
                    Toast.makeText(DetailActivity.this, "No more reviews to load.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button commentButton = findViewById(R.id.submit_comment_button);
        TextView commentInput = findViewById(R.id.comment_input);

        RatingBar ratingBar = findViewById(R.id.comment_rating_bar);

        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Review newReview = new Review(
                    productId,
                        (int) ratingBar.getRating(),
                    commentInput.getText().toString()

                );
                Toast.makeText(DetailActivity.this, "Review added successfully!", Toast.LENGTH_SHORT).show();
                createReview(newReview);
            }
        });



    }

    private void createReview(Review newReview) {
        Call<Review> call = retrofitInterface.createReview(newReview);
        System.out.println("called");
        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if(response.code() == 201 && response.body() != null) {
                    System.out.println("called successfully");
                    reviewList.add(response.body());
                    System.out.println("comment:"+response.body().getComment());
                    reviewAdapter.notifyDataSetChanged();
                }
                else {
                    System.out.println("call failed with code: " + response.code());
                    // Log the response body if it exists
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                            System.out.println("Error response: " + errorResponse);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Response body is null.");
                    }
                }
            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                Log.e("API_ERROR", "Failed to create reviews: " + t.getMessage(), t);
            }
        });
    }


    private void fetchReviews(int page) {
        Call<List<Review>> call = retrofitInterface.getReviewsByProduct(productId,page, PAGE_SIZE);
        call.enqueue(new Callback<List<Review>>() {

            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Review> fetchedReview = response.body();

                    reviewList.addAll(fetchedReview);
                    reviewAdapter.notifyDataSetChanged();

                    if (fetchedReview.size() < PAGE_SIZE) {
                        pageState.isLastPage = true;
                    }
                    pageState.isLoading = false;
                }


            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                pageState.isLoading = false;
                Log.e("API_ERROR", "Failed to fetch reviews: " + t.getMessage(), t);
            }
        });
    }

    private void loadMoreReviews(PageState state) {
        state.isLoading = true;
        state.currentPage++;
        fetchReviews(state.currentPage);
    }


}