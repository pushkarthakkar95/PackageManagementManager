package com.pushkar.packagemanagementadmin.view.Registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pushkar.packagemanagementadmin.R;
import com.pushkar.packagemanagementadmin.databinding.ActivityRegistrationSuccessfulBinding;
import com.pushkar.packagemanagementadmin.model.service.RequestResponse;
import com.pushkar.packagemanagementadmin.utils.Constants;
import com.pushkar.packagemanagementadmin.view.AdminLoginActivity;

public class RegistrationSuccessfulActivity extends AppCompatActivity {
    private ActivityRegistrationSuccessfulBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_registration_successful);
        RequestResponse requestResponse = (RequestResponse) getIntent().getSerializableExtra(Constants.INTENT_TAG_REG_TO_REGSUC_EMPOBJ);
        if(requestResponse != null){
            binding.registrationSuccessEmployeeIDTV.setText(requestResponse.getEmployeeId());
        }
    }

    public void loginClicked(View view) {
        Intent intent = new Intent(RegistrationSuccessfulActivity.this, AdminLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
