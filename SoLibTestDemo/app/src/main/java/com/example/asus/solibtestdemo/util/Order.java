package com.example.asus.solibtestdemo.util;

import java.util.List;

/**
 * Created by 杨淋 on 2019/1/9.
 * Describe：
 */

public class Order {

    private int mLockType;
    private List<String> mLockData;

    public int getmLockType() {
        return mLockType;
    }

    public void setmLockType(int mLockType) {
        this.mLockType = mLockType;
    }

    public List<String> getmLockData() {
        return mLockData;
    }

    public void setmLockData(List<String> mLockData) {
        this.mLockData = mLockData;
    }
}
