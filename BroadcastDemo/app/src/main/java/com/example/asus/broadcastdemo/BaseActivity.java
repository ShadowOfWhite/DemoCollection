package com.example.asus.broadcastdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BaseActivity extends AppCompatActivity {

    MyBroadcastReceiver myBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.asus.broadcastDemo.offlone");
        myBroadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(myBroadcastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myBroadcastReceiver != null){
            unregisterReceiver(myBroadcastReceiver);
        }
        myBroadcastReceiver = null;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(final Context context, Intent intent) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(BaseActivity.this);
            dialog.setCancelable(false);
            dialog.setIcon(R.mipmap.icon_logo);
            dialog.setTitle("警告");
            dialog.setMessage("强制下线演示");
            dialog.setPositiveButton("登陆", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCollector.removeAllActivity();
                    Intent intent1 = new Intent(context,LoginActivity.class);
                    startActivity(intent1);
//                    dialog.dismiss();
                }
            }).show();
        }
    }
}
