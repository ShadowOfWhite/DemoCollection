package com.example.asus.connectwifi;

import android.net.wifi.ScanResult;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 杨淋 on 2018/8/15.
 * Describe：
 */

public class WIFIAdapter extends RecyclerView.Adapter<WIFIAdapter.ViewHolder> {

    List<ScanResult> wifiList;

    public  WIFIAdapter(List list){
        wifiList = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_wifi, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        wifiList.get(position);
        holder.wifiName.setText(wifiList.get(position));
        holder.wifiSignal.setText(wifiList.get(position));


    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView wifiName;
        TextView wifiSignal;

        ViewHolder(View itemView) {
            super(itemView);
            wifiName = itemView.findViewById(R.id.wifiName);
            wifiSignal = itemView.findViewById(R.id.wifiSignal);
        }
    }

}
