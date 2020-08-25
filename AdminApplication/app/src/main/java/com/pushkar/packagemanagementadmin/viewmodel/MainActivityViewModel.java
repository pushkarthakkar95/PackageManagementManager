package com.pushkar.packagemanagementadmin.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.pushkar.packagemanagementadmin.model.service.EncryptRequest;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.model.service.LogoutRequest;
import com.pushkar.packagemanagementadmin.repository.MainActivityRepository;
import com.pushkar.packagemanagementadmin.service.EncryptionWrapper;

import java.util.ArrayList;

public class MainActivityViewModel extends ViewModel {
    private final String TAG = MainActivityViewModel.class.getSimpleName();
    private Gson gson = new Gson();
    private MainActivityRepository repository = MainActivityRepository.getMainActivityRepository();
    private MutableLiveData<IResponse> responseMutableLiveData;

    public MainActivityViewModel(){
        responseMutableLiveData = repository.getResponseMutableLiveData();
    }

    public void attemptLogout(LogoutRequest logoutRequest){
        String jsonStr = gson.toJson(logoutRequest);
        Log.d(TAG, "attemptLogout: raw string is: "+jsonStr);
        try {
            String encryptedString = EncryptionWrapper.encrypt(jsonStr);
            Log.d(TAG, "attemptLogout: encrypted string is: "+encryptedString);
            EncryptRequest encryptRequest = new EncryptRequest(encryptedString);
            repository.attemptLogout(encryptRequest);
        } catch (Exception e) {
            Log.d(TAG, "attemptLogout viewmodel Exception "+e.getLocalizedMessage());
            e.printStackTrace();
            unexpectedErrorHandling();
        }
    }

    public MutableLiveData<IResponse> getResponseMutableLiveData() {
        return responseMutableLiveData;
    }

    private void unexpectedErrorHandling(){
        ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                ,new ArrayList<>());
        responseMutableLiveData.postValue(errorResponse);
    }
}
