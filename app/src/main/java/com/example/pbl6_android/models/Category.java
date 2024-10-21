package com.example.pbl6_android.models;

public class Category {
    int image;
    String name;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category(int image, String name) {
        this.image = image;
        this.name = name;
    }
}
