package com.pushkar.packagemanagementadmin.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.pushkar.packagemanagementadmin.model.service.EncryptRequest;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.model.service.LogoutSuccessResponse;
import com.pushkar.packagemanagementadmin.service.EncryptionWrapper;
import com.pushkar.packagemanagementadmin.service.LoginService;
import com.pushkar.packagemanagementadmin.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivityRepository extends BaseRepository{
    private LoginService service;
    private static final String TAG = MainActivityRepository.class.getSimpleName();
    private Gson gson = new Gson();
    private static MainActivityRepository mainActivityRepository;
    private MutableLiveData<IResponse> responseMutableLiveData;
    public static MainActivityRepository getMainActivityRepository(){
         if(mainActivityRepository == null){
             synchronized (MainActivityRepository.class){
                 if(mainActivityRepository == null){
                     mainActivityRepository = new MainActivityRepository();
                 }
             }
         }
         return mainActivityRepository;
    }

    private MainActivityRepository(){
        responseMutableLiveData = new MutableLiveData<>();
        service = getLoginService();
    }

    public void attemptLogout(EncryptRequest request){
        service.logout(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    responseMutableLiveData.postValue(new LogoutSuccessResponse());
                }else{
                    try {
                        String errorResponseRawEncryptedStr = response.errorBody().string();
                        String decryptedErrorStr = EncryptionWrapper.decrypt(errorResponseRawEncryptedStr);
                        Log.d(TAG, "onResponse error decrypted string is: "+decryptedErrorStr);
                        ErrorResponse errorResponse = gson.fromJson(decryptedErrorStr,ErrorResponse.class);
                        responseMutableLiveData.postValue(errorResponse);
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: Exception"+e.getLocalizedMessage());
                        e.printStackTrace();
                        unexpectedErrorHandling();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage()+t.getMessage());
                unexpectedErrorHandling();
            }
        });
    }

    private void unexpectedErrorHandling(){
        ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                ,new ArrayList<>());
        responseMutableLiveData.postValue(errorResponse);
    }

    public MutableLiveData<IResponse> getResponseMutableLiveData() {
        return responseMutableLiveData;
    }
}
