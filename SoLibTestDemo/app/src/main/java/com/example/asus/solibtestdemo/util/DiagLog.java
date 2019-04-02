package com.example.asus.solibtestdemo.util;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;



/**
 * Created by zhuminfeng on 2017/10/10.
 */

public class DiagLog {
    private static final String TAG="CellularTour";
    private static final String mTAG="DiagLog";

//    private static DiagService diagService;
//    private static DiagServiceHelper diagServiceHelper;

    public static String getTime(){
        Calendar c= Calendar.getInstance();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ZZZZZ", Locale.CHINA);
        return format.format(c.getTime());
    }

    private static String getTimePrefix(){
        return getTime()+" ";
    }




    private static boolean isBlank(String string){
        return string==null||string.trim().length()==0;
    }

    public static String osgetprop(String key){
        Process process;
        String property;
        try {
            process= Runtime.getRuntime().exec("/system/bin/getprop"+" "+key);
            BufferedReader bis=new BufferedReader(new InputStreamReader(process.getInputStream()));
            property=bis.readLine();
            process.destroy();
            if (isBlank(property)) {
                return "<n/a>";
            }
        }catch (IOException | NullPointerException ee){
            Log.e(TAG,mTAG+":osgetprop(): Error executing getprop:"+ee.toString());
            return "error";
        }
        return property;
    }

    public static String getDeviceProps(){
        String prop;
        try{
            prop=   "AOS version:                 "+ Build.VERSION.RELEASE+"\n"
                    +"Kernel version:             "+ System.getProperty("os.version")+"\n"
                    +"Manufacturer:               "+ Build.MANUFACTURER+"\n"
                    +"Brand:                      "+ Build.BRAND+"\n"
                    +"Product (Model):            "+ Build.PRODUCT+" ("+ Build.MODEL+")"+"\n"
                    +"gsm_version_baseband:       "+osgetprop("gsm.version.baseband")+"\n"
                    +"gsm_version_ril-impl:       "+osgetprop("gsm.version.ril-impl")+"\n"
                    + "ro_confg_hw_chipidversion: " + osgetprop("ro.confg.hw_chipidversion") + "\n" // Huawei?
                    + "ril_hw_ver:                " + osgetprop("ril.hw_ver") + "\n"                // Samsung?
                    + "ril_modem_board:           " + osgetprop("ril.modem.board") + "\n"           // Samsung?
                    + "ro_arch:                   " + osgetprop("ro.arch") + "\n"                   // Samsung?
                    + "ro_product_cpu_abi:        " + osgetprop("ro.product.cpu.abi") + "\n"
                    + "ro_dual_sim_phone:         " + osgetprop("ro.dual.sim.phone") + "\n"
                    + "ro_board_platform:         " + osgetprop("ro.board.platform") + "\n";
        }catch (Exception e){
            Log.e(TAG, mTAG + "Exception in getDeviceProps(): Unable to retrieve system properties: " + e);
            return "";
        }
        return prop;
    }


}
