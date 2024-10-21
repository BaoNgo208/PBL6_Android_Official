package com.example.pbl6_android.Activity.Product;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pbl6_android.R;
import com.example.pbl6_android.SelectPaymentMethodActivity;
import com.example.pbl6_android.models.CartService;
import com.example.pbl6_android.models.Product;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import java.text.DecimalFormat;

import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    private String BASE_URL = "http://10.0.2.2:5273/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Product product = getIntent().getParcelableExtra("product");
        System.out.println("get product name:" + product.getName());
        System.out.println("product id before:"+product.getProductId());

        TextView title = findViewById(R.id.detail_title);
        title.setText(product.getName());

        double priceDB = product.getPrice();

        System.out.println("get product price:" + product.getPrice());

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

    }
}