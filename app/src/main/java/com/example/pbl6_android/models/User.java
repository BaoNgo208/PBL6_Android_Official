package com.example.pbl6_android.models;

import java.util.UUID;



public class User {

    private UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public User(UUID userId, String userName, String email, UUID roleId) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.roleId = roleId;
    }

    private String userName;
    private String email;
    private UUID roleId;
}
