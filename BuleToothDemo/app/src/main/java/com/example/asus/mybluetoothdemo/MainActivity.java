package com.example.asus.mybluetoothdemo;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    Button searchButton;
    Button stopSearchBluetooth;
    Button openBluetooth;
    Button closeBluetooth;
    Button startConnect;
    AppCompatButton sendDataButton;
    TextView receiveData;
    RecyclerView devicesListRecycler;

    Context mContext ;
    BluetoothAdapter bluetoothAdapter;
    ArrayAdapter pairedDevicesAdapter;//已配对的蓝牙设备
    ArrayAdapter discoverDevicesAdapter;//搜索到的蓝牙设备
    IntentFilter filter;//广播意图过滤器
    List<BluetoothDevice> devices;//蓝牙设备集合
    BluetoothAdapt bluetoothAdapt;//蓝牙显示适配器
    IOThread ioThread;//数据传输线程
    ConnectThread connectThread;//客户端线程


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 2://收到了数据
                    receiveData.setText((String)msg.obj);
                    break;
                case 3://收到了acceptThtead
                    Log.d(TAG, "handlerMessage: 收到了handler数据");
                    connectThread = (ConnectThread) msg.obj;
                    if (connectThread == null){
                        receiveData.setText("连接线程为空");
                        Log.d(TAG, "handlerMessage: 连接线程为空");
                    }
                    break;
                default:
                    break;
            }

        }
    };


    public static final int EQUEST_ENABLE_BT = 1;
    private static final String TAG = "MainActivity";
    private static final String string = "hallo world!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListenler();
        configAndInit();

    }

    private void initListenler() {
        searchButton.setOnClickListener(this);
        stopSearchBluetooth.setOnClickListener(this);
        openBluetooth.setOnClickListener(this);
        closeBluetooth.setOnClickListener(this);
        startConnect.setOnClickListener(this);
        sendDataButton.setOnClickListener(this);
    }

    private void initView() {

        searchButton = findViewById(R.id.searchBluetooth);
        stopSearchBluetooth = findViewById(R.id.stopSearchBluetooth);
        openBluetooth = findViewById(R.id.openBuletooth);
        closeBluetooth = findViewById(R.id.closeBluetooth);
        startConnect = findViewById(R.id.startConnect);
        devicesListRecycler = findViewById(R.id.devicesListRecycler);
        sendDataButton = findViewById(R.id.sendData);
        receiveData = findViewById(R.id.receiveData);

    }

    private void configAndInit() {

        mContext = getApplicationContext();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedDevicesAdapter = new ArrayAdapter(mContext,android.R.layout.simple_list_item_1);
        discoverDevicesAdapter = new ArrayAdapter(mContext,android.R.layout.simple_list_item_1);
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_UUID);
        devices = new ArrayList<>();
        bluetoothAdapt = new BluetoothAdapt(mContext,devices,bluetoothAdapter,mHandler);


        devicesListRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        devicesListRecycler.setAdapter(bluetoothAdapt);
        //添加Android自带的分割线
        devicesListRecycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        registerReceiver(mReceiver,filter);//注册广播
        if (bluetoothAdapter == null){
            Toast.makeText(mContext,"设备不支持蓝牙功能",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.searchBluetooth:
                searchBluetooth();
                break;
            case R.id.stopSearchBluetooth:
                stopSearchBluetooth();
                break;
            case R.id.closeBluetooth:
                closeBluetooth();
                break;
            case R.id.openBuletooth:
                openBluetooth();
                break;
            case R.id.startConnect:
                startConnect();
                break;
            case R.id.sendData:
                startSendData();
            default:
                break;
        }

    }

    private void startSendData() {
        if (connectThread.getConnectSocket() == null){
            receiveData.setText("socket为空");
            return;
        }
        ioThread = new IOThread(connectThread.getConnectSocket(),mHandler);
        ioThread.write(string.getBytes());
        ioThread.start();
    }

    private void startConnect() {
        getDeviceOfPaired();

    }

    private void openBluetooth() {
        Log.d(TAG, "openBluetooth: 按下打开蓝牙");
        if (!bluetoothAdapter.enable()){
            Intent openBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(openBtIntent,EQUEST_ENABLE_BT);
        }

}

    private void closeBluetooth() {
        Log.d(TAG, "closeBluetooth:按下关闭蓝牙 ");
    }

    private void stopSearchBluetooth() {
        Log.d(TAG, "stopSearchBluetooth: 按下停止搜索蓝牙");
        bluetoothAdapter.cancelDiscovery();
    }

    private void searchBluetooth() {
        bluetoothAdapter.cancelDiscovery();//搜索之前先关闭搜索，不然可能会失败
        devices.clear();
        bluetoothAdapt.notifyDataSetChanged();
        Log.d(TAG, "searchBluetooth: 按下开始搜索蓝牙,搜索结果为："+ bluetoothAdapter.startDiscovery());
       //搜索设备，异步，会返回boolea值指示是否成功启动发现操作,
        // 执行设备发现对于蓝牙适配器而言是一个非常繁重的操作过程，并且会消耗大量资源
       //如果您已经保持与某台设备的连接，那么执行发现操作可能会大幅减少可用于该连接的带宽，
        // 因此不应该在处于连接状态时执行发现操作。
        //一次搜索大概12秒
    }

    /**
     * 获取已配对的设备信息
     */
    private void getDeviceOfPaired(){
        Set<BluetoothDevice> pairedDevices  = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0){
            devices.clear();
            for (BluetoothDevice bluetoothDevice:pairedDevices) {
                devices.add(bluetoothDevice);
//                pairedDevicesAdapter.add(bluetoothDevice.getName()+"\n"+bluetoothDevice.getAddress());
            }
            bluetoothAdapt.notifyDataSetChanged();
            receiveData.setText(devices.get(0).getUuids()[0].toString() == null?"uuid为空":devices.get(0).getUuids()[0].toString());
        }

    }

    /**
     * 设置设备可见性,如未启用蓝牙,会先启用蓝牙
     *  默认情况下，设备将变为可检测到并持续 120 秒钟
     *  应用可以设置的最大持续时间为 3600 秒，值为 0 则表示设备始终可检测到。
     *  任何小于 0 或大于 3600 的值都会自动设为 120 秒
     */
    private void  setDiscoverable(){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        startActivity(discoverableIntent);
    }

    /**
     * 蓝牙搜索广播接收器，每搜到一个蓝牙都会接收一条广播
     */
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: 广播收到了信息："+action);
            //当发现一个设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devices.add(device);
                bluetoothAdapt.notifyDataSetChanged();
                discoverDevicesAdapter.add(device.getName()+"\n"+device.getAddress());
                Log.v(TAG, "onReceive: "+device.getName()+"\n"
                        +device.getAddress()+"\n");
                if (device.getUuids() != null && device.getUuids().length!=0){
                    Log.d(TAG, "onReceive: "+device.getUuids().length+","+device.getUuids()[0].toString());
                }else {
                    Log.d(TAG, "onReceive: uuid为空");
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: 收到了其他活动给的信息");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
