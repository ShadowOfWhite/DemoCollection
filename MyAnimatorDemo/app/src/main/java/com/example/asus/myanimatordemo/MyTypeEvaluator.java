package com.example.asus.myanimatordemo;

import android.animation.TypeEvaluator;
import android.util.Log;

/**
 * Created by 杨淋 on 2018/8/2.
 * Describe：
 */

public class MyTypeEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {

        float diffX = ((Point)endValue).getX() - ((Point)startValue).getX();
        float diffY = ((Point)endValue).getY() - ((Point)startValue).getY();

        float x = ((Point) startValue).getX() + fraction * diffX;
        float y = ((Point) startValue).getY() + fraction * diffY;
        Log.d("MyTypeEvaluator", x+","+y+","+fraction);
        return new Point(x,y);
    }
}
