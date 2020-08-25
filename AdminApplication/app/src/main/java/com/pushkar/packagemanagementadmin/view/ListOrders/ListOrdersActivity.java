package com.pushkar.packagemanagementadmin.view.ListOrders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pushkar.packagemanagementadmin.R;
import com.pushkar.packagemanagementadmin.databinding.ActivityListOrdersBinding;
import com.pushkar.packagemanagementadmin.model.PackageOrder;
import com.pushkar.packagemanagementadmin.model.service.ErrorResponse;
import com.pushkar.packagemanagementadmin.model.service.IResponse;
import com.pushkar.packagemanagementadmin.model.service.ListOrdersRequest;
import com.pushkar.packagemanagementadmin.model.service.ListOrdersResponse;
import com.pushkar.packagemanagementadmin.utils.Constants;
import com.pushkar.packagemanagementadmin.view.BaseActivity;
import com.pushkar.packagemanagementadmin.viewmodel.ListOrdersViewModel;

import java.util.List;

public class ListOrdersActivity extends BaseActivity {
    private ActivityListOrdersBinding binding;
    private ListPackageOrdersAdapter adapter;
    private ListOrdersViewModel viewModel;
    private SharedPreferences mPreferences;
    private String storeNum = "";
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_list_orders);
        mPreferences = getSharedPreferences(Constants.SHARED_PREF_STRING,MODE_PRIVATE);
        storeNum = mPreferences.getString(Constants.SHARED_PREF_STORE_NUM,"");
        adapter = new ListPackageOrdersAdapter(this);
        binding.listOrdersRV.setAdapter(adapter);
        binding.listOrdersRV.setLayoutManager(new LinearLayoutManager(this));
        viewModel = ViewModelProviders.of(this).get(ListOrdersViewModel.class);
        viewModel.getListOfPackagesLiveData().observe(this, new Observer<IResponse>() {
            @Override
            public void onChanged(IResponse iResponse) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                if(iResponse instanceof ListOrdersResponse){
                    ListOrdersResponse listOrdersResponse = (ListOrdersResponse) iResponse;
                    adapter.setData(listOrdersResponse.getPackageOrderList());
                }else if(iResponse instanceof ErrorResponse){
                    showErrorToast(ListOrdersActivity.this,(ErrorResponse) iResponse);
                }else{
                    showGeneralErrorToast(ListOrdersActivity.this);
                }
            }
        });
        fetchLatestListOfPackages();

        binding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchLatestListOfPackages();
                binding.swipeContainer.setRefreshing(false);
            }
        });
    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(ListOrdersActivity.this);
        progressDialog.setMessage("please wait....");
        progressDialog.setTitle("Fetching all packages");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }


    private void fetchLatestListOfPackages(){
        if(!storeNum.equals("")){
            showProgressDialog();
            viewModel.getListOfOrders(new ListOrdersRequest(storeNum));
        }else{
            showGeneralErrorToast(ListOrdersActivity.this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
