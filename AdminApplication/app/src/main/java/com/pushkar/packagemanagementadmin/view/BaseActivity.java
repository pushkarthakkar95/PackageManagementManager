package com.pushkar.packagemanagementadmin.view;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;

public abstract class BaseActivity extends AppCompatActivity {

    protected void showGeneralErrorToast(Context context){
        Toast.makeText(context,"Something went wrong with the system. Please try again later",Toast.LENGTH_SHORT)
                .show();
    }

    protected void showErrorToast(Context context, ErrorResponse errorResponse){
        Toast.makeText(context,errorResponse.toString(),Toast.LENGTH_SHORT).show();
    }
}
