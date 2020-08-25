package com.pushkar.packagemanagementadmin.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.pushkar.packagemanagementadmin.model.service.RequestResponse;
import com.pushkar.packagemanagementadmin.model.service.EncryptRequest;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.service.EncryptionWrapper;
import com.pushkar.packagemanagementadmin.service.RegistrationService;
import com.pushkar.packagemanagementadmin.utils.Constants;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegistrationRepository extends BaseRepository{
    private static RegistrationRepository registrationRepository;
    private RegistrationService registrationService;
    private MutableLiveData<IResponse> adminResponseLiveData;
    private static final String TAG = RegistrationRepository.class.getSimpleName();
    private Gson gson = new Gson();

    public static RegistrationRepository getRegistrationRepository(){
        if(registrationRepository == null){
            synchronized (RegistrationRepository.class){
                if(registrationRepository == null){
                    registrationRepository = new RegistrationRepository();
                }
            }
        }

        return registrationRepository;
    }

    private RegistrationRepository(){
        adminResponseLiveData = new MutableLiveData<>();
        registrationService = getRegistrationService();
    }

    public void registerUser(String storeNum, EncryptRequest request){
        registrationService.register(storeNum,request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d(TAG,"onResponse successful");
                    try {
                        String encryptedResponseStr = response.body().string();
                        Log.d(TAG, "onResponse raw response string is: "+encryptedResponseStr);
                        String decryptedResponseStr = EncryptionWrapper.decrypt(encryptedResponseStr);
                        Log.d(TAG, "onResponse decrypted response string is: "+decryptedResponseStr);
                        RequestResponse requestResponse = gson.fromJson(decryptedResponseStr, RequestResponse.class);
                        adminResponseLiveData.postValue(requestResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                                ,new ArrayList<>());
                        adminResponseLiveData.postValue(errorResponse);
                    }
                }
                else{
                    Log.d(TAG,"onResponse is not successful");
                    try {
                        String errorResponseDecryptStr = EncryptionWrapper.decrypt(response.errorBody().string());
                        ErrorResponse errorResponse = gson.fromJson(errorResponseDecryptStr,ErrorResponse.class);
                        adminResponseLiveData.postValue(errorResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ErrorResponse errorResponse1 = new ErrorResponse("Something went wrong with the system. Please try again later"
                                ,new ArrayList<>());
                        adminResponseLiveData.postValue(errorResponse1);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG,"onFailure" + t.getMessage() +t.getLocalizedMessage()+t.getCause());
                ErrorResponse errorResponse1 = new ErrorResponse("Something went wrong with the system. Please try again later"
                        ,new ArrayList<>());
                adminResponseLiveData.postValue(errorResponse1);
            }
        });
    }

    public MutableLiveData<IResponse> getAdminResponseLiveData() {
        return adminResponseLiveData;
    }
}
