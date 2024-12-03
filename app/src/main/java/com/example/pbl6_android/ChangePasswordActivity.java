package com.example.pbl6_android;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pbl6_android.retrofit.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePasswordActivity extends AppCompatActivity {
    private EditText editPassword, editEmail;
    private Button btnSubmit;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        String email = intent.getStringExtra("email");

        // Tham chiếu các thành phần UI
        backButton = findViewById(R.id.back_button);
        editPassword = findViewById(R.id.edit_password);
        editEmail = findViewById(R.id.edit_email);
        btnSubmit = findViewById(R.id.btn_submit);

        // Đặt giá trị mặc định cho các EditText
        editPassword.setText(password);
        editEmail.setText(email);

        // Thiết lập sự kiện cho nút "Trở về"
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay lại màn hình trước
                onBackPressed();
            }
        });

        // Xử lý sự kiện nhấn nút Submit
        btnSubmit.setOnClickListener(v -> {
            String password_update = editPassword.getText().toString();
            String email_update = editEmail.getText().toString();

            if (TextUtils.isEmpty(password_update) || TextUtils.isEmpty(email_update)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi API để cập nhật mật khẩu và email (nếu cần)
            updatePasswordAndEmail(username, password_update, email_update);
        });
    }

    private void updatePasswordAndEmail(String username, String password, String email) {
        // Gọi API cập nhật thông tin
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5273/") // URL của API
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface userService = retrofit.create(RetrofitInterface.class);
        Call<Void> call = userService.updateUser(username, password, email);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        finish(); // Quay lại màn hình trước
    }
}
