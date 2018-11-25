package com.example.asus.calldemo;

/**
 * Created by 今夜犬吠 on 2017/8/20.
 * 性能测试-结果集-总控制
 */

public abstract class BasePerforResult {
  /*调度类型*/private String mPerforType;

  /*测试开始时间*/private  long mStartTime;

  /*测试结束时间*/private  long mEndTime;

  /*测试持续时间*/private  long mDuration;


  /**获取测试结果*/
  public abstract String getResult();

  public long getmStartTime() {
    return mStartTime;
  }

  public void setmStartTime(long mStartTime) {
    this.mStartTime = mStartTime;
  }

  public long getmEndTime() {
    return mEndTime;
  }

  public void setmEndTime(long mEndTime) {
    this.mEndTime = mEndTime;
  }

  public long getmDuration() {
    return mDuration;
  }

  public void setmDuration(long mDuration) {
    this.mDuration = mDuration;
  }

  public String getmPerforType() {
    return mPerforType;
  }

  public void setmPerforType(String mPerforType) {
    this.mPerforType = mPerforType;
  }
}
