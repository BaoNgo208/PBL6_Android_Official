package com.example.pbl6_android.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductWithQuantity implements Parcelable {
    private Product product;
    private int quantity;

    public ProductWithQuantity(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    protected ProductWithQuantity(Parcel in) {
        product = in.readParcelable(Product.class.getClassLoader());
        quantity = in.readInt();
    }

    public static final Creator<ProductWithQuantity> CREATOR = new Creator<ProductWithQuantity>() {
        @Override
        public ProductWithQuantity createFromParcel(Parcel in) {
            return new ProductWithQuantity(in);
        }

        @Override
        public ProductWithQuantity[] newArray(int size) {
            return new ProductWithQuantity[size];
        }
    };

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(product, flags);
        dest.writeInt(quantity);
    }
}
