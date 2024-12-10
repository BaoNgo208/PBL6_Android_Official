package com.example.pbl6_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pbl6_android.models.LoginRequest;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private String BASE_URL = "http://10.0.2.2:5273/";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Button loginBtn = findViewById(R.id.login_btn);

        EditText username = findViewById(R.id.editTextTextPersonName2);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRequest loginRequest = new LoginRequest(
                        ((EditText)findViewById(R.id.editTextTextPersonName2)).getText().toString(),
                        ((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString()
                );
                Call<Object> call = retrofitInterface.login(loginRequest);
                call.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Intent i = new Intent(MainActivity.this, Home.class);
                            startActivity(i);
                        }

                        else {
                            Toast.makeText(MainActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Lỗi mạng,xin vui lòng thử lại sau", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }


}