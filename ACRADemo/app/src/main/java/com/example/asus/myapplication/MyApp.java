package com.example.asus.myapplication;

import android.app.Application;
import android.content.Context;

import org.acra.ACRA;
import org.acra.ErrorReporter;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.config.ACRAConfiguration;
import org.acra.config.ACRAConfigurationException;
import org.acra.config.ConfigurationBuilder;

/**
 * Created by 杨淋 on 2018/8/20.
 * Describe：
 */

//发送到指定邮箱。注意：该操作触发时会调起用户客户端邮箱需要用户主动发送。不建议使用。
@ReportsCrashes(mailTo = "837026436@qq.com",
        customReportContent = {ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA,
                ReportField.STACK_TRACE, ReportField.LOGCAT},//发送的字段
        mode = ReportingInteractionMode.TOAST,//异常时弹出信息的类型
        resToastText = R.string.crash_toast_text)//弹出的文字
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        MyReportSenderFactory myReportSenderFactory = new MyReportSenderFactory();
        try {
            final ACRAConfiguration config = new ConfigurationBuilder(this)
                    .setReportSenderFactoryClasses(myReportSenderFactory.getClass())
                    .build();
        } catch (ACRAConfigurationException e) {
            e.printStackTrace();
        }



    }
}
