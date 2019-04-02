package com.example.asus.solibtestdemo.util;

import android.app.Activity;
import android.os.Build;

import java.lang.ref.WeakReference;

/**
 * Created by zhuminfeng on 2017/11/6.
 * Activity管理器
 */

public class MyActyManager {
    private static MyActyManager sInstance = new MyActyManager();
    private WeakReference<Activity> sCurrentActivityWeakRef;


    private MyActyManager() {

    }

    public static MyActyManager getInstance() {
        return sInstance;
    }

    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (currentActivity == null || currentActivity.isDestroyed()) {
                    currentActivity = null;
                }
            }
        }
        return currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }
}
