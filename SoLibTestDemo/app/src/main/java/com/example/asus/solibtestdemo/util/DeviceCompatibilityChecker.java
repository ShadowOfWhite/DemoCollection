package com.example.asus.solibtestdemo.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 * Created by zhuminfeng on 2017/10/13.
 */

public class DeviceCompatibilityChecker {
    private static String TAG="CellularTour";
    private static String mTAG="DeviceCompatibilityChecker";

    private static String su_binary=null;
    private static int suFailReason=0;
    private static final int SU_ROOT_DENIED=1;
    private static final int SU_NOT_PRESENT=2;
    private static final int SU_NOT_WORKING=3;

    public static String checkDeviceCompatibility(Context context){
        boolean markedCompatible=DiagConfig.getDeviceCompatibleDetected(context);
        boolean markedIncompatible=DiagConfig.getDeviceIncompatible(context);

        if (!markedCompatible && markedIncompatible){
            return "Error!Your device has been marked imcompatible.";
        }else if (markedCompatible && markedIncompatible){
            DiagConfig.setDeviceCompatibleDetected(context,true);
            return "Error : No baseband messages, please reboot device.";
        }

        String suBinary;
        File diagDevice=new File("/dev/diag");

        if (!diagDevice.exists() && Utils.getDiagDeviceNodeMajor()==null){
            return "Device /dev/diag does not exist,no Qualcomm chipset or ...";
        }

        suBinary=getSuBinary();
        if (suBinary==null){
            switch (suFailReason){
                case SU_ROOT_DENIED: return "Root access denied by su.Check permissions.";
                case SU_NOT_WORKING: return "Your device is not properly rooted(su binary does not work)";
                case SU_NOT_PRESENT: return "Your device is not rooted(no su binary found)";
                default: return "root access failed.";
            }
        }

        String createDiagError=Utils.createDiagDevice();
        if (createDiagError != null) {
            return "Failed to create diag device: "+createDiagError;
        }

        if (!testRunOK(context,suBinary)){
            return "Could not initialize DIAG interface";
        }

        return null;
    }

    public static boolean testRunOK(Context context, String suBinary){
        Process helper;
        String libdir=context.getApplicationInfo().nativeLibraryDir;
        String diag_helper=libdir+"/libdiag_helper.so";
        String cmd[]={suBinary,"-c","exec "+diag_helper+" test"};

        try {
            helper= Runtime.getRuntime().exec(cmd);
        }catch (IOException e){
            Log.e(TAG,mTAG+": testRunOK(): IOException:\n"+e.toString());
            return false;
        }

        boolean teminated=false;
        do {
            try{
                helper.waitFor();
                teminated=true;
            }catch (InterruptedException ee){

            }
        }while (!teminated);
        return helper.exitValue()==0;
    }

    public static String getSuBinary(){
        if (su_binary==null){
            su_binary=findSuBinary();
        }
        return su_binary;
    }

    public static String findSuBinary(){
        String path= System.getenv("PATH");
        HashSet<String> pathDirs=new HashSet<>();

        pathDirs.add("/system/bin");
        pathDirs.add("/system/xbin");
        for (String pathEntry:path.split(":")){
            pathDirs.add(pathEntry);
        }

        int suBinaryTried=0;

        for (String pathDir:pathDirs){
            File f=new File(pathDir+"/su");
            if (!f.exists())
                continue;
            suBinaryTried++;
            String cmd[]={pathDir+"/su","-c","id"};
            Process p;
            try{
                p= Runtime.getRuntime().exec(cmd,null,null);
                BufferedReader su_stdout=new BufferedReader(new InputStreamReader(p.getInputStream()));
                String id_line=su_stdout.readLine();
                su_stdout.close();
                p.waitFor();
                if (id_line==null){
                    suFailReason=SU_ROOT_DENIED;
                    return null;
                }

                if (id_line.startsWith("uid=0")){
                    return pathDir+"/su";
                }
            }catch (InterruptedException | IOException e){
                Log.e(TAG,mTAG+": Exception in findBinary():\n"+e);
            }
        }
        if (suBinaryTried>0){
            suFailReason=SU_NOT_WORKING;
            return null;
        }else {
            suFailReason=SU_NOT_PRESENT;
            return null;
        }
    }
}
