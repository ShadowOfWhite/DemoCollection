package com.example.asus.calldemo;

import android.telephony.CellInfo;

import java.util.List;

/**
 * Created by 今夜犬吠 on 2017/6/6.
 * <p>
 * 基站信息基类
 */

public interface BaseBaseStationInfo {

  /**
   * 获取所有小区信息
   */
  abstract void getBaseStationData(List<CellInfo> mCellInfoList);

  /**
   * 开启监听器
   */
  abstract void openPhoneStateLis();

  /**
   * 关闭监听器
   */
  abstract void closePhoneStateLis();

  /**
   * 传递回调接口
   */
  abstract void setServiceCallBackInter(MCidChangeCallBack mCidChangeCallBack, int callBackMark);

  /**
   * 回调接口-用于Service-刷新基站名
   */
  public interface MCidChangeCallBack {
    void toUpdateCid(int mMark);
  }

}
