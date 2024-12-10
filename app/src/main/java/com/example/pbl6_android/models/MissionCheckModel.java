package com.example.pbl6_android.models;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class MissionCheckModel {
    @SerializedName("result")
    private Map<String, String> result;

    // Getter and Setter
    public Map<String, String> getResult() {
        return result;
    }

    public void setResult(Map<String, String> result) {
        this.result = result;
    }
}
