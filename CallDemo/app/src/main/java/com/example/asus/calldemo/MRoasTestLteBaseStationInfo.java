package com.example.asus.calldemo;

import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.util.List;


/**
 * Created by ChenHong on 2017/6/6.
 * <p>
 * Lte路测模块-基站信息控制工具
 */

public class MRoasTestLteBaseStationInfo extends MLteBaseStationInfoAbst {
  /*当前Lte的CID*/private int mLteCid = -1;

  /**
   * 构造
   */
  public MRoasTestLteBaseStationInfo(Context mContext) {

    /**实例化手机管理者*/setmTelephonyManager((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE));
  }


  /**
   * 获取所有小区信息
   * 提取Lte小区信息
   */
  public void getLteAllCellInfo() {
    /**获取所有小区信息*/List<CellInfo> mCellInfoList = getmTelephonyManager().getAllCellInfo();
    if (mCellInfoList != null && mCellInfoList.size() > 0) {
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
      if (mCellInfo instanceof CellInfoLte) {
        CellInfoLte mCellInfoLte = (CellInfoLte) mCellInfo;
        /**当前手机所在小区*/
        if (mCellInfoLte.isRegistered()) {
          if (mCellInfoLte.getCellIdentity().getCi() != Integer.MAX_VALUE) {
            /**获取Lte-ci*/setmLteCI(mCellInfoLte.getCellIdentity().getCi() + "");
          }else{
            setmLteCI("-");
          }

          if (mCellInfoLte.getCellIdentity().getPci() != Integer.MAX_VALUE) {
            /**获取lte-pci*/setmLtePCI(mCellInfoLte.getCellIdentity().getPci() + "");
          }
          if (mCellInfoLte.getCellSignalStrength().getDbm() != Integer.MAX_VALUE) {
            ///**获取lte-rsrp*/setmLteRSRP(mCellInfoLte.getCellSignalStrength().getDbm() + "");
            //MLogUtils.e("==正常接口获取LTE_RSRP:",getmLteRSRP()+"");
          }

          ///**计算asu*/setmLteAsu(mCellInfoLte.getCellSignalStrength().getDbm() + 140 + "");
          /**获取lte-tac*/setmLteTAC(mCellInfoLte.getCellIdentity().getTac() + "");
          /**计算ENodBeId*/setmLteENodBeId((int) (mCellInfoLte.getCellIdentity().getCi() / 256));
          /**计算CellId*/setmLteCellId(mCellInfoLte.getCellIdentity().getCi() - getmLteENodBeId() * 256 + "");
          /**获取中心频点*/if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setmLteEarfcn(mCellInfoLte.getCellIdentity().getEarfcn());
          }
        }
      }
    }
  }

  /**
   * 开启监听器
   */
  @Override
  public void openPhoneStateLis() {
    /**实例化监听*/setmLteLins(new LTEPhoneStateListener());
    /**开启监听*/getmTelephonyManager().listen(getmLteLins(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_CELL_INFO | PhoneStateListener.LISTEN_CELL_LOCATION);

  }

  /**
   * 关闭监听器
   */
  @Override
  public void closePhoneStateLis() {
    if (getmLteLins() != null) {
      getmTelephonyManager().listen(getmLteLins(), PhoneStateListener.LISTEN_NONE);
      mLteCallBack = null;
      setmLteLins(null);
    }
  }

  /**
   * 传递回调接口
   */
  @Override
  public void setCallBackInter(MLteCallBack mLteCallBack, int callBackMark) {
    this.mLteCallBack = mLteCallBack;
    this.mCallBackMark = callBackMark;
  }

  /**
   * 传递回调接口-服务
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
  public class LTEPhoneStateListener extends PhoneStateListener {

    /**
     * Callback invoked when network signal strengths changes.
     */
    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
      try {

        Method mGetLteRsrpMed = signalStrength.getClass().getDeclaredMethod("getLteRsrp");

        mGetLteRsrpMed.setAccessible(true);

        if ((int) mGetLteRsrpMed.invoke(signalStrength) != Integer.MAX_VALUE) {
          /**得到Lte-RSRP*/setmLteRSRP((int) mGetLteRsrpMed.invoke(signalStrength) + "");
          // MLogUtils.e("==反射获取LTE_RSRP:",getmLteRSRP()+"");
          /**计算asu*/setmLteAsu((int) mGetLteRsrpMed.invoke(signalStrength) + 140 + "");
        } else {
          /**得到Lte-RSRP*/setmLteRSRP("-");
        }

        Method mGetLteRsrqMed = signalStrength.getClass().getDeclaredMethod("getLteRsrq");

        mGetLteRsrqMed.setAccessible(true);


        if ((int) mGetLteRsrqMed.invoke(signalStrength) != Integer.MAX_VALUE) {
          /**得到Lte-RSRQ*/setmLteRSRQ((int) mGetLteRsrqMed.invoke(signalStrength) + "");
        }

        Method mGetLteRssnrMed = signalStrength.getClass().getDeclaredMethod("getLteRssnr");

        mGetLteRssnrMed.setAccessible(true);

        /**得到Lte-SINR*/setmLteSINR((int) mGetLteRssnrMed.invoke(signalStrength));

        /**获取所有基站*/getLteAllCellInfo();

//        /**回调刷新UI*/
//        if (mLteCallBack != null && !getmLteRSRP().equals("-")) {
//          mLteCallBack.toUpdateUI(mCallBackMark);
//        }

        /**回调刷新UI*/
        if (mLteCallBack != null ) {
          mLteCallBack.toUpdateUI(mCallBackMark);
        }
        /*回调服务*/
        if (mCidChangeCallBack != null && getmLteENodBeId() != mLteCid) {
          mCidChangeCallBack.toUpdateCid(mServiceCallBackMark);
          /*把当前的Cid保存起来-便于下一次对比*/
          mLteCid = getmLteENodBeId();
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

        /**获取基站数据*/getBaseStationData(mCellInfoList);

        /**回调Activity刷新UI*/
//        if (mLteCallBack != null && !getmLteRSRP().equals("-")) {
//          mLteCallBack.toUpdateUI(mCallBackMark);
//        }
        /**回调刷新UI*/
        if (mLteCallBack != null ) {
          mLteCallBack.toUpdateUI(mCallBackMark);
        }
        /*回调服务*/
        if (mCidChangeCallBack != null && getmLteENodBeId() != mLteCid) {
          mCidChangeCallBack.toUpdateCid(mServiceCallBackMark);
          /*把当前的Cid保存起来-便于下一次对比*/
          mLteCid = getmLteENodBeId();
        }
      }
    }
  }


}
