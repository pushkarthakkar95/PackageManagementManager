package com.pushkar.packagemanagementadmin.model.service;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestResponse implements Serializable, IResponse {

    @SerializedName("id")
    private String employeeId;

    @SerializedName("name")
    private String name;

    @SerializedName("storeNumber")
    private String storeNumber;


    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

}
