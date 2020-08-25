package com.pushkar.packagemanagementadmin.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.pushkar.packagemanagementadmin.model.service.EncryptRequest;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.model.service.LoginRequestData;
import com.pushkar.packagemanagementadmin.repository.LoginRepository;
import com.pushkar.packagemanagementadmin.service.EncryptionWrapper;

import java.util.ArrayList;

public class LoginViewModel extends ViewModel {
    private static final String TAG = LoginViewModel.class.getSimpleName();
    private LoginRepository loginRepository = LoginRepository.getLoginRepository();
    private MutableLiveData<IResponse> loginResponseLiveData;
    private Gson gson = new Gson();

    public LoginViewModel() {
        loginResponseLiveData = loginRepository.getLoginResponseLiveData();
    }

    public void attemptAdminLogin(LoginRequestData loginData){
        Log.d(TAG, "attemptAdminLogin - with login id: "+loginData.getEmployeeId()
                +" and password: "+loginData.getPassword());
        String jsonStr = gson.toJson(loginData);
        Log.d(TAG, "attemptAdminLogin - json string from gson is: "+jsonStr);
        try {
            String encryptRequestStr = EncryptionWrapper.encrypt(jsonStr);
            Log.d(TAG, "attemptAdminLogin - json string from gson is: "+jsonStr);
            EncryptRequest request = new EncryptRequest(encryptRequestStr);
            loginRepository.attemptLogin(request);
        } catch (Exception e) {
            Log.d(TAG, "attemptAdminLogin - Exception");
            e.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                    ,new ArrayList<>());
            loginResponseLiveData.postValue(errorResponse);
        }
    }

    public MutableLiveData<IResponse> getLoginResponseLiveData() {
        return loginResponseLiveData;
    }


}
