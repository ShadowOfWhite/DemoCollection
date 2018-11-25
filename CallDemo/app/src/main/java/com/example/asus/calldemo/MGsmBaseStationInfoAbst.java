package com.example.asus.calldemo;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;


/**
 * Created by 今夜犬吠 on 2017/7/31.
 * <p>
 * GSM数据实体
 */

public abstract class MGsmBaseStationInfoAbst
    implements BaseBaseStationInfo {

  /*LAC位置区码*/private String mGsmLac = "-";
  /*RSSI信号强度*/private String mGsmRssi = "-";
  /*CID*/private String mGsmCid = "-";
  /*BSIC基站识别码*/private String mGsmBsic = "-";
  /*中心频点 大于500=1800MHZ*/private String mGsmArfcn = "-";
  /*基站名称 通过Lac和CID去后台获取*/private String mGsmBaseName = "-";

  /*手机状态管理者*/private TelephonyManager mTelephonyManager;
  /*手机状态监听器*/private PhoneStateListener mGsmLins;

  /*回调接口-Activity-Fragment*/public MGsmCallBack mGsmCallBack;

  /*回调接口-service*/public MCidChangeCallBack mCidChangeCallBack;

  /*回调标识-Activity-Fragment*/public int mCallBackMark;

  /*回调标识-Service*/public int mServiceCallBackMark;

  public String getmGsmLac() {
    return mGsmLac;
  }

  public void setmGsmLac(String mGsmLac) {
    this.mGsmLac = mGsmLac;
  }

  public String getmGsmRssi() {
    return mGsmRssi;
  }

  public void setmGsmRssi(String mGsmRssi) {
    this.mGsmRssi = mGsmRssi;
  }

  public String getmGsmCid() {
    return mGsmCid;
  }

  public void setmGsmCid(String mGsmCid) {
    this.mGsmCid = mGsmCid;
  }

  public String getmGsmBsic() {
    return mGsmBsic;
  }

  public void setmGsmBsic(String mGsmBsic) {
    this.mGsmBsic = mGsmBsic;
  }

  public String getmGsmArfcn() {
    return mGsmArfcn;
  }

  public void setmGsmArfcn(String mGsmArfcn) {
    this.mGsmArfcn = mGsmArfcn;
  }

  public String getmGsmBaseName() {
    return mGsmBaseName;
  }

  public void setmGsmBaseName(String mGsmBaseName) {
    this.mGsmBaseName = mGsmBaseName;
  }

  public TelephonyManager getmTelephonyManager() {
    return mTelephonyManager;
  }

  public void setmTelephonyManager(TelephonyManager mTelephonyManager) {
    this.mTelephonyManager = mTelephonyManager;
  }

  public PhoneStateListener getmGsmLins() {
    return mGsmLins;
  }

  public void setmGsmLins(PhoneStateListener mGsmLins) {
    this.mGsmLins = mGsmLins;
  }

  public MGsmCallBack getmGsmCallBack() {
    return mGsmCallBack;
  }

  public void setmGsmCallBack(MGsmCallBack mGsmCallBack) {
    this.mGsmCallBack = mGsmCallBack;
  }

  public int getmCallBackMark() {
    return mCallBackMark;
  }

  public void setmCallBackMark(int mCallBackMark) {
    this.mCallBackMark = mCallBackMark;
  }

  /**
   * 回调接口
   */
  public interface MGsmCallBack {
    void gsmToUpdateUI(int mMark);
  }

  /**
   * 传递回调接口
   */
  protected abstract void setGsmCallBackInter(MGsmCallBack mGsmCallBack, int callBackMark);

}
