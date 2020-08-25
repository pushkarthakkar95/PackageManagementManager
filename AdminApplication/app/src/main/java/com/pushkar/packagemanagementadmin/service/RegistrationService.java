package com.pushkar.packagemanagementadmin.service;

import com.pushkar.packagemanagementadmin.model.service.EncryptRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RegistrationService {
    @POST("admin/register/{storeNumber}")
    Call<ResponseBody> register(@Path("storeNumber") String storeNumber, @Body EncryptRequest request);
}
