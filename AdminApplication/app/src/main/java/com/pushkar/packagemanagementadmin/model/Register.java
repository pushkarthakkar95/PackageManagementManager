package com.pushkar.packagemanagementadmin.model;

public class Register {
    private String name, password, confirmPassword, storeNum;

    public Register(String name, String password, String confirmPassword, String storeNum) {
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.storeNum = storeNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getStoreNum() {
        return storeNum;
    }

    public void setStoreNum(String storeNum) {
        this.storeNum = storeNum;
    }

}
