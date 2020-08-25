package com.pushkar.packagemanagementadmin.service;

import com.pushkar.packagemanagementadmin.model.service.EncryptRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface PackageService {
    @POST("/package/all/storeNumber")
    Call<ResponseBody> getAllPackagesForStore(@Body EncryptRequest request);

    @PUT("/package/possession/status/trackingNumber")
    Call<ResponseBody> takePossessionOfPackages(@Body EncryptRequest request);

    @PUT("/package/delivered/status/trackingNumber")
    Call<ResponseBody> deliverPackages(@Body EncryptRequest request);
}
