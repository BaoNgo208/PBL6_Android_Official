package com.example.pbl6_android.models;

public class User2Dto {
    private boolean gender;       // Thay đổi từ userName thành gender
    private String phoneNumber;
    private String address;
    private String firstName;
    private String lastName;

    // Getters và setters
    public boolean isGender() { return gender; }  // Getter cho kiểu boolean thường sử dụng "is"
    public void setGender(boolean gender) { this.gender = gender; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
