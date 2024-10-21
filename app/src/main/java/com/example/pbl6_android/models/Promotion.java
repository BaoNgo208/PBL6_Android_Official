package com.example.pbl6_android.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Promotion {

    private UUID PromotionId ;
    @SerializedName("code")
    private String Code ;

    private String content;

    @SerializedName("percentage")
    private Double Percentage ;

    public UUID getPromotionId() {
        return PromotionId;
    }

    public void setPromotionId(UUID promotionId) {
        PromotionId = promotionId;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public Double getPercentage() {
        return Percentage;
    }

    public void setPercentage(Double percentage) {
        Percentage = percentage;
    }

    public LocalDateTime getStartDate() {
        return StartDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        StartDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return EndDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        EndDate = endDate;
    }

    public Integer getMaxUsage() {
        return MaxUsage;
    }

    public void setMaxUsage(Integer maxUsage) {
        MaxUsage = maxUsage;
    }

    public List<Order> getOrders() {
        return Orders;
    }

    public void setOrders(List<Order> orders) {
        Orders = orders;
    }

    private LocalDateTime StartDate ;
    private   LocalDateTime EndDate ;

    private Integer MaxUsage ;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Promotion(UUID promotionId, String code, String content, Double percentage, LocalDateTime startDate, LocalDateTime endDate, Integer maxUsage) {
        PromotionId = promotionId;
        Code = code;
        this.content = content;
        Percentage = percentage;
        StartDate = startDate;
        EndDate = endDate;
        MaxUsage = maxUsage;
    }

    public List<Order> Orders ;
}
