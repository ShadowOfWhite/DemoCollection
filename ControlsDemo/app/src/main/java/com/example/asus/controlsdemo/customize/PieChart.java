package com.example.asus.controlsdemo.customize;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.asus.controlsdemo.bean.Pie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨淋 on 2018/7/4.
 * Describe：自定义控件-圆饼图
 */

public class PieChart extends View {

    /*圆饼起始角度*/private float startAngle;
    /*画笔*/private Paint mPaint;
    /*圆饼数据集合*/private List<Pie> mPieList;
    /*视图的宽*/private float mWidth;
    /*视图的高*/private float mHeight;
    /*颜色的集合*/private int[] mcolor = {0X88FF0000,0X8800FF00,0X880000FF};

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initObj();
        /*设置抗锯齿*/mPaint.setAntiAlias(true);
        /*设置画笔填充*/mPaint.setStyle(Paint.Style.FILL);
        initData();
     }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*圆饼半径*/ float r = (float)((Math.min(mHeight,mWidth))*0.5*0.8);
        RectF rectF = new RectF(-r,-r,r,r);
        canvas.translate(mWidth/2,mHeight/2);
        for (int i = 0;i<mcolor.length;i++){
            mPaint.setColor(mcolor[i]);
            canvas.drawArc(rectF,startAngle,mPieList.get(i).getAngle(),true,mPaint);
            startAngle = startAngle + mPieList.get(i).getAngle();
        }
        mPaint.setColor(0X88006600);
        canvas.drawRect(rectF,mPaint);

    }


    /**
     * 初始化实体类
     */
    private void initObj() {
        startAngle = 0;
        mPaint = new Paint();
        mPieList = new ArrayList<>();
    }

    /**
     * 初始化变量
     */
    private void initData() {
    for (int i = 0; i<3; i++){
        Pie pie = new Pie();
        pie.setAngle(60+i*60);
        pie.setmCorlor(mcolor[i]);
        mPieList.add(pie);
    }

    }
}
