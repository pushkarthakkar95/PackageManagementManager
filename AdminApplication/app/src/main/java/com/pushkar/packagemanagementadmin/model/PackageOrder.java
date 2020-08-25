package com.pushkar.packagemanagementadmin.model;

import com.google.gson.annotations.SerializedName;
import com.pushkar.packagemanagementadmin.model.service.Status;

public class PackageOrder {
    private String trackingNumber;
    private String description;

    @SerializedName("status")
    private Status packageStatus;

    private String userEmail;
    private String name;

    public PackageOrder(String trackingNumber, String description, Status packageStatus, String userEmail, String name) {
        this.trackingNumber = trackingNumber;
        this.description = description;
        this.packageStatus = packageStatus;
        this.userEmail = userEmail;
        this.name = name;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(Status packageStatus) {
        this.packageStatus = packageStatus;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
