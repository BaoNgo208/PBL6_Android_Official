package com.example.pbl6_android.models;

public class LoginRequest {
    private String Username;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginRequest(String username, String password) {
        Username = username;
        this.password = password;
    }

    private String password;

}
