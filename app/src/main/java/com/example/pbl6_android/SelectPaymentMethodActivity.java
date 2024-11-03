package com.example.pbl6_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pbl6_android.models.Order;
import com.example.pbl6_android.models.OrderDetail;
import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.retrofit.RetrofitInterface;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.provider.Browser;


public class SelectPaymentMethodActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";
    String promtionId;

    private Order createOrder(Product selectedProduct) {
        System.out.println("final check:" +promtionId);
       return new Order("Pending", new ArrayList<OrderDetail>(){
            {
                add(new OrderDetail(1,selectedProduct.getProductId()));
            }
        }, promtionId);

    }

    private Order createOrderWithoutPromotion(Product selectedProduct) {

        return new Order("Pending", new ArrayList<OrderDetail>(){
            {
                add(new OrderDetail(1,selectedProduct.getProductId()));
            }
        });
    }

    private void sendOrderAndRedirect(String paymentMethod, Order order) {
        if (order.getPromotionId() == null) {
            order.setPromotionId(null); // Hoặc không gửi trường này trong yêu cầu
        }
        Call<ResponseBody> call = retrofitInterface.checkout(paymentMethod, order);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String redirectUrl = response.body().string();
                        System.out.println("URL: " + redirectUrl);




                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        finish();
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SelectPaymentMethodActivity.this,
                                "Lỗi khi lấy URL thanh toán",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        System.out.println("Error Code: " + response.code());
                        System.out.println("Error Body: " + errorBody);
                        Toast.makeText(SelectPaymentMethodActivity.this,
                                "Đã xảy ra lỗi: " + response.code() + " - " + errorBody,
                                Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SelectPaymentMethodActivity.this,
                                "Lỗi khi xử lý phản hồi: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SelectPaymentMethodActivity.this,
                        "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showHomeUIElements() {
        findViewById(R.id.home_hor_rec).setVisibility(View.VISIBLE);
        findViewById(R.id.adflipper).setVisibility(View.VISIBLE);
        findViewById(R.id.recommended_product).setVisibility(View.VISIBLE);
        findViewById(R.id.new_product).setVisibility(View.VISIBLE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment_method);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

//        Product product = (Product) getIntent().getSerializableExtra("product");

        Product product = getIntent().getParcelableExtra("product");
        promtionId = getIntent().getStringExtra("promotionId");

        System.out.println("product id:"+product.getProductId());
        System.out.println("product price after discount:"+promtionId);

//        webViewPayment = findViewById(R.id.webview_payment);
//        webViewPayment.setWebViewClient(new WebViewClient());
//        webViewPayment.getSettings().setJavaScriptEnabled(true);


        RadioGroup paymentMethods = findViewById(R.id.payment_methods);
        Button confirmButton = findViewById(R.id.btn_confirm_payment);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = paymentMethods.getCheckedRadioButtonId();


                if (selectedId != -1) {
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String selectedPaymentMethod = selectedRadioButton.getText().toString();
                    Order newOrder;
                    if(promtionId.isEmpty())  {
                        newOrder = createOrderWithoutPromotion(product);
                    }
                    else {
                        newOrder = createOrder(product);
                    }
                    Gson gson = new Gson();
                    String jsonOrder = gson.toJson(newOrder);

// Log chuỗi JSON
                    Log.d("OrderDetails", "New Order: " + jsonOrder);


                    System.out.println("final check 2:" + newOrder.getPromotionId());

                    switch (selectedPaymentMethod) {
                        case "ZaloPay":

                            sendOrderAndRedirect("ZaloPay", newOrder);
                            break;

                        case "Momo":
                            sendOrderAndRedirect("Momo", newOrder);
                            break;

                        case "VNPay":

                            sendOrderAndRedirect("VNP", newOrder);
                            break;
                    }
                } else {
                    Toast.makeText(SelectPaymentMethodActivity.this,
                            "Vui lòng chọn phương thức thanh toán!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
