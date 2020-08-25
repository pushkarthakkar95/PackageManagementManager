package com.pushkar.packagemanagementadmin.view.Registration;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pushkar.packagemanagementadmin.R;
import com.pushkar.packagemanagementadmin.databinding.ActivityRegistrationBinding;
import com.pushkar.packagemanagementadmin.model.Register;
import com.pushkar.packagemanagementadmin.model.service.RequestResponse;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.utils.Constants;
import com.pushkar.packagemanagementadmin.utils.Validate;
import com.pushkar.packagemanagementadmin.view.BaseActivity;
import com.pushkar.packagemanagementadmin.viewmodel.RegistrationViewModel;


public class RegistrationActivity extends BaseActivity {
    private final String TAG = RegistrationActivity.class.getSimpleName();
    private RegistrationViewModel registrationViewModel;
    private ActivityRegistrationBinding binding;
    private ProgressDialog progressDialog;
    private boolean isDialogShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_registration);
        registrationViewModel = ViewModelProviders.of(this).get(RegistrationViewModel.class);
        if (savedInstanceState != null) {
            if(savedInstanceState.getBoolean(Constants.DIALOG_SHOWN_KEY)){
                showProgressDialog();
            }
        }
        registrationViewModel.getAdminResponseLiveData().observe(this, new Observer<IResponse>() {
            @Override
            public void onChanged(IResponse response) {
                if(response != null){
                    if(progressDialog != null){
                        progressDialog.dismiss();
                        isDialogShown = false;
                    }
                    if(response instanceof RequestResponse){
                        goToSuccessfulRegistrationActivity((RequestResponse) response);
                    }else if (response instanceof ErrorResponse){
                        showErrorToast(RegistrationActivity.this, (ErrorResponse) response);
                    }else{
                        showGeneralErrorToast(RegistrationActivity.this);
                    }
                }
                registrationViewModel.getAdminResponseLiveData().postValue(null);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.DIALOG_SHOWN_KEY,isDialogShown);
    }

    public void registerClicked(View view) throws Exception {
        Register register = new Register(
                binding.registrationNameET.getText().toString(),
                binding.registrationPasswordET.getText().toString(),
                binding.registrationConfirmPasswordET.getText().toString(),
                binding.registrationStoreNumET.getText().toString()
        );
        if(!Validate.isRegisterationRequestDataValid(register)){
            Toast.makeText(getApplicationContext(), R.string.incorrect_registration_input_toast_message
                    ,Toast.LENGTH_SHORT).show();
        }else{
            showProgressDialog();
            registrationViewModel.attemptAdminRegistration(register);
        }
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(RegistrationActivity.this);
        progressDialog.setMessage("please wait....");
        progressDialog.setTitle("Registration in progress");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        isDialogShown = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog != null){
            progressDialog.dismiss();
            isDialogShown = false;
        }
    }

    private void goToSuccessfulRegistrationActivity(RequestResponse response){
        Intent intent = new Intent(RegistrationActivity.this,RegistrationSuccessfulActivity.class);
        intent.putExtra(Constants.INTENT_TAG_REG_TO_REGSUC_EMPOBJ,response);
        startActivity(intent);
        finish();
    }

}
