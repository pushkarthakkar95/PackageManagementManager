package com.pushkar.packagemanagementadmin.model.service;

import com.google.gson.annotations.SerializedName;

public class UpdateDeviceTokenRequest {
    @SerializedName("id")
    private String employeeId;

    @SerializedName("deviceToken")
    private String deviceToken;

    public UpdateDeviceTokenRequest(String employeeId, String deviceToken) {
        this.employeeId = employeeId;
        this.deviceToken = deviceToken;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }
}
