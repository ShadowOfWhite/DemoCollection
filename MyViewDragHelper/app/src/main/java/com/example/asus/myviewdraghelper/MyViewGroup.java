package com.example.asus.myviewdraghelper;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * Created by 杨淋 on 2018/7/18.
 * Describe：自定义控件
 */

public class MyViewGroup extends FrameLayout {
    private static final String TAG = "MyViewGroup";

    /*最小滑动被监听距离*/private int slop;
    /*枚举对象*/private Status status;
    /*被点击的View*/private View clickView;

    //被移动的总距离
    private float totalDistanceX;
    private float totalDistanceY;

    //被点击的坐标
    private float lastPointX;
    private float lastPointY;

    private Context context;


    public MyViewGroup(@NonNull Context context) {
       this(context,null);
    }

    public MyViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,null,0);
    }

    public MyViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
//        slop = ViewConfiguration.getWindowTouchSlop();
        Log.e(TAG, "MyViewGroup: "+slop+"   "+ViewConfiguration.get(context).getScaledTouchSlop() );
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                if (pointInView(event)){
                    lastPointX = event.getX();
                    lastPointY = event.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (clickView != null){
                    moveView(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (clickView != null){
                    for (int i = 0; i<Math.sqrt(totalDistanceX*totalDistanceX+totalDistanceY*totalDistanceY);i++){

                        clickView.offsetTopAndBottom(0-(int)totalDistanceY);
                        clickView.offsetLeftAndRight(0-(int)totalDistanceX);
                    }
                    clickView = null;
                    totalDistanceX = 0;
                    totalDistanceY = 0;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG, "onTouchEvent:ACTION_CANCEL: " );
            default:
                break;
        }
        return true;
    }


    /**
     * 随着手指移动View
     * @param event
     */
    private void moveView(MotionEvent event) {

        //移动距离
        float distanceX = event.getX()-lastPointX;
        float distanceY = event.getY()-lastPointY;

        totalDistanceX = totalDistanceX + (int)distanceX;
        totalDistanceY = totalDistanceY + (int)distanceY;

        lastPointY = event.getY();
        lastPointX = event.getX();

        clickView.offsetLeftAndRight((int)distanceX);
        clickView.offsetTopAndBottom((int)distanceY);
//        Log.e(TAG, "moveView: View开始移动，触摸点坐标："+event.getX()+","+event.getY() );
    }

    /**
     * 判断触点是否在View上
     * @param event
     * @return
     */
    private boolean pointInView(MotionEvent event) {

        int count = getChildCount();
        Rect rect = new Rect();
        for (int i = count-1; i>=0; i--){
            View view = getChildAt(i);

            rect.set(view.getLeft(),view.getTop(),view.getRight(),view.getBottom());

//            view.getLocalVisibleRect(rect);
            if (rect.contains((int)event.getX(),(int)event.getY())){
                Log.e(TAG, "pointInView "+i+" : 左上角坐标（"+view.getLeft()+","+view.getTop()+")" );
                clickView = view;
                return true;
            }
        }

        return false;
    }

    /**
     * 控件状态枚举
     */
    private enum Status{
        DRAG,IDLE
    }
}
