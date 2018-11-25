package com.example.asus.mybluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

/**
 * Created by 杨淋 on 2018/9/12.
 * Describe：连接线程，作客户端时用
 */

public class ConnectThread  extends Thread{
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private static final String TAG = "ConnectThread";

    public ConnectThread(BluetoothDevice device, UUID serviceUUID) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(serviceUUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }

    @Override
    public void run() {
        // Cancel discovery because it will slow down the connection
//        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            Log.d(TAG, "run: 开始连接蓝牙");
            mmSocket.connect();
            Log.d(TAG, "run: 蓝牙已成功连接上");
        } catch (IOException connectException) {
            Log.d(TAG, "run: 蓝牙连接失败了");
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }

    }

    public  BluetoothSocket getConnectSocket(){
        return mmSocket;
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
