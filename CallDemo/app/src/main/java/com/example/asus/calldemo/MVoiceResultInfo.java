package com.example.asus.calldemo;


/**
 * Created by 今夜犬吠 on 2017/8/29.
 * 语音测试-结果集
 */

public class MVoiceResultInfo extends BasePerforResult {

  /*测试号码*/private String mVoiceTestNumber;

  /*通话时长*/private int mCallTime = -1;

  /*通话类型-来电-去电*/private String mCallType;

  /*通话状态--空闲-响铃-接通-通话中-挂断*/private StringBuffer mCallStatus;

  /*异常状态*/private String mBug;


  public String getmCallType() {
    return mCallType;
  }

  public void setmCallType(String mCallType) {
    this.mCallType = mCallType;
  }

  public MVoiceResultInfo() {
    mCallStatus = new StringBuffer();
  }

  public String getmVoiceTestNumber() {
    return mVoiceTestNumber;
  }

  public void setmVoiceTestNumber(String mVoiceTestNumber) {
    this.mVoiceTestNumber = mVoiceTestNumber;
  }

  public int getmCallTime() {
    return mCallTime;
  }

  public void setmCallTime(int mCallTime) {
    this.mCallTime = mCallTime;
  }

  public StringBuffer getmCallStatus() {
    return mCallStatus;
  }

  public void setmCallStatus(StringBuffer mCallStatus) {
    this.mCallStatus = mCallStatus;
  }

  public String getmBug() {
    return mBug;
  }

  public void setmBug(String mBug) {
    this.mBug = mBug;
  }

  /**
   * 获取测试结果
   */
  @Override
  public String getResult() {
    if (mBug != null && !mBug.equals("")) {
      return "测试类型:语音测试\n" +
          "测试失败:出现异常\n" +
          "错误日志:" + mBug+"\n";
    }
    if (mCallTime == -1) {
      return "测试类型:语音测试\n" +
          "通话类型:" + mCallType + "\n" +
          "通话状态:\n" +
          mCallStatus.toString() +
          "持续时长:" + (getmEndTime() - getmStartTime()) / 1000 + "s\n";
    }
    return "测试类型:语音测试\n" +
        "通话类型:" + mCallType + "\n" +
        "通话状态:\n" +
        mCallStatus.toString() +
        "通话时长:" + mCallTime + "s\n";
  }
}
