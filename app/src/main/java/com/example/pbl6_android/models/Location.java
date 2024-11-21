package com.example.pbl6_android.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Location implements Parcelable {
    private String locationId;
    private String name;
    private String description;

    public Location(String locationId, String name, String description) {
        this.locationId = locationId;
        this.name = name;
        this.description = description;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    // Constructor
    public Location(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getter và Setter cho name và description
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Constructor để khôi phục đối tượng từ Parcel
    protected Location(Parcel in) {
        locationId=in.readString();
        name = in.readString();         // Đọc tên địa điểm
        description = in.readString();  // Đọc mô tả địa điểm
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Ghi đối tượng vào Parcel
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(locationId);
        dest.writeString(name);          // Ghi tên địa điểm
        dest.writeString(description);   // Ghi mô tả địa điểm
    }

    // Creator để khôi phục đối tượng từ Parcel
    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);  // Tạo đối tượng Location từ Parcel
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}
