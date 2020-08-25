package com.pushkar.packagemanagementadmin.model.service;

import com.google.gson.annotations.SerializedName;

public class LogoutRequest {
    @SerializedName("id")
    private String employeeId;

    @SerializedName("deviceToken")
    private String deviceToken;

    public LogoutRequest(String employeeId, String deviceToken) {
        this.employeeId = employeeId;
        this.deviceToken = deviceToken;
    }
}
