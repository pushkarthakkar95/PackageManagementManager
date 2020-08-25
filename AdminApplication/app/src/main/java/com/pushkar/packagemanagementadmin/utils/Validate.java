package com.pushkar.packagemanagementadmin.utils;

import android.util.Log;

import com.pushkar.packagemanagementadmin.model.Register;
import com.pushkar.packagemanagementadmin.model.service.LoginRequestData;

import java.util.ArrayList;

public class Validate {
    private static final String TAG = Validate.class.getSimpleName();
    private static final String VALID_BARCODE_PREFIX = "E";
    public static boolean isRegisterationRequestDataValid(Register register){
        if(register.getName().isEmpty() || register.getConfirmPassword().isEmpty() || register.getPassword().isEmpty() ||
                register.getStoreNum().isEmpty()){
            return false;
        }
        if(!register.getPassword().equals(register.getConfirmPassword())){
            return false;
        }
        return register.getPassword().length() >= Constants.MIN_VALID_PASSWORD_LENGTH;
    }

    public static boolean isLoginRequestDataValid(LoginRequestData loginData){
        if(loginData.getEmployeeId().isEmpty() || loginData.getPassword().isEmpty())
            return false;
        return loginData.getPassword().length() >= 5;
    }

    public static boolean isBarcodeValid(String barcode){
        if(barcode == null || barcode.length() != 9)
            return false;
        return barcode.startsWith(VALID_BARCODE_PREFIX);
    }
}
