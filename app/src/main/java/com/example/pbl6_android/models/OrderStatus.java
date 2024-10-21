package com.example.pbl6_android.models;

public class OrderStatus {
    private Integer image;
    private String name;

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderStatus(Integer image, String name) {
        this.image = image;
        this.name = name;
    }
}
