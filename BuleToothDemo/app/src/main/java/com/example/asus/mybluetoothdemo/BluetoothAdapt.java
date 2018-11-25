package com.example.asus.mybluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;
import java.util.zip.Inflater;

/**
 * Created by 杨淋 on 2018/9/12.
 * Describe：蓝牙列表适配器
 */

public class BluetoothAdapt extends RecyclerView.Adapter<BluetoothAdapt.MyViewHolder> {


    List<BluetoothDevice> devicesList;
    Context mContext;
    ConnectThread connectThread;//蓝牙连接线程
    BluetoothAdapter bluetoothAdaptr;
    Handler mhandler;
    public static final String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";
//    public static final String MY_UUID = "0000110a-0000-1000-8000-00805f9b34fb";


    public BluetoothAdapt(Context context, List list, BluetoothAdapter adapt, Handler handler){
        devicesList = list;
        mContext = context;
        bluetoothAdaptr = adapt;
        mhandler = handler;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_adapt_bluetooth, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        BluetoothDevice bluetoothDevice = devicesList.get(position);

        if (bluetoothDevice.getName() == null){
            holder.deviceName.setText("设备名为空");
        }else {
            holder.deviceName.setText(bluetoothDevice.getName());
        }
        if (bluetoothDevice.getAddress() == null){
            holder.deviceMac.setText("mac为空");
        }else {
            holder.deviceMac.setText(bluetoothDevice.getAddress());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothAdaptr.cancelDiscovery();
                //点击就开始连接
                Toast.makeText(mContext,"开始连接"+devicesList.get(position).getName(),Toast.LENGTH_SHORT).show();
                connectThread = new ConnectThread(devicesList.get(position), UUID.fromString(MY_UUID));
                connectThread.start();
//                mhandler.obtainMessage( 3,connectThread);
                Message message = new Message();
                message.what = 3;
                message.obj = connectThread;
                mhandler.sendMessage(message);
                Log.d("MainActivity", "onClick: handler发出了数据");
            }
        });

    }

    @Override
    public int getItemCount() {
        return devicesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView deviceName;
        TextView deviceMac;

        public MyViewHolder(View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.deviceName);
            deviceMac = itemView .findViewById(R.id.deviceMac);
        }
    }
}
