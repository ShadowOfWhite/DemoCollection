package com.example.asus.calldemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.asus.ProgressWheel;

import java.lang.reflect.Method;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    /*手机状态监听器*/private PhoneStateListener mLteLins;
    /*手机状态*/private TelephonyManager telephonyManager;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.callPhone);
        final ProgressWheel progressWheel = findViewById(R.id.pw_spinner);
        progressWheel.startSpinning();

        progressWheel.setText("53%");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressWheel.setText("100%");
                        progressWheel.stopSpinning();
                    }
                });
            }
        }).start();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openPhoneStateLis();

                //开始自动拨打电话

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        call();

                        try {
                            mSemaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        try {
                            mSemaphoreVoice.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        closePhoneStateLis();
                    }
                }).start();




            }
        });
    }

    /**
     * 开启监听器
     */
    public void openPhoneStateLis() {
        mLteLins = new TelListener();
        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(mLteLins, PhoneStateListener.LISTEN_CALL_STATE);

    }

    /**
     * 关闭监听器
     */
    private void closePhoneStateLis() {
        if (telephonyManager != null) {
            telephonyManager.listen(mLteLins, PhoneStateListener.LISTEN_NONE);
            mLteLins = null;
        }
    }


    /**
     * 拨出
     */
    private void call() {
    /*回调测试页-开启信息收集*/
        Message message = new Message();
        message.what = MConstStr.M_PERFORMANCETEST_SCHEDULING_Call_MARK;
        handlerVoice.sendMessage(message);

        Intent intent = new Intent(Intent.ACTION_CALL
                , Uri.parse("tel:10086"));

      /*没有权限直接返回*/
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "call: 没有语音权限");
            return;
        }
        startActivity(intent);
    }

    /*收集信号-结果集*/private StringBuffer mCallResultSB;
    /*收集信号-RxJava*/private Subscription mCallSP;
    /*获取基站信息工具-Gsm*/private static MGsmBaseStationInfo mGsmBaseStationInfo ;
    /*基站信息工具-Lte*/private static MRoasTestLteBaseStationInfo mLteBaseStationInfo;
    /*收集信号-小区号*/private String mCallCid = "-1";
    /*收集信号-服务小区切换次数*/private int mCellSwitchCount = 0;
    /*定义一个信号 */private Semaphore mSemaphore = new Semaphore(0);
    /*定义一个信号 语音*/private Semaphore mSemaphoreVoice = new Semaphore(0);
    /*语音测试-结果集*/private MVoiceResultInfo mVoiceResultInfo;
    /*是否接通电话*/ private boolean   isConnect = false;

    private void endCall2(){


        Class<TelephonyManager> c = TelephonyManager.class;



        // 再去反射TelephonyManager里面的私有方法 getITelephony 得到 ITelephony对象
        Method mthEndCall = null;
        try {
            mthEndCall = c.getDeclaredMethod("getITelephony", (Class[]) null);

            //允许访问私有方法
            mthEndCall.setAccessible(true);
            final Object obj = mthEndCall.invoke(telephonyManager, (Object[]) null);

            // 再通过ITelephony对象去反射里面的endCall方法，挂断电话
            Method mt = obj.getClass().getMethod("endCall");
            //允许访问私有方法
            mt.setAccessible(true);
            mt.invoke(obj);


        } catch (Exception e) {
            e.printStackTrace();
        }

//    Toast.makeText(MainActivity.this, "挂断电话！", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "endCall2: 挂断电话" );

    }


    @SuppressLint("HandlerLeak")
    Handler handlerVoice = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MConstStr.M_PERFORMANCETEST_SCHEDULING_Call_MARK:
                    Log.e("语音信号收集", "准备拨打电话："+Thread.currentThread().getName());
                    mCallResultSB = new StringBuffer();
                    /*问题:当Lte切换Gsm时，工具没有回调回来，所以LteCi一直有之前的数据-从而导致GSM进不去*/
                    mCallSP = Observable.interval(1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    if (true) {
                                        Log.e("语音信号收集时间", aLong + "s");
                                        if (aLong >= 5) {
                                            try {
                                                endCall2();
                                            } catch (Exception e) {
                                            }
                                        } else {
                                        }

                                    }
                                }
                            });
                     break;
      /*关闭语音信息收集*/
                case MConstStr.M_PERFORMANCETEST_SCHEDULING_CelarCall_MARK:
                    Log.e("LTESTCommunityFragment", "准备停止");
                    if (mCallSP != null) {
                        isConnect = false;
                        mCallCid = "-1";
                        mCallSP.unsubscribe();

                        mSemaphoreVoice.release();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 语音监听器
     */
    public class TelListener extends PhoneStateListener {

        /*通话时长*/ int time = 0;

        /*是否正在通话*/ boolean isCalling = false;

        /*是否是去电*/ boolean isToCall = true;

        /*是否结束*/ boolean isFinish = false;

        /*观察者*/private Subscription mTimeOb;

        /**
         * 构造初始化
         */
        public TelListener() {
            super();
        }

        /**
         * 呼叫转移指示符更改时调用回调。
         *
         * @param cfi
         */
        @Override
        public void onCallForwardingIndicatorChanged(boolean cfi) {

            super.onCallForwardingIndicatorChanged(cfi);
        }



        /**
         * 手机呼叫状态改变回调
         */
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state) {
        /*空闲*/
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.e(TAG, "onCallStateChanged: 当前状态：空闲" );
                    if (isCalling) {

                        isCalling = false;
            /*回调测试页-关闭信息收集*/
                        Message message = new Message();
                        message.what = MConstStr.M_PERFORMANCETEST_SCHEDULING_CelarCall_MARK;
                        handlerVoice.sendMessage(message);
                        Log.e("CALL", "准备释放信号量");

                        mSemaphore.release();
                    } else {

                    }
                    Log.e("CALL", "空闲");
                    break;
        /*接通*/
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.e(TAG, "onCallStateChanged: 当前状态：接通" );
                    isConnect = true;
                    /**接通*/isCalling = true;

                    Log.e("CALL", "接通");
                    break;
        /*来电响铃*/
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.e(TAG, "onCallStateChanged: 当前状态：响铃" );
                    Log.e("CALL", "响铃");
                    break;
        /*其他状态*/
                default:
                    Log.e(TAG, "onCallStateChanged: 当前状态：其它" );
                    break;

            }

        }
    }

}
