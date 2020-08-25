package com.pushkar.packagemanagementadmin.view.findpackage;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pushkar.packagemanagementadmin.R;

import java.util.ArrayList;
import java.util.List;

public class FindPackageListAdapter extends RecyclerView.Adapter<FindPackageListAdapter.FindPackageViewHolder> {
    private List<String> barcodeList = new ArrayList<>();
    private LayoutInflater mInflater;

    public FindPackageListAdapter(List<String> barcodeList, Context context) {
        this.barcodeList = barcodeList;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FindPackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.find_package_list,parent,false);
        return new FindPackageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FindPackageViewHolder holder, int position) {
        String barcode = barcodeList.get(position);
        holder.barcodesTV.setText(barcode);
    }

    @Override
    public int getItemCount() {
        return barcodeList.size();
    }

    public static class FindPackageViewHolder extends RecyclerView.ViewHolder{
        private FindPackageListAdapter adapter;
        public TextView barcodesTV;
        public FindPackageViewHolder(@NonNull View itemView) {
            super(itemView);
            barcodesTV = itemView.findViewById(R.id.barcodeFindTV);
        }
    }
}
