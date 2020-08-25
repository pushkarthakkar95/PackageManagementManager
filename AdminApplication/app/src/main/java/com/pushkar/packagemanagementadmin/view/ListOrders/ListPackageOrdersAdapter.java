package com.pushkar.packagemanagementadmin.view.ListOrders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pushkar.packagemanagementadmin.R;
import com.pushkar.packagemanagementadmin.model.PackageOrder;
import com.pushkar.packagemanagementadmin.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ListPackageOrdersAdapter extends RecyclerView.Adapter<ListPackageOrdersAdapter.MyViewHolder> {
    private List<PackageOrder> listOfPackages = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    public ListPackageOrdersAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.orders_list_item,parent,false);
        return new MyViewHolder(mItemView,this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PackageOrder currentOrder = listOfPackages.get(position);
        holder.nameTV.setText("Name: "+currentOrder.getName());
        holder.statusTV.setText("Status: "+currentOrder.getPackageStatus().getName());
        holder.trackingNumberTV.setText("Tracking Number: "+currentOrder.getTrackingNumber());
        switch (currentOrder.getPackageStatus().getName()){
            case Constants.IN_TRANSIT_STATUS:
                holder.statusImage.setBackground(context.getDrawable(R.drawable.ic_local_shipping_black_24dp));
                break;
            case Constants.IN_STORE_STATUS:
                holder.statusImage.setBackground(context.getDrawable(R.drawable.ic_store_black_24dp));
                break;
            case Constants.DELIVERED_STATUS:
                holder.statusImage.setBackground(context.getDrawable(R.drawable.ic_sentiment_satisfied_black_24dp));
                break;
            default:
                holder.statusImage.setBackground(context.getDrawable(R.drawable.ic_not_interested_black_24dp));
        }
    }

    @Override
    public int getItemCount() {
        return listOfPackages.size();
    }

    public void setData(List<PackageOrder> listOfPackages){
        this.listOfPackages = listOfPackages;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTV;
        public TextView statusTV;
        public ImageView statusImage;
        public TextView trackingNumberTV;
        ListPackageOrdersAdapter adapter;
        public MyViewHolder(@NonNull View itemView, ListPackageOrdersAdapter adapter) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.ordersItemOrderedByTV);
            statusTV = itemView.findViewById(R.id.ordersItemStatusTV);
            statusImage = itemView.findViewById(R.id.ordersItemStatusIV);
            trackingNumberTV = itemView.findViewById(R.id.ordersTrackingNumberTV);
            this.adapter = adapter;
        }
    }
}
