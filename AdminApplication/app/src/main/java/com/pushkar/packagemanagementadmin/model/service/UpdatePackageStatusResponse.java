package com.pushkar.packagemanagementadmin.model.service;

import java.util.List;

public class UpdatePackageStatusResponse implements IResponse {
    private List<String> details;
    private String message;

    public UpdatePackageStatusResponse(List<String> details, String message) {
        this.details = details;
        this.message = message;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
