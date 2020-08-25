package com.pushkar.packagemanagementadmin.viewmodel;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.gson.Gson;
import com.pushkar.packagemanagementadmin.model.service.EncryptRequest;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.model.service.ListOrdersRequest;
import com.pushkar.packagemanagementadmin.repository.ListOrdersRepository;
import com.pushkar.packagemanagementadmin.service.EncryptionWrapper;


public class ListOrdersViewModel extends ViewModel {
    private MutableLiveData<IResponse> listOfPackagesLiveData;
    private ListOrdersRepository listOrdersRepository = ListOrdersRepository.getListOrdersRepository();
    private final String TAG = ListOrdersViewModel.class.getSimpleName();
    Gson gson = new Gson();
    public ListOrdersViewModel() {
        listOfPackagesLiveData = listOrdersRepository.getListMutableLiveData();
    }

    public void getListOfOrders(ListOrdersRequest listOrdersRequest){
        String jsonStr = gson.toJson(listOrdersRequest);
        Log.d(TAG, "getListOfPackagesForStore request with raw string: "+jsonStr);
        try {
            String encryptedStr = EncryptionWrapper.encrypt(jsonStr);
            Log.d(TAG, "getListOfPackagesForStore request with encrypted string: "+encryptedStr);
            EncryptRequest request = new EncryptRequest(encryptedStr);
            listOrdersRepository.getListOfPackagesForStore(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LiveData<IResponse> getListOfPackagesLiveData() {
        return listOfPackagesLiveData;
    }
}
