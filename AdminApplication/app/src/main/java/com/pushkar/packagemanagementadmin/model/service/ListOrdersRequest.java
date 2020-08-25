package com.pushkar.packagemanagementadmin.model.service;

public class ListOrdersRequest {
    private String storeNumber;

    public ListOrdersRequest(String storeNumber) {
        this.storeNumber = storeNumber;
    }

    public String getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }
}
