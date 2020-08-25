package com.pushkar.packagemanagementadmin.view;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.pushkar.packagemanagementadmin.R;
import com.pushkar.packagemanagementadmin.databinding.ActivityAdminLoginBinding;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.model.service.LoginRequestData;
import com.pushkar.packagemanagementadmin.model.service.LoginSuccessResponse;
import com.pushkar.packagemanagementadmin.utils.Constants;
import com.pushkar.packagemanagementadmin.utils.ParserHelper;
import com.pushkar.packagemanagementadmin.utils.Validate;
import com.pushkar.packagemanagementadmin.view.Registration.RegistrationActivity;
import com.pushkar.packagemanagementadmin.viewmodel.LoginViewModel;

import java.util.Date;

public class AdminLoginActivity extends BaseActivity {
    private final String TAG = AdminLoginActivity.class.getSimpleName();
    private SharedPreferences mPreferences;
    private ActivityAdminLoginBinding binding;
    private LoginViewModel viewModel;
    private String deviceToken = "";
    private ProgressDialog progressDialog;

    private boolean isDialogShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_admin_login);
        updateDeviceToken();
        mPreferences = getSharedPreferences(Constants.SHARED_PREF_STRING, MODE_PRIVATE);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        if(savedInstanceState!=null){
            if(savedInstanceState.getBoolean(Constants.DIALOG_SHOWN_KEY)){
                showProgressDialog();
            }
        }
        viewModel.getLoginResponseLiveData().observe(this, new Observer<IResponse>() {
            @Override
            public void onChanged(IResponse iResponse) {
                if(iResponse != null){
                    if(progressDialog != null){
                        progressDialog.dismiss();
                        isDialogShown = false;
                    }
                    if(iResponse instanceof LoginSuccessResponse){
                        LoginSuccessResponse response = ((LoginSuccessResponse) iResponse);
                        saveUserInfo(response);
                        goToMainScreen();
                    }else if(iResponse instanceof ErrorResponse){
                        showErrorToast(AdminLoginActivity.this, (ErrorResponse) iResponse);
                    }else{
                        showGeneralErrorToast(AdminLoginActivity.this);
                    }
                }
                viewModel.getLoginResponseLiveData().postValue(null);
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.DIALOG_SHOWN_KEY,isDialogShown);
    }

    private void updateDeviceToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(AdminLoginActivity.this
                , new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String token = instanceIdResult.getToken();
                        deviceToken = token;
                        Log.d(TAG, "onSuccess: "+token);
                    }
                });
    }

    public void loginBtnClicked(View view) {
        LoginRequestData loginData = new LoginRequestData(binding.adminLoginEmpIdET.getText().toString()
                ,binding.adminLoginPasswordET.getText().toString(), deviceToken);
        if(!Validate.isLoginRequestDataValid(loginData)){
            Toast.makeText(getApplicationContext(),R.string.incorrect_registration_input_toast_message
                    ,Toast.LENGTH_SHORT).show();
        }else{
            showProgressDialog();
            viewModel.attemptAdminLogin(loginData);
        }
    }



    private void goToMainScreen(){
        Intent intent =  new Intent(AdminLoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(AdminLoginActivity.this);
        progressDialog.setMessage("please wait....");
        progressDialog.setTitle("Login in progress");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        isDialogShown = true;
    }

    private void saveUserInfo(LoginSuccessResponse loginSuccessResponse){
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putBoolean(Constants.SHARED_PREF_IS_USER_LOGGED_IN,true);
        preferencesEditor.putString(Constants.SHARED_PREF_DEVICE_TOKEN,deviceToken);
        preferencesEditor.putString(Constants.SHARED_PREF_EMP_ID,loginSuccessResponse.getEmployeeId());
        preferencesEditor.putString(Constants.SHARED_PREF_STORE_NUM, loginSuccessResponse.getStoreNumber());
        preferencesEditor.putString(Constants.SHARED_PREF_LAST_LOG_IN_TIME, ParserHelper.getStringFromDate(new Date()));
        preferencesEditor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog != null){
            progressDialog.dismiss();
            isDialogShown = false;
        }
    }

    public void signUpClicked(View view) {
        Intent intent = new Intent(AdminLoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    private boolean isTokenSame(String serverToken){
        return deviceToken.equals(serverToken);
    }
}

