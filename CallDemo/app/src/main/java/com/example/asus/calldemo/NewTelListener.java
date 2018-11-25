package com.example.asus.calldemo;

import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 杨淋 on 2018/11/17.
 * Describe：
 */

public class NewTelListener extends PhoneStateListener {
    /*通话时长*/ int time = 0;

    /*是否正在通话*/ boolean isCalling = false;

    /*是否是去电*/ boolean isToCall = true;

    /*是否结束*/ boolean isFinish = false;

    /*观察者*/private Subscription mTimeOb;

    private static final String TAG = "NewTelListener";
    /*语音测试-结果集*/private MVoiceResultInfo mVoiceResultInfo;

    /**
     * 构造初始化
     */
    public NewTelListener() {
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
                Log.e(TAG, "onCallStateChanged: 状态回调：手机空闲" );
                if (isCalling) {

                    isCalling = false;

                    if (mVoiceResultInfo != null && mVoiceResultInfo.getmCallStatus() != null) {
                        mVoiceResultInfo.getmCallStatus().append(MTimeConstants
                                .millis2String(System.currentTimeMillis()) + "   挂断\n");

                        if (!isToCall) {
                            isFinish = true;
                            mVoiceResultInfo.setmCallTime(time);
                        } else {
                            mVoiceResultInfo.setmEndTime(System.currentTimeMillis());
                        }

                        time = 0;
                    }
            /*回调测试页-关闭信息收集*/
                    Message message = new Message();
                    message.what = MConstStr.M_PERFORMANCETEST_SCHEDULING_CelarCall_MARK;
//                    handlerVoice.sendMessage(message);
                    Log.e("CALL", "准备释放信号量");

//                    mSemaphore.release();
                } else {
              /*未通讯时*/
                    if (mVoiceResultInfo != null) {
                        if (mVoiceResultInfo.getmCallStatus() != null) {
                            mVoiceResultInfo.getmCallStatus().append(MTimeConstants
                                    .millis2String(System.currentTimeMillis()) + "   准备通话\n");
                        }
                    }
                }
                Log.e("CALL", "空闲");
                break;
        /*接通*/
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.e(TAG, "onCallStateChanged: 状态回调:接通" );
//                isConnect = true;
                /**接通*/isCalling = true;
                if (mVoiceResultInfo != null) {
                    if (isToCall) {
                        mVoiceResultInfo.setmCallType("去电");
                        if (mVoiceResultInfo.getmCallStatus() != null) {
                            Log.e("CALL", "已进入去电");//isplay Surface is null
                            mVoiceResultInfo.getmCallStatus().append(MTimeConstants
                                    .millis2String(System.currentTimeMillis()) + "   已拨出\n");
                        }
                        //后期使用动态截获日志-判断状态
                    } else {
              /*来电-可计算通话时长*/

                        Log.e(TAG, "onCallStateChanged: 接通了电话" );
                        mTimeOb = Observable.interval(1, TimeUnit.SECONDS)
                                .observeOn(Schedulers.computation())
                                .subscribe(new Action1<Long>() {
                                    @Override
                                    public void call(Long aLong) {
                                        if (isFinish) {
                                            mTimeOb.unsubscribe();
                                        }
                                        time++;
                                    }
                                });
                        if (mVoiceResultInfo.getmCallStatus() != null) {
                            mVoiceResultInfo.getmCallStatus().append(MTimeConstants
                                    .millis2String(System.currentTimeMillis()) + "   接通\n");
                        }
                    }

                }
                Log.e("CALL", "接通");
                break;
        /*来电响铃*/
            case TelephonyManager.CALL_STATE_RINGING:
                Log.e(TAG, "onCallStateChanged: 状态回调:响铃" );
                if (mVoiceResultInfo != null) {
                    if (mVoiceResultInfo.getmCallStatus() != null) {
                        mVoiceResultInfo.getmCallStatus().append(MTimeConstants
                                .millis2String(System.currentTimeMillis()) + "   响铃\n");
                    }
                    // mVoiceResultInfo.setmCallType("来电");
                    isToCall = false;
                }
                Log.e("CALL", "响铃");
                break;
        /*其他状态*/
            default:
                Log.e(TAG, "onCallStateChanged: 状态回调:default");
                break;

        }

    }

}
