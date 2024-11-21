package com.example.pbl6_android.Activity.Product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pbl6_android.CartActivity;
import com.example.pbl6_android.OrderSummaryActivity;
import com.example.pbl6_android.R;

import com.example.pbl6_android.adapters.ReviewAdapter;
import com.example.pbl6_android.models.Cart;
import com.example.pbl6_android.models.CartService;
import com.example.pbl6_android.models.PageState;
import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.models.Review;
import com.example.pbl6_android.retrofit.RetrofitInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";
    private GoogleMap googleMap;

    private PageState pageState;
    RecyclerView reviewRec;
    ReviewAdapter reviewAdapter;
    List<Review> reviewList;
    Button viewMoreButton;
    List<Product> productItems;
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


        productItems = new ArrayList<>();
        Product product = getIntent().getParcelableExtra("product");
        productItems.add(product);
        productId = product.getProductId();

        Gson gson = new Gson();
        String json = gson.toJson(product);
        System.out.println("Parsed Product JSON from detail : " + json);

        Log.d("Brand Locations", new Gson().toJson(product.getBrand().getLocations()));


//        System.out.println("product brand:" + product.getBrand().getBrandName());
        System.out.println("product location:" + product.getBrand().getLocations().size());

        WebView webView = findViewById(R.id.webView);

        // Cấu hình WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);


        webView.setWebViewClient(new WebViewClient());


        String iframe = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/gDl3s-5x6wM?si=fyH8ZACz47ED_EYL\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";


        webView.loadDataWithBaseURL(null, iframe, "text/html", "utf-8", null);





        reviewList = new ArrayList<>();
        fetchReviews(1);
        reviewRec = findViewById(R.id.review_recycler_view);


        reviewAdapter = new ReviewAdapter(this,reviewList);
        reviewRec.setAdapter(reviewAdapter);
        reviewRec.setLayoutManager(new GridLayoutManager(this,1));
        pageState = new PageState();
        pageState.currentPage = 1;


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
                Intent intent = new Intent(DetailActivity.this, OrderSummaryActivity.class);
//                intent.putExtra("product", (Parcelable) product);
                intent.putParcelableArrayListExtra("productItems" , (ArrayList<? extends Parcelable>) productItems);
                ArrayList<Integer> quantitiesList = new ArrayList<>(Collections.singletonList(1));
                intent.putIntegerArrayListExtra("quantities", quantitiesList);
                startActivity(intent);
            }
        });

        ImageView addToCart = findViewById(R.id.addToCart_btn);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart(product.getProductId());

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


        SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void addToCart(UUID productId) {

        Call<Cart> call = retrofitInterface.addProductToCart(productId);
        call.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if(response.isSuccessful() && response.body() != null) {
                    Toast.makeText(DetailActivity.this,
                            "Đã thêm vào giỏ hàng",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Toast.makeText(DetailActivity.this,
                        "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void createReview(Review newReview) {
        Call<Review> call = retrofitInterface.createReview(newReview);
        call.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                if(response.code() == 201 && response.body() != null) {
                    reviewList.add(response.body());
                    reviewAdapter.notifyDataSetChanged();
                }
                else {
                    // Log the response body if it exists
                    if (response.errorBody() != null) {
                        try {
                            String errorResponse = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
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


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        String address = productItems.get(0).getBrand().getLocations().get(0).getName();
        geocodeAddress(address);
    }

    private void geocodeAddress(String address) {
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address location = addressList.get(0);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // Hiển thị Marker trên bản đồ
                LatLng latLng = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(latLng).title(address));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 22f)); // Zoom vào vị trí
            } else {
                Toast.makeText(this, "Không tìm thấy địa chỉ", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Geocoding", "Error getting location from address", e);
        }
    }
}