package com.pushkar.packagemanagementadmin.model.service;

import com.google.gson.annotations.SerializedName;

public class EncryptRequest {
    @SerializedName("decryptString")
    private String encryptString;

    public EncryptRequest(String encryptString) {
        this.encryptString = encryptString;
    }

    public EncryptRequest(){}

    public String getEncryptString() {
        return encryptString;
    }

    public void setEncryptString(String encryptString) {
        this.encryptString = encryptString;
    }
}
