package com.pushkar.packagemanagementadmin.model.service;


public class LoginRequestData {

    private String employeeId;

    private String password;

    private String deviceToken;

    public LoginRequestData(String employeeId, String password, String deviceToken) {
        this.employeeId = employeeId;
        this.password = password;
        this.deviceToken = deviceToken;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getPassword() {
        return password;
    }


}
