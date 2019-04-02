package com.example.asus.solibtestdemo.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;

import com.example.asus.solibtestdemo.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



/**
 * Created by zhuminfeng on 2017/10/14.
 */

public class Utils {
    private static final String TAG="CellularTour";
    private static final String mTAG="Utils";

    public static Integer getDiagDeviceNodeMajor(){
        BufferedReader r=null;
        try {
            r=new BufferedReader(new InputStreamReader(new FileInputStream("/proc/devices")));
            while (true){
                String line=r.readLine();
                if (line==null){
                    return null;
                }
                line=line.trim();
                String[] line_elements=line.split("\\s+");
                if (line_elements.length!=2){
                    continue;
                }
                if (line_elements[1].trim().toLowerCase(Locale.US).equals("dia")){
                    return Integer.parseInt(line_elements[0].trim());
                }
            }
        }catch (IOException e){
            return null;
        }finally {
            try{
                if (r!=null){
                    r.close();
                }
            }catch (IOException ee){
                Log.e(TAG,mTAG+": IOException in getDiagDeviceNodeMajor():\n"+ee.toString());
            }
        }
    }

    public static String createDiagDevice(){
        File diagDevice=new File("/dev/diag");
        if (diagDevice.exists())
            return null;
        Integer diagDeviceMajor=Utils.getDiagDeviceNodeMajor();
        if (diagDeviceMajor==null){
            return "Diag device does not exist and /proc/devices does not contain any entry for 'dia'";
        }
        String mknodCmd="mknod /dev/diag c "+diagDeviceMajor+" 0 || busybox mknod /dev/diag c "+diagDeviceMajor+" 0";
        String suBinary=DeviceCompatibilityChecker.getSuBinary();
        String cmd[]={suBinary,"-c",mknodCmd};
        Process mknod;
        try {
            mknod= Runtime.getRuntime().exec(cmd);
        }catch (IOException e){
            Log.e(TAG,mTAG+": IOException in createDiagDevice():\n"+e.toString());
            return e.getMessage();
        }
        try {
            mknod.waitFor();
        }catch (InterruptedException ee){
            Log.e(TAG,mTAG+": InterruptedException in createDiagDevice():\n"+ee.toString());
        }
        if (!diagDevice.exists()){
            return "Failed to create diag device: "+mknodCmd;
        }
        return null;
    }

    public static void createFolder(String filePath){
        File folder=new File(filePath);
        if (!folder.exists())
            folder.mkdirs();
    }

    public static String checkDiag(){
        Process checkdiag;
        String result;

        String checkCmd="/system/bin/ls -alz dev/diag";
        String suBinary=DeviceCompatibilityChecker.getSuBinary();
        String cmd[]={suBinary,"-c",checkCmd};

        try {
            checkdiag= Runtime.getRuntime().exec(cmd);
            BufferedReader bis=new BufferedReader(new InputStreamReader(checkdiag.getInputStream()));
            result=bis.readLine();
            checkdiag.destroy();
        }catch (Exception e){
            Log.e(TAG,mTAG+":checkDiag() exception:"+e);
            return "Error:no diag info";
        }
        Log.i(TAG,"DIAG:"+"\""+result+"\"");
        return result;
    }

    public static boolean isDeviceMSM(){
        String msms;
        try {
            msms=DiagLog.osgetprop("ro.baseband")+
                    DiagLog.osgetprop("ro.board.platform")+
                    DiagLog.osgetprop("ro.boot.baseband");
        }catch (Exception e){
            Log.e(TAG,mTAG+"Exception in isDeviceMSM(): "+e);
            return true;
        }
        msms=msms.toLowerCase(Locale.US);
        CharSequence cs="msm";
        return msms.contains(cs);
    }

    public static String formatTimestamp(long millis){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date(millis);
        return dateFormat.format(date);
    }

}
