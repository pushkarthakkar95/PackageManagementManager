package com.pushkar.packagemanagementadmin.view.UpdateStatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pushkar.packagemanagementadmin.R;
import com.pushkar.packagemanagementadmin.model.FailedPackageStatusDHO;

import java.util.List;

public class AlertDialogRecyclerViewAdapter extends RecyclerView.Adapter<AlertDialogRecyclerViewAdapter.FailedBarcodeViewHolder> {
    private LayoutInflater mInflater;
    private List<FailedPackageStatusDHO> listOfFailedPackages;
    public AlertDialogRecyclerViewAdapter(Context context, List<FailedPackageStatusDHO> list) {
        this.mInflater = LayoutInflater.from(context);
        this.listOfFailedPackages = list;
    }

    @NonNull
    @Override
    public FailedBarcodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mitemView = mInflater.inflate(R.layout.failed_barcode_status_item,parent,false);
        return new FailedBarcodeViewHolder(mitemView,this);
    }

    public void setData(List<FailedPackageStatusDHO> list){
        this.listOfFailedPackages = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull FailedBarcodeViewHolder holder, int position) {
        FailedPackageStatusDHO failedPackage = listOfFailedPackages.get(position);
        holder.trackingNumTv.setText(failedPackage.getTrackingNumber());
        holder.reasonTV.setText(failedPackage.getReason());
    }

    @Override
    public int getItemCount() {
        return listOfFailedPackages.size();
    }

    public static class FailedBarcodeViewHolder extends RecyclerView.ViewHolder{
        private AlertDialogRecyclerViewAdapter adapter;
        public TextView trackingNumTv;
        public TextView reasonTV;
        public FailedBarcodeViewHolder(@NonNull View itemView, AlertDialogRecyclerViewAdapter adapter) {
            super(itemView);
            trackingNumTv = itemView.findViewById(R.id.trackingNumberTV);
            reasonTV = itemView.findViewById(R.id.reasonTV);
            this.adapter = adapter;
        }
    }

}
