package com.pushkar.packagemanagementadmin.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.pushkar.packagemanagementadmin.model.service.EncryptRequest;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.model.service.UpdatePackageStatusResponse;
import com.pushkar.packagemanagementadmin.service.EncryptionWrapper;
import com.pushkar.packagemanagementadmin.service.PackageService;
import com.pushkar.packagemanagementadmin.utils.Constants;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScanRepository extends BaseRepository{
    private final int PARTIAL_SUCCESS_STATUS_CODE = 207;
    private final String TAG = ScanRepository.class.getSimpleName();
    private static ScanRepository scanRepository;
    private MutableLiveData<IResponse> responseOfPossessionScanLiveData;
    private Gson gson = new Gson();
    private PackageService packageService;
    public static ScanRepository getScanRepository(){
        if(scanRepository == null){
            synchronized (ScanRepository.class){
                if(scanRepository == null){
                    scanRepository = new ScanRepository();
                }
            }
        }
        return scanRepository;
    }

    public ScanRepository(){
        responseOfPossessionScanLiveData = new MutableLiveData<>();
        packageService = getPackageService();
    }

    public MutableLiveData<IResponse> getResponseOfPossessionScanLiveData() {
        return responseOfPossessionScanLiveData;
    }

    public void updateStatusOfPackages(String scanType, EncryptRequest request){
        switch (scanType){
            case Constants.DELIVERY_SCAN_TYPE_INTENT:
                packageService.deliverPackages(request).enqueue(new CallBackHandler());
                break;
            case Constants.POSSESSION_SCAN_TYPE_INTENT:
                packageService.takePossessionOfPackages(request).enqueue(new CallBackHandler());
                break;
        }
    }



    private void onFailureHandler(Throwable t){
        Log.d(TAG, "onFailure with throwable: "+t.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                ,new ArrayList<>());
        responseOfPossessionScanLiveData.postValue(errorResponse);
    }

    private void onResponseHandler(Response<ResponseBody> response){
        Log.d(TAG, "onResponse");
        if(response.isSuccessful()){
            if(response.code() == PARTIAL_SUCCESS_STATUS_CODE){
                Log.d(TAG, "onResponse partially successful");
                try {
                    String encryptedResponseStr = response.body().string();
                    Log.d(TAG, "onResponse partial success encrypted response string: "+encryptedResponseStr);
                    String decryptedStr = EncryptionWrapper.decrypt(encryptedResponseStr);
                    Log.d(TAG, "onResponse partial success decrypted raw response string: "+decryptedStr);
                    UpdatePackageStatusResponse updatePackageStatusResponse = gson.fromJson(
                            decryptedStr,
                            UpdatePackageStatusResponse.class
                    );
                    responseOfPossessionScanLiveData.postValue(updatePackageStatusResponse);
                } catch (Exception e) {
                    Log.d(TAG, "onResponse exception");
                    e.printStackTrace();
                    ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                            ,new ArrayList<>());
                    responseOfPossessionScanLiveData.postValue(errorResponse);
                }
            }else{
                Log.d(TAG, "onResponse fully successful");
                responseOfPossessionScanLiveData.postValue(new UpdatePackageStatusResponse(
                        new ArrayList<>(),
                        "Successful"
                ));
            }
        }else{
            Log.d(TAG, "onResponse not successful");
            try {
                String encryptedResponseStr = response.errorBody().string();
                Log.d(TAG, "onResponse not successful error response raw encrypted string: "+encryptedResponseStr);
                String decryptedStr = EncryptionWrapper.decrypt(encryptedResponseStr);
                ErrorResponse errorResponse = gson.fromJson(decryptedStr,ErrorResponse.class);
                responseOfPossessionScanLiveData.postValue(errorResponse);
            } catch (Exception e) {
                Log.d(TAG, "onResponse - is not successful Exception");
                e.printStackTrace();
                ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                        ,new ArrayList<>());
                responseOfPossessionScanLiveData.postValue(errorResponse);
            }
        }
    }

    private class CallBackHandler implements Callback<ResponseBody>{

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            onResponseHandler(response);
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            onFailureHandler(t);
        }
    }
}
