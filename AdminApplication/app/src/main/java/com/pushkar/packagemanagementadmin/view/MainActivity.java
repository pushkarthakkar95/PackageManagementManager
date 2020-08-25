package com.pushkar.packagemanagementadmin.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pushkar.packagemanagementadmin.R;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.model.service.LogoutRequest;
import com.pushkar.packagemanagementadmin.model.service.LogoutSuccessResponse;
import com.pushkar.packagemanagementadmin.utils.Constants;
import com.pushkar.packagemanagementadmin.view.ListOrders.ListOrdersActivity;
import com.pushkar.packagemanagementadmin.view.UpdateStatus.ScanActivity;
import com.pushkar.packagemanagementadmin.viewmodel.MainActivityViewModel;

public class MainActivity extends BaseActivity {

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = MainActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private String storeNum;
    private String deviceToken;
    private String employeeId;
    private MainActivityViewModel viewModel;
    private ProgressDialog progressDialog;
    private boolean isDialogShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_STRING, MODE_PRIVATE);
        storeNum = sharedPreferences.getString(Constants.SHARED_PREF_STORE_NUM,"");
        deviceToken = sharedPreferences.getString(Constants.SHARED_PREF_DEVICE_TOKEN,"");
        employeeId = sharedPreferences.getString(Constants.SHARED_PREF_EMP_ID,"");
        if(savedInstanceState != null){
            if(savedInstanceState.getBoolean(Constants.DIALOG_SHOWN_KEY)){
                showProgressDialog();
            }
        }
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewModel.getResponseMutableLiveData().observe(this, new Observer<IResponse>() {
            @Override
            public void onChanged(IResponse iResponse) {
                if(iResponse != null){
                    if(progressDialog != null){
                        progressDialog.dismiss();
                        isDialogShown = false;
                    }
                    if(iResponse instanceof LogoutSuccessResponse){
                        processLogout();
                    }else if(iResponse instanceof ErrorResponse){
                        showErrorToast(MainActivity.this, (ErrorResponse) iResponse);
                    }else{
                        showGeneralErrorToast(MainActivity.this);
                    }
                }
                viewModel.getResponseMutableLiveData().postValue(null);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.DIALOG_SHOWN_KEY,isDialogShown);
    }


    public void logoutClicked(View view) {
        showProgressDialog();
        viewModel.attemptLogout(new LogoutRequest(employeeId,deviceToken));
    }

    private void processLogout(){
        resetPreferences();
        Intent intent = new Intent(MainActivity.this,AdminLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this,"Please logout or close the application",Toast.LENGTH_LONG).show();
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("please wait....");
        progressDialog.setTitle("Logout in progress");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        isDialogShown = true;
    }

    private void resetPreferences(){
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putBoolean(Constants.SHARED_PREF_IS_USER_LOGGED_IN,false);
        preferencesEditor.putString(Constants.SHARED_PREF_DEVICE_TOKEN,"");
        preferencesEditor.putString(Constants.SHARED_PREF_EMP_ID,"");
        preferencesEditor.putString(Constants.SHARED_PREF_STORE_NUM, "");
        preferencesEditor.apply();
    }

    public void showOrdersClicked(View view) {
        Intent intent = new Intent(MainActivity.this, ListOrdersActivity.class);
        startActivity(intent);
    }

    public void possessionScanClicked(View view) {
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        intent.putExtra(Constants.STORE_NUM_INTENT_KEY,storeNum);
        intent.putExtra(Constants.SCAN_TYPE_INTENT,Constants.POSSESSION_SCAN_TYPE_INTENT);
        startActivity(intent);
    }

    public void deliveryScanClicked(View view) {
        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
        intent.putExtra(Constants.STORE_NUM_INTENT_KEY,storeNum);
        intent.putExtra(Constants.SCAN_TYPE_INTENT,Constants.DELIVERY_SCAN_TYPE_INTENT);
        startActivity(intent);
    }
}
