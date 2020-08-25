package com.pushkar.packagemanagementadmin.model.service;

public class UpdatePackageStatusDHO {
    private String trackingNumber;
    private String storeId;
    private String deliveryDate;

    public UpdatePackageStatusDHO(String trackingNumber, String storeId) {
        this.trackingNumber = trackingNumber;
        this.storeId = storeId;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
