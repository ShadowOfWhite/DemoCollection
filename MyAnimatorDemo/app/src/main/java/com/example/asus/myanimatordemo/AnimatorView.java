package com.example.asus.myanimatordemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by 杨淋 on 2018/8/2.
 * Describe：
 */

public class AnimatorView extends View {

    /*半径*/private float radius = 50;

    //当前图形x,y坐标
    private float currentX = radius;
    private float currentY = radius;

    /*起始坐标对象*/Point startPoint;
    /*终点坐标对象*/Point endPoint;
    /*画笔*/Paint mPaint;
    private static final String TAG = "AnimatorView";
    boolean tag = true;

    public AnimatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initObject();
    }

    /**
     * 初始化对象
     */
    private void initObject() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);//设置抗锯齿

        startPoint = new Point(radius,radius);
        currentX = radius;
        currentY = radius;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (tag){
            startAnimator();
            tag = false;
        }
        canvas.drawCircle(currentX,currentY,radius,mPaint);

    }


    private void startAnimator() {
        endPoint = new Point(getWidth()-radius,getHeight()-radius);
        Log.d(TAG, "startAnimator: "+getWidth()+","+getHeight());
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new MyTypeEvaluator(),startPoint,endPoint);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point animatedPoint = (Point)animation.getAnimatedValue();
                currentX = animatedPoint.getX();
                currentY = animatedPoint.getY();
                Log.d(TAG, "onAnimationUpdate: "+currentX+","+currentY);
                invalidate();

            }
        });
        valueAnimator.setDuration(5000);
        valueAnimator.start();
    }
}
