package com.example.pbl6_android;
public class CartItem {
    private String productName;
    private int productPrice;
    private Integer productQuantity;
    private Integer productImage; // Có thể là id của ảnh từ drawable

    public CartItem(String productName, int productPrice, int productQuantity, int productImage) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public int getProductImage() {
        return productImage;
    }

    public void increaseQuantity() {
        this.productQuantity++;
    }

    public void decreaseQuantity() {
        if (this.productQuantity > 1) {
            this.productQuantity--;
        }
    }
}
