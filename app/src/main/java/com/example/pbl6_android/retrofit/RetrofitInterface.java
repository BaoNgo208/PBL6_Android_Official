package com.example.pbl6_android.retrofit;

import com.example.pbl6_android.models.Cart;
import com.example.pbl6_android.models.Order;
import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.models.Promotion;
import com.example.pbl6_android.models.Review;
import com.example.pbl6_android.models.User1Dto;
import com.example.pbl6_android.models.User2Dto;

import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @GET("/Cart/getAllProduct")
    Call<List<Product>> getAllProduct(@Query("page") int page, @Query("size") int size);

    @GET("/Cart/getAllPromotion")
    Call<List<Promotion>> getAllPromotion(@Query("page") int page, @Query("size") int size);

    @POST("/Cart/checkout")
    Call<ResponseBody> checkout(@Query("payment") String paymentMethod, @Body Order order);

    @GET("/Order/getOrderByStatus")
    Call<List<Order>> getOrderByStatus(@Query("status") String status , @Query("page") int page, @Query("size") int size);

    @GET("/api/Product/getProductByName")
    Call<List<Product>> getProductByName(@Query("productName") String name, @Query("page") int page, @Query("size") int size);

    @GET("/api/Product/getProductTrending")
    Call<List<Product>> getProductTrending(@Query("page") int page, @Query("size") int size);

    @GET("/api/Product/getProductNew")
    Call<List<Product>> getProductNew(@Query("page") int page, @Query("size") int size);

    @GET("/Review/{productId}")
    Call<List<Review>> getReviewsByProduct(@Path("productId") UUID productId, @Query("page")int page, @Query("size") int size);

    @POST("/User/rate")
    Call<Review> createReview(@Body Review newReview);

    @GET("/User/{username}")
    Call<User1Dto> getUserDetails(@Path("username") String username);
    @POST("/User/update/{username}")
    Call<Void> updateUser(
            @Query("username") String username,
            @Query("password") String password,
            @Query("email") String email
    );

    @POST("/User/update-by-username/{username}")
    Call<Void> updateUserInfo(@Path("username") String username, @Body User2Dto updatedInfo);

    @GET("/api/Product/getProductByCategory")

    Call<List<Product>> getProductsByCategory(@Query("categoryName") String categoryName,@Query("page") int page, @Query("size") int size);
    @GET("/api/Product/getProductByCategory")
    Call<List<Product>> getAllProductsByCategory(@Query("categoryName") String categoryName, @Query("getAll") boolean getAll);

    @GET("/Promotion/getPromotionCode/{code}")
    Call<Promotion> getPromotionCode(@Path("code") String code);

    @POST("/Cart/addProductToCart")
    Call<Cart> addProductToCart(@Query("productId") UUID productId);

    @GET("/Cart/getCartOfUser")
    Call<Cart> getCartOfUser(@Query("userId") UUID userId);

    @GET("/api/Product/getProductNotYetReview")
    Call<List<Product>> getProductsNotYetReview(@Query("page") int page, @Query("size") int size);

    @GET("/api/Product/getProductSuggestedByCategory")
    Call<List<Product>> getProductSuggestedByCategory(@Query("userId") UUID userId,@Query("page") int page, @Query("size") int size);

}
