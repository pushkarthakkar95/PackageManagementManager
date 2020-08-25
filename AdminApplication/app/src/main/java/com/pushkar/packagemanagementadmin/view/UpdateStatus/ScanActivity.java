package com.pushkar.packagemanagementadmin.view.UpdateStatus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.pushkar.packagemanagementadmin.R;
import com.pushkar.packagemanagementadmin.barcodeutils.BarcodeCaptureActivity;
import com.pushkar.packagemanagementadmin.databinding.ActivityPossessionScanBinding;
import com.pushkar.packagemanagementadmin.model.FailedPackageStatusDHO;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.model.service.UpdatePackageStatusDHO;
import com.pushkar.packagemanagementadmin.model.service.UpdatePackageStatusResponse;
import com.pushkar.packagemanagementadmin.utils.Constants;
import com.pushkar.packagemanagementadmin.view.BaseActivity;
import com.pushkar.packagemanagementadmin.viewmodel.ScanViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScanActivity extends BaseActivity {
    private final String TAG = ScanActivity.class.getSimpleName();
    private ActivityPossessionScanBinding binding;
    private ScanViewModel viewModel;
    private ProgressDialog progressDialog;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private boolean isProcessDialogShown = false;
    private String storeNum = "";
    private String scanType;
    private AlertDialog partialDialog;
    private AlertDialog dialog;
    private boolean isPartialSuccessAlertShown = false;
    private boolean isSuccessAlertShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_possession_scan);
        storeNum = getIntent().getStringExtra(Constants.STORE_NUM_INTENT_KEY);
        scanType = getIntent().getStringExtra(Constants.SCAN_TYPE_INTENT);
        setLayoutAccordingToScanType();
        ScanListAdapter adapter = new ScanListAdapter(this);
        binding.barcodeRecyclerView.setAdapter(adapter);
        binding.barcodeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel = ViewModelProviders.of(this).get(ScanViewModel.class);
        viewModel.setStoreNum(storeNum);

        if(savedInstanceState!=null){
            if(savedInstanceState.getBoolean(Constants.DIALOG_SHOWN_KEY)){
                progressDialog.show();
                isProcessDialogShown = true;
            }
            if(savedInstanceState.getBoolean(Constants.IS_PARTIAL_DIALOG_SHOWN_KEY)){
                showPartialSuccessDialog(viewModel.getParsedServerFailedListResponse());

            }else if(savedInstanceState.getBoolean(Constants.IS_SUCCESS_DIALOG_SHOWN_KEY)){
                showSuccessAlertDialog();
            }
        }
        binding.possessionScanNoBarcodeTV.setVisibility(View.VISIBLE);
        binding.countBarcodesTV.setText("0 packages scanned");
        viewModel.getListOfScannedBarcodeMutableLiveData().observe(this, new Observer<List<UpdatePackageStatusDHO>>() {
            @Override
            public void onChanged(List<UpdatePackageStatusDHO> packageOrders) {
                adapter.setData(packageOrders);
                binding.countBarcodesTV.setText(packageOrders.size()+" packages scanned");
                if(packageOrders.isEmpty()){
                    binding.possessionScanNoBarcodeTV.setVisibility(View.VISIBLE);
                }else{
                    binding.possessionScanNoBarcodeTV.setVisibility(View.GONE);
                }
            }
        });
        viewModel.getResponseMutableLiveData().observe(this, new Observer<IResponse>() {
            @Override
            public void onChanged(IResponse iResponse) {
                if(iResponse != null){
                    viewModel.setLastNonNullResponse(iResponse);
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                        isProcessDialogShown = false;
                    }
                    if(iResponse instanceof UpdatePackageStatusResponse){
                        UpdatePackageStatusResponse response = (UpdatePackageStatusResponse) iResponse;
                        if(response.getDetails().isEmpty()){
                            showSuccessAlertDialog();
                        }else{
                            showPartialSuccessDialog(viewModel.getParsedServerFailedListResponse());
                        }
                    }else if(iResponse instanceof ErrorResponse){
                        showErrorToast(ScanActivity.this,(ErrorResponse) iResponse);
                    }else{
                        showGeneralErrorToast(ScanActivity.this);
                    }
                }
                viewModel.getResponseMutableLiveData().postValue(null);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewModel.removeBarcodeFromList(viewHolder.getAdapterPosition());
            }
        });

        helper.attachToRecyclerView(binding.barcodeRecyclerView);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.DIALOG_SHOWN_KEY,isProcessDialogShown);
        outState.putBoolean(Constants.IS_PARTIAL_DIALOG_SHOWN_KEY,isPartialSuccessAlertShown);
        outState.putBoolean(Constants.IS_SUCCESS_DIALOG_SHOWN_KEY,isSuccessAlertShown);
    }

    public void addScanClicked(View view) {
        Intent intent = new Intent(this, BarcodeCaptureActivity.class);
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
        intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
        startActivityForResult(intent, RC_BARCODE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    ArrayList<String> barcodeList = data.getStringArrayListExtra(Constants.CAPTURED_BARCODES_LIST_INTENT_KEY);
                    if(barcodeList != null && !barcodeList.isEmpty()){
                        viewModel.addBarcodeToList(barcodeList);
                        Log.d(TAG, "Barcodes detected: " + barcodeList.size());
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"No barcode captured",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "No barcode captured");
                }
            } else {
                Toast.makeText(getApplicationContext(),"Barcode reader failed, status code not success",Toast.LENGTH_SHORT).show();
                Log.d(TAG, "No barcode captured, intent status code is not SUCCESS");
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog != null){
            progressDialog.dismiss();
            isProcessDialogShown = false;
        }
        if(dialog != null && isSuccessAlertShown){
            dialog.dismiss();
            isSuccessAlertShown = false;
        }
        if(partialDialog!= null && isPartialSuccessAlertShown){
            partialDialog.dismiss();
            isPartialSuccessAlertShown = false;
        }
    }

    private void setLayoutAccordingToScanType(){
        if(scanType.equals(Constants.DELIVERY_SCAN_TYPE_INTENT)){
            binding.possessionScanTV.setText("DELIVERY SCAN");
            binding.possessionScanTakePossessionBtn.setText("Deliver");
        }
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(ScanActivity.this);
        progressDialog.setMessage("please wait....");
        progressDialog.setTitle("Processing Request");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        isProcessDialogShown = true;
    }

    private void showSuccessAlertDialog(){
        dialog = new AlertDialog.Builder(ScanActivity.this)
                .setTitle("Success")
                .setMessage("All packages scanned were processed successfully")
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isSuccessAlertShown = false;
                        onBackPressed();
                        finish();
                    }
                })
                .setIcon(R.drawable.ic_done_black_24dp)
                .show();
                isSuccessAlertShown = true;
    }

    private void showPartialSuccessDialog(List<FailedPackageStatusDHO> listOfPackage){
        List<String> barcodesList = listOfPackage.stream()
                .map(FailedPackageStatusDHO::getTrackingNumber)
                .collect(Collectors.toList());
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanActivity.this);
        builder.setTitle("Oops!");
        builder.setMessage("Packages listed below were not updated successfully: ");
        View customLayout = getLayoutInflater().inflate(R.layout.update_package_status_partial_alert_layout,null);
        RecyclerView recyclerView = customLayout.findViewById(R.id.failedTrackingRecyclerView);
        AlertDialogRecyclerViewAdapter alertDialogRecyclerViewAdapter = new AlertDialogRecyclerViewAdapter(
                this,
                        listOfPackage
                );
        recyclerView.setAdapter(alertDialogRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        builder.setView(customLayout);
        builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewModel.updateBarcodeList(barcodesList);
                isPartialSuccessAlertShown = false;
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isPartialSuccessAlertShown = false;
                onBackPressed();
                finish();
            }
        });
        builder.setCancelable(false);
        partialDialog = builder.show();
        isPartialSuccessAlertShown = true;

    }

    public void updateClicked(View view) {
        showProgressDialog();
        viewModel.attemptUpdateStatusOfBarcodes(scanType);
    }
}
