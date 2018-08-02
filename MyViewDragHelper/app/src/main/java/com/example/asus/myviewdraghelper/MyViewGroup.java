package com.example.asus.myviewdraghelper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Rect;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 杨淋 on 2018/7/18.
 * Describe：自定义控件
 */


//时光倒流：保存每次移动的坐标，点击倒流，取出数据移动

/**
 * clickView.getX()+"  getLeft:"+clickView.getLeft()+" "+clickView.getTranslationX()具体值的意思
 */
public class MyViewGroup extends FrameLayout {
    private static final String TAG = "MyViewGroup";

    /*最小滑动被监听距离*/private int slop;
    /*枚举对象*/private Status status;
    /*被点击的View*/private View clickView;

    //被移动的总距离
    private int totalDistanceX;
    private int totalDistanceY;

    //初始坐标
    private float startPointX;
    private float startPointY;

    //上次触摸的坐标
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
                clickView = null;

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
//                    clickView.offsetTopAndBottom(0-(int)totalDistanceY);
//                    clickView.offsetLeftAndRight(0-(int)totalDistanceX);
//                    moveToOrigin(totalDistanceX,totalDistanceY);
                    moveToOriginAnimator();
//                    clickView = null;
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

    private void moveToOriginAnimator() {

        ValueAnimator animatorX = ValueAnimator.ofFloat(clickView.getX(),startPointX);
        animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int mWidth = clickView.getWidth();
                clickView.setLeft((int)((float)animation.getAnimatedValue()));
                clickView.setRight(clickView.getLeft()+mWidth);
            }
        });

        ValueAnimator animatorY = ValueAnimator.ofFloat(clickView.getY(),startPointY);
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int mHeight = clickView.getHeight();
                clickView.setTop((int)((float)animation.getAnimatedValue()));
                clickView.setBottom(clickView.getTop()+mHeight);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX).with(animatorY);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Log.e(TAG, "onTouchEvent: "+clickView.getX()+"  getLeft:"+clickView.getLeft()+" "+clickView.getTranslationX() );
                clickView = null;
            }
        });
        animatorSet.start();
    }

    /**
     *回弹 模拟动画的形式回到原点,斜边每次移动50像素
     * 结果：失败了，本来想每移动50像素就让线程沉睡1秒，结果没动画效果
     * @param totalDistanceX
     * @param totalDistanceY
     */
    private void moveToOrigin(int totalDistanceX, int totalDistanceY) {

        /*斜边长度*/double mHypotenuse = Math.sqrt(totalDistanceX*totalDistanceX+totalDistanceY*totalDistanceY);
        /*cos*/double mcos = totalDistanceX/mHypotenuse;
        /*sin*/double msin = totalDistanceY/mHypotenuse;

        /*X轴Y轴每次移动的距离*/
        double mdistanceX = mcos*50;
        double mdistanceY = msin*50;

        /*X轴Y轴总共移动的距离*/
        int mtotalmoveX = 0;
        int mtotalmoveY = 0;

        //每一秒移动50像素
        for (int i = 0; i<mHypotenuse/50-1;i++){


            clickView.offsetTopAndBottom(0-(int)mdistanceY);
            clickView.offsetLeftAndRight(0-(int)mdistanceX);

            mtotalmoveX = mtotalmoveX + (int)mdistanceX;
            mtotalmoveY = mtotalmoveY + (int)mdistanceY;
            Log.e(TAG, "moveToOrigin: "+","+mHypotenuse/50+",i:"+i);

        }
       /*最后小于50像素的部分*/
        clickView.offsetTopAndBottom(mtotalmoveY-totalDistanceY);
        clickView.offsetLeftAndRight(mtotalmoveX-totalDistanceX);



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
//        ViewCompat.offsetLeftAndRight(clickView,(int)distanceX);
//        ViewCompat.offsetTopAndBottom(clickView,(int)distanceY);
//        Log.e(TAG, "moveView: getX:"+clickView.getX()+"  getLeft:"+clickView.getLeft()+" "+clickView.getTranslationX());
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
                clickView = view;
                startPointX = clickView.getX();
                startPointY = clickView.getY();
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
