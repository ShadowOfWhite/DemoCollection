package com.example.yanglin.indoordot;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class MUtils {
    /**
     * 获取屏幕宽度
     * @return
     */
    public static int getScreenWidth(Context context){
        final Resources resources = context.getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     * @return
     */
    public static int getScreenHeight(Context context){
        final Resources resources = context.getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
