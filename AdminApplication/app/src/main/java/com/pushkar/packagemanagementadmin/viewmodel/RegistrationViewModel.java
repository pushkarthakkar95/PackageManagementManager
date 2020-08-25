package com.pushkar.packagemanagementadmin.viewmodel;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.pushkar.packagemanagementadmin.model.Register;
import com.pushkar.packagemanagementadmin.model.service.RegistrationRequest;
import com.pushkar.packagemanagementadmin.model.service.EncryptRequest;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.repository.RegistrationRepository;
import com.pushkar.packagemanagementadmin.service.EncryptionWrapper;

import java.util.ArrayList;

public class RegistrationViewModel extends ViewModel {
    private RegistrationRepository registrationRepository = RegistrationRepository.getRegistrationRepository();
    private MutableLiveData<IResponse> adminResponseLiveData;
    private static final String TAG = RegistrationViewModel.class.getSimpleName();
    Gson gson = new Gson();

    public RegistrationViewModel() {
        adminResponseLiveData = registrationRepository.getAdminResponseLiveData();
    }


    public void attemptAdminRegistration(Register user) {
        try {
            RegistrationRequest registrationRequest = new RegistrationRequest(user.getName(),user.getPassword());
            String encryptedStr = EncryptionWrapper.encrypt(
                    gson.toJson(registrationRequest)
            );
            Log.d(TAG, "attemptAdminRegistration - Encrypted String is: "+encryptedStr);
            EncryptRequest request = new EncryptRequest(encryptedStr);
            registrationRepository.registerUser(user.getStoreNum(),request);
        } catch (Exception e) {
            Log.d(TAG, "attemptAdminRegistration - Exception");
            e.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                    ,new ArrayList<>());
            adminResponseLiveData.postValue(errorResponse);
        }
    }

    public MutableLiveData<IResponse> getAdminResponseLiveData() {
        return adminResponseLiveData;
    }
}
