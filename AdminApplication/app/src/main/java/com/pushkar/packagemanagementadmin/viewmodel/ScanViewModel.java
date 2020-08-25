package com.pushkar.packagemanagementadmin.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.pushkar.packagemanagementadmin.model.FailedPackageStatusDHO;
import com.pushkar.packagemanagementadmin.model.service.EncryptRequest;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.model.service.UpdatePackageStatusDHO;
import com.pushkar.packagemanagementadmin.model.service.UpdatePackageStatusRequest;
import com.pushkar.packagemanagementadmin.model.service.UpdatePackageStatusResponse;
import com.pushkar.packagemanagementadmin.repository.ScanRepository;
import com.pushkar.packagemanagementadmin.service.EncryptionWrapper;
import com.pushkar.packagemanagementadmin.utils.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ScanViewModel extends ViewModel {
    private MutableLiveData<IResponse> responseMutableLiveData;
    private MutableLiveData<List<UpdatePackageStatusDHO>> listOfScannedBarcodeMutableLiveData;
    private Gson gson = new Gson();
    private List<String> listOfBarcode;
    private List<UpdatePackageStatusDHO> listOfPackageOrders;
    private final String TAG = ScanViewModel.class.getSimpleName();
    private ScanRepository scanRepository = ScanRepository.getScanRepository();
    private String storeNum;
    private IResponse lastNonNullResponse;
    public ScanViewModel() {
        responseMutableLiveData = scanRepository.getResponseOfPossessionScanLiveData();
        listOfBarcode = new ArrayList<>();
        listOfPackageOrders = new ArrayList<>();
        listOfScannedBarcodeMutableLiveData = new MutableLiveData<>();
    }

    public void addBarcodeToList(ArrayList<String> barcodeList){
        boolean shouldPost = false;
        for (String barcode: barcodeList) {
            if(!listOfBarcode.contains(barcode)){
                listOfBarcode.add(barcode);
                listOfPackageOrders.add(new UpdatePackageStatusDHO(barcode,storeNum));
                shouldPost = true;
            }
        }
        if(shouldPost)
            listOfScannedBarcodeMutableLiveData.postValue(listOfPackageOrders);
    }

    public void updateBarcodeList(List<String> barcodes){
        listOfBarcode = barcodes;
        listOfPackageOrders = new ArrayList<>();
        for (String barcodeStr: listOfBarcode) {
            listOfPackageOrders.add(new UpdatePackageStatusDHO(barcodeStr,storeNum));
        }
        listOfScannedBarcodeMutableLiveData.postValue(listOfPackageOrders);
    }

    public void removeBarcodeFromList(int position){
        listOfBarcode.remove(position);
        listOfPackageOrders.remove(position);
        listOfScannedBarcodeMutableLiveData.postValue(listOfPackageOrders);
    }

    public MutableLiveData<IResponse> getResponseMutableLiveData() {
        return responseMutableLiveData;
    }

    public MutableLiveData<List<UpdatePackageStatusDHO>> getListOfScannedBarcodeMutableLiveData() {
        return listOfScannedBarcodeMutableLiveData;
    }

    public void attemptUpdateStatusOfBarcodes(String scanType){
        UpdatePackageStatusRequest updatePackageStatusRequest = new UpdatePackageStatusRequest(getUpdatedListOfPackageOrders(
                listOfPackageOrders, scanType
        ));
        Log.d(TAG, "attemptUpdateStatusOfBarcodes updatePackageStatusRequest was made with listOfPackageOrders");
        String jsonStr = gson.toJson(updatePackageStatusRequest);
        Log.d(TAG, "attemptUpdateStatusOfBarcodes with decrypt raw request string: "+jsonStr);
        try {
            String encryptedString = EncryptionWrapper.encrypt(jsonStr);
            Log.d(TAG, "attemptUpdateStatusOfBarcodes with encrypt request string: "+encryptedString);
            EncryptRequest request = new EncryptRequest(encryptedString);
            scanRepository.updateStatusOfPackages(scanType,request);
        } catch (Exception e) {
            Log.d(TAG, "attemptUpdateStatusOfBarcodes Exception");
            e.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse("Something went wrong with the system. Please try again later"
                    ,new ArrayList<>());
            responseMutableLiveData.postValue(errorResponse);
        }
    }

    private List<UpdatePackageStatusDHO> getUpdatedListOfPackageOrders(List<UpdatePackageStatusDHO> originalList, String scanType){
        if (Constants.DELIVERY_SCAN_TYPE_INTENT.equals(scanType)) {
            for (UpdatePackageStatusDHO element : originalList) {
                element.setDeliveryDate(getDeliveryFormattedDate());
            }
            return originalList;
        }
        return originalList;
    }

    private String getDeliveryFormattedDate(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public void setStoreNum(String storeNum) {
        this.storeNum = storeNum;
    }

    public List<FailedPackageStatusDHO> getParsedServerFailedListResponse(){
        UpdatePackageStatusResponse response  = (UpdatePackageStatusResponse)lastNonNullResponse;
        List<FailedPackageStatusDHO> result = new ArrayList<>();
        if(response!=null){
            List<String> detailsList = response.getDetails();
            for (String detail: detailsList) {
                String[] arrayString = detail.split(":");
                result.add(new FailedPackageStatusDHO(arrayString[0],arrayString[1]));
            }
        }
        return result;
    }

    public void setLastNonNullResponse(IResponse lastNonNullResponse) {
        this.lastNonNullResponse = lastNonNullResponse;
    }
}
