package com.pushkar.packagemanagementadmin.model.service;

import com.google.gson.annotations.SerializedName;
import com.pushkar.packagemanagementadmin.model.PackageOrder;

import java.util.List;

public class ListOrdersResponse implements IResponse{
    @SerializedName("lists")
    private List<PackageOrder> packageOrderList;

    public ListOrdersResponse(List<PackageOrder> packageOrderList) {
        this.packageOrderList = packageOrderList;
    }

    public List<PackageOrder> getPackageOrderList() {
        return packageOrderList;
    }

    public void setPackageOrderList(List<PackageOrder> packageOrderList) {
        this.packageOrderList = packageOrderList;
    }
}
