package com.example.yanglin.networkmonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * 作者：贪欢
 * 时间：2019/5/16
 * 描述：网络变化广播接收器
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    private static final String TAG = "NetWorkStateReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: 网络变化");
        //检测API是不是小于23，因为到了API23之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            Log.i("网络信息", "onReceive: "+dataNetworkInfo.getTypeName() + " connect is " + dataNetworkInfo.isConnected());

            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已连接,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已连接,移动数据已断开", Toast.LENGTH_SHORT).show();
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI已断开,移动数据已连接", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "WIFI已断开,移动数据已断开", Toast.LENGTH_SHORT).show();
            }
//API大于23时使用下面的方式进行网络监听
        }else {

//
//            //获得ConnectivityManager对象
//            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//            //获取所有网络连接的信息
//            Network[] networks = connMgr.getAllNetworks();
//            //用于存放网络连接信息
//            StringBuilder sb = new StringBuilder();
//            //通过循环将网络信息逐个取出来
//            for (int i=0; i < networks.length; i++){
//                //获取ConnectivityManager对象对应的NetworkInfo对象
//                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
//                sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
//                Log.i("网络信息", "onReceive: "+networkInfo.getType() + " connect is " + networkInfo.isConnected());
//            }
//            Toast.makeText(context, sb.toString(),Toast.LENGTH_SHORT).show();
        }

    }

    private void analyzeNetwork(int networkType) {
        switch (networkType) {
        /*
         GPRS : 2G(2.5) General Packet Radia Service 114kbps
         EDGE : 2G(2.75G) Enhanced Data Rate for GSM Evolution 384kbps
         UMTS : 3G WCDMA 联通3G Universal Mobile Telecommunication System 完整的3G移动通信技术标准
         CDMA : 2G 电信 Code Division Multiple Access 码分多址
         EVDO_0 : 3G (EVDO 全程 CDMA2000 1xEV-DO) Evolution - Data Only (Data Optimized) 153.6kps - 2.4mbps 属于3G
         EVDO_A : 3G 1.8mbps - 3.1mbps 属于3G过渡，3.5G
         1xRTT : 2G CDMA2000 1xRTT (RTT - 无线电传输技术) 144kbps 2G的过渡,
         HSDPA : 3.5G 高速下行分组接入 3.5G WCDMA High Speed Downlink Packet Access 14.4mbps
         HSUPA : 3.5G High Speed Uplink Packet Access 高速上行链路分组接入 1.4 - 5.8 mbps
         HSPA : 3G (分HSDPA,HSUPA) High Speed Packet Access
         IDEN : 2G Integrated Dispatch Enhanced Networks 集成数字增强型网络 （属于2G，来自维基百科）
         EVDO_B : 3G EV-DO Rev.B 14.7Mbps 下行 3.5G
         LTE : 4G Long Term Evolution FDD-LTE 和 TDD-LTE , 3G过渡，升级版 LTE Advanced 才是4G
         EHRPD : 3G CDMA2000向LTE 4G的中间产物 Evolved High Rate Packet Data HRPD的升级
         HSPAP : 3G HSPAP 比 HSDPA 快些
         */
            // 2G网络
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                Log.i(TAG, "analyzeNetwork: 2G");
                break;
            // 3G网络
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                Log.i(TAG, "analyzeNetwork: 3G");
                break;
            // 4G网络
            case TelephonyManager.NETWORK_TYPE_LTE:
                Log.i(TAG, "analyzeNetwork: 4G");
                break;
            default:

                Log.i(TAG, "analyzeNetwork: 未知");
                break;
        }
    }
}
