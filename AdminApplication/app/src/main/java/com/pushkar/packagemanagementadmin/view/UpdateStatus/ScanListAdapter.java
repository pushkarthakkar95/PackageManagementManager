package com.pushkar.packagemanagementadmin.view.UpdateStatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pushkar.packagemanagementadmin.R;
import com.pushkar.packagemanagementadmin.model.service.UpdatePackageStatusDHO;

import java.util.ArrayList;
import java.util.List;

public class ScanListAdapter extends RecyclerView.Adapter<ScanListAdapter.BarcodeViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    private List<UpdatePackageStatusDHO> barcodeList = new ArrayList<>();

    public ScanListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<UpdatePackageStatusDHO> barcodes){
        this.barcodeList = barcodes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BarcodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.barcode_item,parent,false);
        return new BarcodeViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull BarcodeViewHolder holder, int position) {
        String barcodeStr = barcodeList.get(position).getTrackingNumber();
        holder.barcodeTV.setText(barcodeStr);
    }

    @Override
    public int getItemCount() {
        return barcodeList.size();
    }

    public static class BarcodeViewHolder extends RecyclerView.ViewHolder{
        private ScanListAdapter adapter;
        public TextView barcodeTV;
        public BarcodeViewHolder(@NonNull View itemView, ScanListAdapter adapter) {
            super(itemView);
            barcodeTV = itemView.findViewById(R.id.barcodeItemBarcodeTV);
            this.adapter = adapter;
        }
    }
}
