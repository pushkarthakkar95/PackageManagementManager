package com.pushkar.packagemanagementadmin.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.pushkar.packagemanagementadmin.R;
import com.pushkar.packagemanagementadmin.utils.Constants;
import com.pushkar.packagemanagementadmin.utils.ParserHelper;
import com.pushkar.packagemanagementadmin.view.findpackage.PickupDetailsActivity;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private final String TAG = SplashActivity.class.getSimpleName();
    private String notificationMessageString = "";
    private String notificationNameString = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_STRING, MODE_PRIVATE);
        Intent intent = getIntent();
        notificationMessageString = "";
        notificationNameString = "";
        if(intent.getAction()!=null && intent.getAction().equals("GO_TO_DETAILS") && intent.getExtras()!=null){
            notificationMessageString = intent.getExtras().getString("notification_message");
            notificationNameString = intent.getExtras().getString("notification_title");
        }
        splash();
    }

    private boolean isUserLoggedIn(){
        return sharedPreferences.getBoolean(Constants.SHARED_PREF_IS_USER_LOGGED_IN,false);
    }

    private boolean isUserTimedOut(){
        return ParserHelper.timedOut(sharedPreferences.getString(Constants.SHARED_PREF_LAST_LOG_IN_TIME,"0"));
    }

    private void goToLoginScreen(){
        Intent intent = new Intent(SplashActivity.this,AdminLoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToHomeScreen(){
        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToInfoScreen(){
        ArrayList<String> barcodes = ParserHelper.getListOfParsedBarcodes(notificationMessageString);
        Intent intent = new Intent(SplashActivity.this,PickupDetailsActivity.class);
        intent.putStringArrayListExtra(Constants.BARCODE_LIST_NOTIFICATION_MESSAGE,barcodes);
        intent.putExtra(Constants.NAME_IN_NOTIFICATION,ParserHelper.getNameFromTitle(notificationNameString));
        startActivity(intent);
        finish();
    }

    private void splash(){
        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 2 seconds
                    sleep(2*1000);

                    // After 2 seconds redirect to another intent
                    goToNextScreen();

                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();
    }

    private void goToNextScreen(){
        if(isUserLoggedIn()){
            if(!notificationMessageString.isEmpty()){
                goToInfoScreen();
            }else{
                if(!isUserTimedOut()){
                    goToHomeScreen();
                }else{
                    goToLoginScreen();
                }
            }
        }else{
            goToLoginScreen();
        }
    }
}
