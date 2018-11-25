package com.example.asus.myanimatordemo;

/**
 * Created by 杨淋 on 2018/8/2.
 * Describe：坐标对象，保存X，Y坐标
 */

public class Point {

    float x;
    float y;

    Point(float x,float y){
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
