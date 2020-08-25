package com.pushkar.packagemanagementadmin.model.service;

import com.google.gson.annotations.SerializedName;
import com.pushkar.packagemanagementadmin.model.PackageOrder;

import java.util.List;

public class UpdatePackageStatusRequest {
    @SerializedName("lists")
    private List<UpdatePackageStatusDHO> packageOrderList;

    public UpdatePackageStatusRequest(List<UpdatePackageStatusDHO> packageOrderList) {
        this.packageOrderList = packageOrderList;
    }

    public List<UpdatePackageStatusDHO> getPackageOrderList() {
        return packageOrderList;
    }

    public void setPackageOrderList(List<UpdatePackageStatusDHO> packageOrderList) {
        this.packageOrderList = packageOrderList;
    }
}
