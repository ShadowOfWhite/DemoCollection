package com.example.asus.calldemo;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;


/**
 * Created by 今夜犬吠 on 2017/6/6.
 *
 * Lte数据实体
 */

public abstract class MLteBaseStationInfoAbst implements BaseBaseStationInfo {

    /*小区识别码*/private String mLteCI = "-";
    /*CellId*/private String mLteCellId = "-";
    /*主基站物理小区标识*/private String mLtePCI = "-";
    /*中心频点*/private int mLteEarfcn=-1;
    /*eNodBeId*/private int mLteENodBeId=-1;
    /*跟踪区域码*/private String mLteTAC = "-";
    /*信号强度*/private String mLteRSRP = "-";
    /*信号接收质量*/private String mLteRSRQ = "-";
    /*独立信号单元*/private String mLteAsu = "-";
    /*信噪比*/private int mLteSINR=-1;

    /*手机状态管理者*/private TelephonyManager mTelephonyManager;
    /*手机状态监听器*/private PhoneStateListener mLteLins;
    /*回调接口-Activity*/public MLteCallBack mLteCallBack;
    /*回调接口-service*/public MCidChangeCallBack mCidChangeCallBack;
    /*回调标识-activity*/public int mCallBackMark;
    /*回调标识-Service*/public int mServiceCallBackMark;
    /**
     * =================外部接口===============================
     */

    public PhoneStateListener getmLteLins() {
        return mLteLins;
    }

    public void setmLteLins(PhoneStateListener mLteLins) {
        this.mLteLins = mLteLins;
    }

    public String getmLteCI() {
        return mLteCI;
    }

    public void setmLteCI(String mLteCI) {
        this.mLteCI = mLteCI;
    }

    public String getmLteCellId() {
        return mLteCellId;
    }

    public void setmLteCellId(String mLteCellId) {
        this.mLteCellId = mLteCellId;
    }

    public String getmLtePCI() {
        return mLtePCI;
    }

    public void setmLtePCI(String mLtePCI) {
        this.mLtePCI = mLtePCI;
    }

    public int getmLteEarfcn() {
        return mLteEarfcn;
    }

    public void setmLteEarfcn(int mLteEarfcn) {
        this.mLteEarfcn = mLteEarfcn;
    }

    public int getmLteENodBeId() {
        return mLteENodBeId;
    }

    public void setmLteENodBeId(int mLteENodBeId) {
        this.mLteENodBeId = mLteENodBeId;
    }

    public String getmLteTAC() {
        return mLteTAC;
    }

    public void setmLteTAC(String mLteTAC) {
        this.mLteTAC = mLteTAC;
    }

    public String getmLteRSRP() {
        return mLteRSRP;
    }

    public void setmLteRSRP(String mLteRSRP) {
        this.mLteRSRP = mLteRSRP;
    }

    public String getmLteRSRQ() {
        return mLteRSRQ;
    }

    public void setmLteRSRQ(String mLteRSRQ) {
        this.mLteRSRQ = mLteRSRQ;
    }

    public String getmLteAsu() {
        return mLteAsu;
    }

    public void setmLteAsu(String mLteAsu) {
        this.mLteAsu = mLteAsu;
    }

    public int getmLteSINR() {
        return mLteSINR;
    }

    public void setmLteSINR(int mLteSINR) {
        this.mLteSINR = mLteSINR;
    }

    public TelephonyManager getmTelephonyManager() {
        return mTelephonyManager;
    }

    public void setmTelephonyManager(TelephonyManager mTelephonyManager) {
        this.mTelephonyManager = mTelephonyManager;
    }

    /**
     * 传递回调接口
     */
    protected  abstract void setCallBackInter(MLteCallBack mLteRoadTestCallBack, int callBackMark);

    /**
     * 回调接口-用于Activity-刷新UI
     */
    public interface MLteCallBack {
        void toUpdateUI(int mMark);
    }
}
