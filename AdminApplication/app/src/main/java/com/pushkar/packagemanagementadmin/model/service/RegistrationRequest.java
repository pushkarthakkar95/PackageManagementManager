package com.pushkar.packagemanagementadmin.model.service;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class RegistrationRequest {
    @SerializedName("name")
    private String name;

    @SerializedName("password")
    private String password;


    public RegistrationRequest(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"name\" :");
        builder.append(name);
        builder.append(", \"password\" :");
        builder.append(password);
        builder.append("}");
        return builder.toString();
    }
}
