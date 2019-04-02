package com.example.asus.mapsnake.system;

import android.app.Application;

/**
 * Created by 杨淋 on 2018/5/7.
 * Describe：
 */

public class BaseApplication extends Application {

    //初始化操作，app打开时会先执行这个方法采取执行活动服务的onCreate()
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
