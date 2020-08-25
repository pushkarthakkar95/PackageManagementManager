package com.pushkar.packagemanagementadmin.model.service;

import com.google.gson.annotations.SerializedName;

public class LoginSuccessResponse implements IResponse {

    @SerializedName("id")
    private String employeeId;

    @SerializedName("name")
    private String employeeName;

    @SerializedName("storeNumber")
    private String storeNumber;

    @SerializedName("deviceToken")
    private String deviceToken;

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getStoreNumber() {
        return storeNumber;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }


    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
