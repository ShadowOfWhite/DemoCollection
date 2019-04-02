package com.example.asus.solibtestdemo;

import android.Manifest;
import android.content.Context;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.asus.solibtestdemo.util.DeviceCompatibilityChecker;
import com.example.asus.solibtestdemo.util.Order;
import com.example.asus.solibtestdemo.util.Utils;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import engine.ch.datachecktool.library.bean.MLteServiceCellInfoModel;
import engine.ch.datachecktool.library.bean.MNeighInfoListBean;
import engine.ch.datachecktool.library.bean.MSignaBean;
import engine.ch.datachecktool.library.bean.MSignaEventBean;
import engine.ch.datachecktool.library.model.MSimBaseModel;
import engine.ch.datachecktool.library.model.lte.MLteBandDataModel;
import engine.ch.datachecktool.library.signaling.MSignaCapturer;
import engine.ch.datachecktool.library.signaling.MSignaDataCallbackI;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements MSignaDataCallbackI {

    Button startButton;
    Button stopButton;
    private static final String TAG = "MainActivity";
    MSignaCapturer mSignaCapturer;

    //读写Diag口进程
    Process helper;
    //diag口输出流
    BufferedReader diagStdout;
    //diag口输入流
    DataOutputStream diagStdin;
    //diag口错误流
    private BufferedReader diagStderr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSignaCapturer = new MSignaCapturer(MainActivity.this);
        new RxPermissions(this)
                .request(Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean){
                            mSignaCapturer.toolSendCallBackI(MainActivity.this)//传递回调接口
                                    .toolSetCapturerInterval(500)//设置主要数据回调时间
                                    .toolRequestControl(true)//暂停重启回调
                                    .toolStart();//开始 一次就可以了
                        }
                    }
                });



        startButton = findViewById(R.id.start);
        stopButton = findViewById(R.id.shutdown);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    launchHelper();

//                    new FromDiagThread().start();

//                    Order order = new Order();
//                    order.setmLockType(1);
//                    Gson gson = new Gson();
////                    String gsonStr = gson.toJson(order);
//                    String gsonStr = "{" +
//                            "\"mLockType\":1," +
//                            "\"mLockData\":[" +
//                            "]" +
//                            "}";
//                    Log.i(TAG, "onClick: 获取锁频指令为："+gsonStr);
//
//                    byte[] bytes = gsonStr.getBytes();
//
//                    if (diagStdin != null){
//                        diagStdin.writeInt(bytes.length);
//                        diagStdin.write(bytes);
//                        diagStdin.flush();
//                    }
//
//
//                } catch (IOException e) {
//                    Log.e(TAG, "onClick: 异常" );
//                    e.printStackTrace();
//                }



                List<MLteBandDataModel> mLetBandList=mSignaCapturer.toolGetBandOfLte();

                if (mLetBandList != null){

                    if (mLetBandList.size()>0){
                        for (MLteBandDataModel b:mLetBandList) {
                            Log.i(TAG, "onClick: 支持频段："+b.getB());
                        }
                    }

                }


            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shutdown(false);
            }
        });
    }


    private void launchHelper() throws IOException {
        String libDir = this.getApplicationInfo().nativeLibraryDir;
        String diag_helper = libDir + "/libdiag_helper.so";
        String su_binary = DeviceCompatibilityChecker.getSuBinary();
        if (su_binary == null) {
            Log.e(TAG, "launchHelper:su_binary为空，异常 ");
            return;
        }
        String cmd[] = {su_binary, "-c", "exec " + diag_helper + " run"};

        helper = Runtime.getRuntime().exec(cmd);

        this.diagStdin = new DataOutputStream(helper.getOutputStream());

        //this.diagStdout = new DataInputStream(helper.getInputStream());
        this.diagStdout =new BufferedReader(new InputStreamReader(helper.getInputStream()));

        this.diagStderr = new BufferedReader(new InputStreamReader(helper.getErrorStream()));

        char[] handshakeBytes = new char[4];
        try {
            diagStdout.read(handshakeBytes);
        } catch (IOException e) {
            this.helper = null;
            this.diagStdout = null;
            this.diagStdin = null;
            this.diagStderr = null;
            throw new IOException("handshake from helper not successful:" + e.getMessage());
        }
        String handshake = new String(handshakeBytes).trim();
//        DiagService.this.diagStdout.read(handshakeBytes);
//        String handshake = new String(handshakeBytes, "ASCII");
        if (handshake.equals("OKAY")) {
            Log.i(TAG, "launchHelper:handshake from helper successful ");
        } else {
            Log.e(TAG, "launchHelper: 读写流创建失败" );
            throw new IOException("handshake from helper not successful");
        }
        try {
            int ret = helper.exitValue();
            throw new IOException("helper exited prematurely (" + ret + ")");
        } catch (IllegalThreadStateException e) {
            // helper still running, success.
        }

    }

    private synchronized boolean shutdown(boolean shuttingDownAlreadySet) {

        /**
         * 停止diag口信令读写进程
         */
        if (helper != null) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        helper.waitFor();//
                    } catch (InterruptedException e) {

                    }
                }
            };
            t.start();

            try {
                t.join(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            t.interrupt();
            try {
                int exitValue = helper.exitValue();
                Log.e(TAG,"Helper terminated with exitvalue " + exitValue);
            } catch (IllegalThreadStateException e) {

                helper.destroy();
            }
            helper = null;
        }

        return true;
    }

    @Override
    public void toolMmobileData(String s) {

    }

    @Override
    public void toolSimOneData(MSimBaseModel mSimBaseModel) {

    }

    @Override
    public void toolSimTwoData(MSimBaseModel mSimBaseModel) {

    }

    @Override
    public void toolDefaultData(MLteServiceCellInfoModel mLteServiceCellInfoModel) {

    }

    @Override
    public void toolNeiData(MNeighInfoListBean mNeighInfoListBean) {

    }

    @Override
    public void toolEventData(MSignaEventBean mSignaEventBean) {

    }

    @Override
    public void toolRrcData(MSignaBean mSignaBean) {

    }

    /**
     * diag口输出处理线程
     */
    class FromDiagThread extends Thread {
        @Override
        public void run() {
            try {
                
                while (true) {
                    if (diagStdout != null){
                        String line = diagStdout.readLine();
                        Log.i(TAG, "run: 收到的数据为："+line);
                        if (line.trim().length() == 0)
                            continue;

                    }else {

                        Log.i(TAG, "run: nullnullnullnullnullnullnull");
                        
                    }

                }
            } catch (IOException e) {

                Log.e(TAG, "run: 读取流异常" );
            }
        }
    }



}
