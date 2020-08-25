package com.pushkar.packagemanagementadmin.service;

import com.pushkar.packagemanagementadmin.model.service.EncryptRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {
    @POST("/admin/login")
    Call<ResponseBody> login(@Body EncryptRequest loginRequest);

    @POST("/admin/logout")
    Call<ResponseBody> logout(@Body EncryptRequest logoutRequest);

}
