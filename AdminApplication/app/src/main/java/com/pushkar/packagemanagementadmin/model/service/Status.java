package com.pushkar.packagemanagementadmin.model.service;

public enum Status {
    IN_TRANSIT("IN_TRANSIT"),
    DELIVERED("DELIVERED"),
    IN_STORE("IN_STORE");

    private final String name;
    private Status(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
    }
    @Override
    public String toString() {
        return this.name;
    }
}
