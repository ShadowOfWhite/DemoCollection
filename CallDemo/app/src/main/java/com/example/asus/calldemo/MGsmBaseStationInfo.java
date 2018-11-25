package com.example.asus.calldemo;

import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;


/**
 * Created by 今夜犬吠 on 2017/7/31.
 * GSM基站数据获取
 */

public class MGsmBaseStationInfo extends MGsmBaseStationInfoAbst {

  /*当前Lte的CID*/private int mGsmCid = -1;

  /**
   * 构造
   */
  public MGsmBaseStationInfo(Context mContext) {
    /**实例化手机管理者*/setmTelephonyManager((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE));
  }

  /**
   * 获取所有小区信息
   * <p>
   * 提取Lte小区信息
   */
  public void getGsmAllCellInfo() {
    /**获取所有小区信息*/List<CellInfo> mCellInfoList = getmTelephonyManager().getAllCellInfo();
    if (mCellInfoList != null && mCellInfoList.size() > 0) {
      Log.e("2G", "准备获取所有小区信息");
      /**获取基站数据*/getBaseStationData(mCellInfoList);
    }
  }

  /**
   * 获取所有小区信息
   *
   * @param mCellInfoList
   */
  @Override
  public void getBaseStationData(List<CellInfo> mCellInfoList) {
    /**各小区*/
    for (CellInfo mCellInfo : mCellInfoList) {
      /**Lte小区*/
      if (mCellInfo instanceof CellInfoGsm) {
        CellInfoGsm mCellInfoGsm = (CellInfoGsm) mCellInfo;

        /**当前手机所在小区*/
        if (mCellInfoGsm.isRegistered()) {
          Log.e("2G", "捕获一个GSM小区，CID:" + mCellInfoGsm.getCellIdentity().getCid());

          /**LAC*/
          if (mCellInfoGsm.getCellIdentity().getLac() != Integer.MAX_VALUE) {
            setmGsmLac(mCellInfoGsm.getCellIdentity().getLac() + "");
          } else {
            setmGsmLac("-");
          }

          /**CID*/
          if (mCellInfoGsm.getCellIdentity().getCid() != Integer.MAX_VALUE) {
            setmGsmCid(mCellInfoGsm.getCellIdentity().getCid() + "");
          } else {
            setmGsmCid("-");
          }

          /**Arfcn*/
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (mCellInfoGsm.getCellIdentity().getBsic() != Integer.MAX_VALUE) {
              setmGsmArfcn(mCellInfoGsm.getCellIdentity().getArfcn() + "");
            }
          }
          /**RSSI*/
          if (mCellInfoGsm.getCellSignalStrength().getDbm() != Integer.MAX_VALUE &&
              mCellInfoGsm.getCellSignalStrength().getDbm() != 99
              && mCellInfoGsm.getCellSignalStrength().getDbm() != 0) {
            setmGsmRssi(mCellInfoGsm.getCellSignalStrength().getDbm() + "");
          } else {
            /**获取GSM信号强度*/setmGsmRssi("-");
          }

          /**BSIC*/
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (mCellInfoGsm.getCellIdentity().getBsic() != Integer.MAX_VALUE) {
              setmGsmBsic(mCellInfoGsm.getCellIdentity().getBsic() + "");
            }
          }

        } else {
          Log.e("2g邻区小区", "" + mCellInfoGsm.getCellIdentity().getLac());

        }
      }
    }
  }

  /**
   * 开启监听器
   */
  @Override
  public void openPhoneStateLis() {
    Log.e("GSM", "正在开始监听");
    /**实例化监听*/setmGsmLins(new GSMPhoneStateListener());
    /**开启监听*/getmTelephonyManager().listen(getmGsmLins()
        , PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
            | PhoneStateListener.LISTEN_CELL_INFO
            | PhoneStateListener.LISTEN_CELL_LOCATION);
  }

  /**
   * 关闭监听器
   */
  @Override
  public void closePhoneStateLis() {
    if (getmGsmLins() != null) {
      getmTelephonyManager().listen(getmGsmLins(), PhoneStateListener.LISTEN_NONE);
      mGsmCallBack = null;
      setmGsmLins(null);
    }
  }


  /**
   * 传递回调接口-Gsm
   *
   * @param mGsmCallBack
   * @param callBackMark
   */
  @Override
  public void setGsmCallBackInter(MGsmCallBack mGsmCallBack, int callBackMark) {
    Log.e("2G", "开始传递回调接口");
    this.mGsmCallBack = mGsmCallBack;
    this.mCallBackMark = callBackMark;
  }

  /**
   * 传递回调接口
   *
   * @param mCidChangeCallBack
   * @param callBackMark
   */
  @Override
  public void setServiceCallBackInter(MCidChangeCallBack mCidChangeCallBack, int callBackMark) {
    this.mCidChangeCallBack = mCidChangeCallBack;
    this.mServiceCallBackMark = callBackMark;
  }


  /**
   * 内部类-手机状态监听器-Lte
   */
  public class GSMPhoneStateListener extends PhoneStateListener {

    /**
     * Callback invoked when network signal strengths changes.
     */
    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
      try {

        if (signalStrength.getGsmSignalStrength() != Integer.MAX_VALUE &&
            signalStrength.getGsmSignalStrength() != 99
            && signalStrength.getGsmSignalStrength() != 0) {
          Log.e("2G", "信号强度改变了");
          /**获取GSM信号强度*/setmGsmRssi(signalStrength.getGsmSignalStrength() + "");
          /**获取Gsm小区信息*/getGsmAllCellInfo();
        } else {
          /**获取GSM信号强度*/setmGsmRssi("-");
        }

        /**回调刷新UI*/
        if (mGsmCallBack != null && !getmGsmRssi().equals("-")) {
          Log.e("2G", "下一秒就要回调");
          mGsmCallBack.gsmToUpdateUI(mCallBackMark);
        }

      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    /**
     * Callback invoked when a observed cell info has changed,
     * or new cells have been added or removed.
     */
    @Override
    public void onCellInfoChanged(List<CellInfo> mCellInfoList) {

      if (mCellInfoList != null && mCellInfoList.size() > 0) {

        Log.e("2G", "小区改变了");

        /**获取基站数据*/getBaseStationData(mCellInfoList);

        /**回调刷新UI*/
        if (mGsmCallBack != null && !getmGsmRssi().equals("-")) {
          Log.e("2G", "小区改变了-下一秒就要回调");
          mGsmCallBack.gsmToUpdateUI(mCallBackMark);
        }

        if (!getmGsmCid().trim().equals("-")) {
               /*回调服务*/
          if (mCidChangeCallBack != null && Integer.valueOf(getmGsmCid().trim()) != mGsmCid) {
            mCidChangeCallBack.toUpdateCid(mServiceCallBackMark);
          }
          mGsmCid = Integer.valueOf(getmGsmCid().trim());
        }
      }
    }
  }

}
