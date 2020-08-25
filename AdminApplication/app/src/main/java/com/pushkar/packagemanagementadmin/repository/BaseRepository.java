package com.pushkar.packagemanagementadmin.repository;

import com.pushkar.packagemanagementadmin.service.LoginService;
import com.pushkar.packagemanagementadmin.service.PackageService;
import com.pushkar.packagemanagementadmin.service.RegistrationService;
import com.pushkar.packagemanagementadmin.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseRepository {

    protected LoginService getLoginService(){
        return getRetrofitBuilder().create(LoginService.class);
    }

    protected PackageService getPackageService(){
        return getRetrofitBuilder().create(PackageService.class);
    }

    protected RegistrationService getRegistrationService(){
        return getRetrofitBuilder().create(RegistrationService.class);
    }

    private OkHttpClient getHttpClient(){
        return new OkHttpClient.Builder().build();
    }

    private Retrofit getRetrofitBuilder(){
        return new Retrofit.Builder()
                .baseUrl(Constants.SERVICE_BASE_URL)
                .client(getHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
