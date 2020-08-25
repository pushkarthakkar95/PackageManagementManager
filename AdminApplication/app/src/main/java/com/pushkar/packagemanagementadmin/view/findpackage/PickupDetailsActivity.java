package com.pushkar.packagemanagementadmin.view.findpackage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.pushkar.packagemanagementadmin.R;
import com.pushkar.packagemanagementadmin.databinding.ActivityPickupDetailsBinding;
import com.pushkar.packagemanagementadmin.utils.Constants;

import java.util.ArrayList;

public class PickupDetailsActivity extends AppCompatActivity {
    ArrayList<String> listOfBarcodes;
    String customerName;
    private final String TAG = PickupDetailsActivity.class.getSimpleName();
    private ActivityPickupDetailsBinding binding;
    private FindPackageListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_pickup_details);
        listOfBarcodes = getIntent().getStringArrayListExtra(Constants.BARCODE_LIST_NOTIFICATION_MESSAGE);
        customerName = getIntent().getStringExtra(Constants.NAME_IN_NOTIFICATION);
        binding.customerNameTV.setText(customerName);
        binding.customerNameTV.setPaintFlags(binding.customerNameTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if(listOfBarcodes!=null && !listOfBarcodes.isEmpty()){
            adapter = new FindPackageListAdapter(listOfBarcodes,this);
            binding.findPackageRV.setAdapter(adapter);
            binding.findPackageRV.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    public void onBackPressed() {
        showAlertDialog();
    }

    private void showAlertDialog(){
        AlertDialog dialog = new AlertDialog.Builder(PickupDetailsActivity.this)
                .setTitle("Are you sure?")
                .setMessage("You will not be able to see this list of packages again, are you sure you are done?")
                .setCancelable(false)
                .setPositiveButton("Yes, I am Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No, Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    public void doneBtnClicked(View view) {
        showAlertDialog();
    }
}
