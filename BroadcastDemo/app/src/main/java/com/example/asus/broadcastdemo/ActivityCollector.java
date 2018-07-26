package com.example.asus.broadcastdemo;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨淋 on 2018/7/10.
 * Describe：
 */

public class ActivityCollector {

    public static List<Activity> list = new ArrayList<>();
    public static void addActivity(Activity activity){
        list.add(activity);
    }

    public static boolean removeActivity(Activity activity){
        if (list != null){
            return list.remove(activity);
        }
        return false;
    }

    public static boolean removeAllActivity(){
        if (list != null){
            for (Activity activity:list){
                activity.finish();
            }
        }
        return false;
    }
}
