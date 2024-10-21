package com.example.pbl6_android.models;

public class PageState {
   public int currentPage = 1;
    public boolean isLoading = false;
    public boolean isLastPage = false;
    public final int totalPage = 2;

    // Constructor
    public PageState() {}




    // Optionally, you can add a reset method to reset the state
    public void resetState() {
        this.currentPage = 1;
        this.isLoading = false;
        this.isLastPage = false;
    }
}
