package com.example.pbl6_android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pbl6_android.models.User1Dto;
import com.example.pbl6_android.models.User2Dto;
import com.example.pbl6_android.retrofit.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SettingsFragment extends Fragment {
    private TextView tvUserName, tvName, tvEmail, tvMobile, tvAddress,tvgender,tvFirstName,tvLastName;
    private TextView btnChangePassword;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300; // Thời gian giữa hai lần nhấn để tính là đúp chuột (ms)
    private long lastClickTime = 0;
    private LinearLayout privateLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =inflater.inflate(R.layout.activity_user_profile2, container, false) ;

        // Khởi tạo các thành phần UI
        tvUserName = view.findViewById(R.id.change_Username_text);
        tvEmail = view.findViewById(R.id.textView10);
        tvMobile = view.findViewById(R.id.change_Mobile_text);
        tvAddress = view.findViewById(R.id.change_Address_text);
        tvName = view.findViewById(R.id.textView6);
        btnChangePassword = view.findViewById(R.id.Pass_text);
        tvgender = view.findViewById(R.id.change_Avatar_text);
        tvFirstName=view.findViewById(R.id.change_fistName_text);
        tvLastName=view.findViewById(R.id.change_lastName_text);
        privateLayout = view.findViewById(R.id.private_layout);

        privateLayout.setOnClickListener(v -> {
            showOldPasswordDialog();
        });


        String username = "user2"; // Thay thế bằng dữ liệu động nếu cần

        // Gọi API để lấy thông tin người dùng
        getUserDetails(username);
        ImageView pay = view.findViewById(R.id.payment);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "Đã click", Toast.LENGTH_SHORT).show();

            }
        });
        // Đặt sự kiện đúp chuột cho các trường thông tin
        setUpDoubleClickToEdit();
        return view;
    }
    private void getUserDetails(String username) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5273/") // Thay thế bằng URL cơ sở của bạn
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface userService = retrofit.create(RetrofitInterface.class);
        Call<User1Dto> call = userService.getUserDetails(username);

        // Thực hiện cuộc gọi API
        call.enqueue(new Callback<User1Dto>() {
            @Override
            public void onResponse(Call<User1Dto> call, Response<User1Dto> response) {
                if (response.isSuccessful()) {
                    // Lấy dữ liệu người dùng từ phản hồi và cập nhật giao diện
                    User1Dto user = response.body();

                    if (user != null) {
                        tvName.setText(user.getFirstName() + " " + user.getLastName());
                        tvEmail.setText(user.getEmail());
                        tvMobile.setText(user.getPhoneNumber());
                        tvAddress.setText(user.getAddress());
                        tvUserName.setText(user.getUserName());
                        tvgender.setText(user.getGender());
                        btnChangePassword.setText((user.getPasswordHash()));
                        tvFirstName.setText(user.getFirstName());
                        tvLastName.setText(user.getLastName());
                    }
                }

            }

            @Override
            public void onFailure(Call<User1Dto> call, Throwable t) {
                // Xử lý lỗi, ví dụ như hiển thị một Toast
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setUpDoubleClickToEdit() {
        // Mobile field
        tvMobile.setOnClickListener(v -> handleDoubleClick(tvMobile, "Enter Mobile"));

        // Address field
        tvAddress.setOnClickListener(v -> handleDoubleClick(tvAddress, "Enter Address"));

        // Gender field
        tvgender.setOnClickListener(v -> handleDoubleClick(tvgender, "Enter Gender"));

        // LastName field
        tvLastName.setOnClickListener(v -> handleDoubleClick(tvLastName, "Enter LastName"));
        // First field
        tvFirstName.setOnClickListener(v -> handleDoubleClick(tvFirstName, "Enter Gender"));
    }
    // Xử lý sự kiện đúp chuột
    private void handleDoubleClick(TextView textView, String hintText) {
        long currentClickTime = System.currentTimeMillis();
        if (currentClickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            // Ẩn TextView và hiển thị EditText
            textView.setVisibility(View.GONE);
            EditText editText = new EditText(getContext());
            editText.setText(textView.getText());
            editText.setHint(hintText);
            editText.setTextColor(Color.BLACK);
            editText.setHintTextColor(Color.GRAY);

            // Tạo nút Lưu
            Button saveButton = new Button(getContext());
            saveButton.setText("Lưu");

            // Tạo nút Hủy
            Button cancelButton = new Button(getContext());
            cancelButton.setText("Hủy");

            // Thêm các view vào giao diện
            ViewGroup parent = (ViewGroup) textView.getParent();
            parent.addView(editText);
            parent.addView(saveButton);
            parent.addView(cancelButton);

            // Xử lý sự kiện nút Lưu
            saveButton.setOnClickListener(v -> {
                // Lấy giá trị mới từ EditText
                String newValue = editText.getText().toString();


                // Tạo đối tượng cập nhật
                User2Dto updatedInfo = new User2Dto();
                if(tvgender.getText().toString()=="Nam"){
                    updatedInfo.setGender(true);
                }else{
                    updatedInfo.setGender(false);
                }
                updatedInfo.setPhoneNumber(tvMobile.getText().toString());
                updatedInfo.setAddress(tvAddress.getText().toString());
                updatedInfo.setFirstName(tvFirstName.getText().toString());
                updatedInfo.setLastName(tvLastName.getText().toString());
                switch (textView.getId()) {
                    case R.id.change_Mobile_text:
                        updatedInfo.setPhoneNumber(newValue);
                        break;
                    case R.id.change_Address_text:
                        updatedInfo.setAddress(newValue);
                        break;
                    case R.id.change_fistName_text:
                        updatedInfo.setFirstName(newValue);
                        break;
                    case R.id.change_lastName_text:
                        updatedInfo.setLastName(newValue);
                        break;
                    case R.id.change_Avatar_text:
                        boolean gender = newValue.equalsIgnoreCase("Nam");
                        updatedInfo.setGender(gender);
                        break;
                }
                // Gọi API cập nhật thông tin
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:5273/") // URL của API
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RetrofitInterface userService = retrofit.create(RetrofitInterface.class);
                Call<Void> call = userService.updateUserInfo(tvUserName.getText().toString(), updatedInfo);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                            textView.setText(newValue);
                        } else {
                            Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Hiển thị lại TextView và xóa các thành phần khác
                textView.setVisibility(View.VISIBLE);
                parent.removeView(editText);
                parent.removeView(saveButton);
                parent.removeView(cancelButton);
            });

            // Xử lý sự kiện nút Hủy
            cancelButton.setOnClickListener(v -> {
                textView.setVisibility(View.VISIBLE);
                parent.removeView(editText);
                parent.removeView(saveButton);
                parent.removeView(cancelButton);
            });

            editText.requestFocus();
        }
        lastClickTime = currentClickTime;
    }
    private void showOldPasswordDialog() {
        // Tạo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xác nhận mật khẩu cũ");

        // Thêm EditText vào AlertDialog
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setHint("Nhập mật khẩu cũ");
        builder.setView(input);

        // Thêm nút Xác nhận
        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String oldPassword = input.getText().toString();

            // Kiểm tra mật khẩu cũ
            checkOldPassword(oldPassword);
        });

        // Thêm nút Hủy
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        // Hiển thị hộp thoại
        builder.show();
    }
    private void checkOldPassword(String oldPassword) {
        // Ví dụ kiểm tra mật khẩu cũ (gọi API hoặc kiểm tra cục bộ)
        String correctPassword = btnChangePassword.getText().toString();

        if (oldPassword.equals(correctPassword)) {
            Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
            intent.putExtra("username", tvUserName.getText().toString());  // Truyền username
            intent.putExtra("password", btnChangePassword.getText().toString());  // Truyền password
            intent.putExtra("email", tvEmail.getText().toString());        // Truyền email
            startActivity(intent);
        } else {
            // Mật khẩu sai -> Hiển thị thông báo lỗi
            Toast.makeText(getContext(), "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show();
        }
    }
}