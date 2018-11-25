package com.example.asus.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

/**
 * Created by 杨淋 on 2018/8/20.
 * Describe：
 */

public class MyReportSender implements ReportSender {

    private Context context = null;

    public MyReportSender(Context context){

        this.context = context;
    }
    @Override
    public void send(@NonNull Context context, @NonNull CrashReportData errorContent) throws ReportSenderException {
        Log.i("MyReportSender", "发送日志");
        sendMailReport(errorContent);
    }

    private void sendMailReport(CrashReportData errorContent) {

    }
}

