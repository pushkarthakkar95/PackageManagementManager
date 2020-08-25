package com.pushkar.packagemanagementadmin.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.pushkar.packagemanagementadmin.model.service.EncryptRequest;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.model.service.LoginSuccessResponse;
import com.pushkar.packagemanagementadmin.service.EncryptionWrapper;
import com.pushkar.packagemanagementadmin.service.LoginService;
import com.pushkar.packagemanagementadmin.utils.Constants;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginRepository extends BaseRepository{
    private static LoginRepository loginRepository;
    private LoginService loginService;
    private MutableLiveData<IResponse> loginResponseLiveData;
    private static final String TAG = LoginRepository.class.getSimpleName();
    private Gson gson = new Gson();

    public static LoginRepository getLoginRepository(){
        if(loginRepository == null){
            synchronized (LoginRepository.class){
                if (loginRepository == null){
                    loginRepository = new LoginRepository();
                }
            }
        }

        return loginRepository;
    }

    private LoginRepository(){
        loginResponseLiveData = new MutableLiveData<>();
        loginService = getLoginService();
    }

    public void attemptLogin(EncryptRequest encryptLoginRequest){
        Log.d(TAG, "attemptLogin - with encrypted login string as : "+encryptLoginRequest.getEncryptString());
        loginService.login(encryptLoginRequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse");
                if(response.isSuccessful()){
                    try {
                        String encryptedResponseStr = response.body().string();
                        Log.d(TAG, "onResponse - Successful with raw encrypted string: "+encryptedResponseStr);
                        String decryptedResponseStr = EncryptionWrapper.decrypt(encryptedResponseStr);
                        Log.d(TAG, "onResponse - Successful with decrypted string: "+decryptedResponseStr);
                        LoginSuccessResponse loginSuccessResponse = gson.fromJson(decryptedResponseStr,
                                LoginSuccessResponse.class);
                        loginResponseLiveData.postValue(loginSuccessResponse);
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse - Exception ");
                        e.printStackTrace();
                        ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                                ,new ArrayList<>());
                        loginResponseLiveData.postValue(errorResponse);
                    }
                }else{
                    Log.d(TAG,"onResponse is not successful");
                    try {
                        String errorResponseEncryptedStr = response.errorBody().string();
                        Log.d(TAG, "onResponse - is not successful with encrypted error response string: " +
                                errorResponseEncryptedStr);
                        String decryptedErrorResponseStr = EncryptionWrapper.decrypt(errorResponseEncryptedStr);
                        ErrorResponse errorResponse = gson.fromJson(decryptedErrorResponseStr,ErrorResponse.class);
                        loginResponseLiveData.postValue(errorResponse);
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse - is not successful Exception");
                        e.printStackTrace();
                        ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                                ,new ArrayList<>());
                        loginResponseLiveData.postValue(errorResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure with throwable: "+t.getLocalizedMessage());
                ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                        ,new ArrayList<>());
                loginResponseLiveData.postValue(errorResponse);
            }
        });
    }



    public MutableLiveData<IResponse> getLoginResponseLiveData() {
        return loginResponseLiveData;
    }
}
