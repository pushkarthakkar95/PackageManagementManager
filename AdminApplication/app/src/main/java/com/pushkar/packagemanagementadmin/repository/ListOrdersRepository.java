package com.pushkar.packagemanagementadmin.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pushkar.packagemanagementadmin.model.PackageOrder;
import com.pushkar.packagemanagementadmin.model.service.EncryptRequest;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.model.service.ListOrdersRequest;
import com.pushkar.packagemanagementadmin.model.service.ListOrdersResponse;
import com.pushkar.packagemanagementadmin.service.EncryptionWrapper;
import com.pushkar.packagemanagementadmin.service.PackageService;
import com.pushkar.packagemanagementadmin.utils.Constants;
import com.pushkar.packagemanagementadmin.view.ListOrders.ListPackageOrdersAdapter;
import com.pushkar.packagemanagementadmin.viewmodel.ListOrdersViewModel;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListOrdersRepository extends BaseRepository {
    private PackageService packageService;
    private static ListOrdersRepository listOrdersRepository;
    private MutableLiveData<IResponse> listMutableLiveData;
    private Gson gson = new Gson();
    private final String TAG = ListOrdersRepository.class.getSimpleName();

    public static ListOrdersRepository getListOrdersRepository(){
        if(listOrdersRepository == null){
            synchronized (ListOrdersRepository.class){
                if(listOrdersRepository == null){
                    listOrdersRepository = new ListOrdersRepository();
                }
            }
        }
        return listOrdersRepository;
    }

    private ListOrdersRepository(){
        listMutableLiveData = new MutableLiveData<>();
        packageService = getPackageService();
    }

    public void getListOfPackagesForStore(EncryptRequest request){
        packageService.getAllPackagesForStore(request).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse is successful");
                    String encryptedResponseStr = null;
                    try {
                        encryptedResponseStr = response.body().string();
                        Log.d(TAG, "onResponse raw response string is: "+encryptedResponseStr);
                        String decryptedResponseStr = EncryptionWrapper.decrypt(encryptedResponseStr);
                        Log.d(TAG, "onResponse - Successful with decrypted string: "+decryptedResponseStr);
                        ListOrdersResponse listOrdersResponse = gson.fromJson(decryptedResponseStr,ListOrdersResponse.class);
                        listMutableLiveData.postValue(listOrdersResponse);
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse - Exception ");
                        e.printStackTrace();
                        ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                                ,new ArrayList<>());
                        listMutableLiveData.postValue(errorResponse);
                    }


                }else{
                    Log.d(TAG,"onResponse is not successful");
                    try {
                        String errorResponseEncryptedStr = response.errorBody().string();
                        Log.d(TAG, "onResponse - is not successful with encrypted error response string: " +
                                errorResponseEncryptedStr);
                        String decryptedErrorResponseStr = EncryptionWrapper.decrypt(errorResponseEncryptedStr);
                        ErrorResponse errorResponse = gson.fromJson(decryptedErrorResponseStr,ErrorResponse.class);
                        listMutableLiveData.postValue(errorResponse);
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse - is not successful Exception");
                        e.printStackTrace();
                        ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                                ,new ArrayList<>());
                        listMutableLiveData.postValue(errorResponse);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure with throwable: "+t.getLocalizedMessage());
                ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                        ,new ArrayList<>());
                listMutableLiveData.postValue(errorResponse);
            }
        });
    }

    public MutableLiveData<IResponse> getListMutableLiveData() {
        return listMutableLiveData;
    }
}
