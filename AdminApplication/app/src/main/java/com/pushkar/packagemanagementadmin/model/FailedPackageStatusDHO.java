package com.pushkar.packagemanagementadmin.model;

public class FailedPackageStatusDHO {
    private String trackingNumber;
    private String reason;

    public FailedPackageStatusDHO(String trackingNumber, String reason) {
        this.trackingNumber = trackingNumber;
        this.reason = reason;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public String getReason() {
        return reason;
    }
}
