package com.example.asus.solibtestdemo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by zhuminfeng on 2017/10/13.
 */

public class DiagConfig {
    private static final String TAG="CellularTour";
    private static final String mTAG="DiagConfig";

    private static SharedPreferences sharedPrefs(Context context){
        return context.getSharedPreferences("yindatech.com.cellulartour_preferences", Context.MODE_PRIVATE);
    }

    // ========================================================================
    // 设备兼容性
    // ========================================================================
    // Device compatibility is set in:
    //   (a) ActiveTestService.java:257     setDeviceIncompatible         -- MAYBE incompatible
    //   (b) MsdService.java:1052           setDeviceCompatibleDetected   -- once compatible, always compatible!)

    // The following marks a device as permanently diag compatible. Meaning that
    // the app has received diag messages at one point. (Thus it should not be marked as
    // incompatible again)
    public static boolean getDeviceCompatibleDetected(Context context){
        return sharedPrefs(context).getBoolean("device_compatible_detected",false);
    }

    public static void setDeviceCompatibleDetected(Context context, boolean compatible){
        Editor editor=sharedPrefs(context).edit();
        editor.putBoolean("device_compatible_detected",compatible);
        editor.commit();
    }

    private static final String DEVICE_INCOMPATIBLE_DETECTED_FLAG="device_compatible_detected_2";

    public static boolean getDeviceIncompatible(Context context){
        return sharedPrefs(context).getBoolean(DEVICE_INCOMPATIBLE_DETECTED_FLAG,false);
    }

    public static void setDeviceIncompatible(Context context, boolean incompatible){
        Editor editor=sharedPrefs(context).edit();
        editor.putBoolean(DEVICE_INCOMPATIBLE_DETECTED_FLAG,incompatible);
        editor.commit();
    }

    // ========================================================================
    public static boolean getParserLogging(Context context){

        return sharedPrefs(context).getBoolean("setting_parser_logging",false);
    }

    public static String getAppId(Context context){
        return sharedPrefs(context).getString("settings_appId","");
    }

    public static boolean getCrash(Context context){
        return sharedPrefs(context).getBoolean("settings_crash",false);
    }

    public static void setCrash(Context context, boolean crash){
        Editor edit=sharedPrefs(context).edit();
        edit.putBoolean("settings_crash",crash);
        edit.commit();
    }
}
