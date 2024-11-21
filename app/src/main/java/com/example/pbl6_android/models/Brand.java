package com.example.pbl6_android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Brand implements Parcelable {

    private String brandId;
    private String brandName;

    @SerializedName("locations")
    private List<Location> Locations;
    public Brand(String brandId, String brandName, List<Location> locations) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.Locations = locations;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public Brand(String brandName, List<Location> locations) {
        this.brandName = brandName;
        Locations = locations;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<Location> getLocations() {
        return Locations;
    }

    public void setLocations(List<Location> locations) {
        Locations = locations;
    }

    // Phương thức mô tả các đặc tính của đối tượng.
    @Override
    public int describeContents() {
        return 0;
    }

    // Ghi các thuộc tính vào Parcel.
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(brandId);
        dest.writeString(brandName);  // Ghi tên thương hiệu

        if (Locations == null || Locations.isEmpty()) {
            Log.e("Brand Parcel", "Locations is null or empty while writing to Parcel.");
        } else {
            for (Location location : Locations) {
                Log.d("Brand Parcel", "Writing Location: " + location.getName() + ", " + location.getDescription());
            }
        }
        dest.writeTypedList(Locations);  // Ghi danh sách các địa điểm (List<Location>)
    }

    // Phương thức để phục hồi đối tượng từ Parcel
    protected Brand(Parcel in) {
        brandId=in.readString();
        brandName = in.readString();  // Đọc tên thương hiệu
        Locations = in.createTypedArrayList(Location.CREATOR);  // Đọc danh sách các địa điểm (List<Location>)
        if (Locations == null || Locations.isEmpty()) {
            Log.e("Brand check:", "Locations is null or empty in Parcel.");
        } else {
            for (Location loc : Locations) {
                Log.d("Brand check:", "Location: " + loc.getName() + ", " + loc.getDescription());
            }
        }
    }

    // Creator để tạo đối tượng Brand từ Parcel.
    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
        @Override
        public Brand createFromParcel(Parcel in) {
            return new Brand(in);
        }

        @Override
        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };
}
