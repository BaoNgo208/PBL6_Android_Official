package com.example.pbl6_android.Activity.User;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pbl6_android.R;
import com.example.pbl6_android.models.User1Dto;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileActivity2 extends AppCompatActivity {
    private TextView tvUserName,tvName, tvEmail,ivAvatar, tvMobile, tvAddress;
    private TextView btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile2);
    }
}