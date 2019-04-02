package com.example.yanglin.indoordot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Looper;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

import static android.content.Context.SENSOR_SERVICE;

/**
 *
 */
public class AutoDotView extends View  implements SensorEventListener {
    private static final String TAG = "AutoDotView";

    private int viewWidth;
    private int viewHeight;



//    private Matrix matrix; // 根据旋转角度，生成旋转矩阵
    private int degrees;//指针旋转角度
    private int lastDegrees;
    private Bitmap pointer;//指针
    private Paint  paint;

    private int length = 40;

    private Path mPath = new Path();


    float accValues[]=new float[3]; //加速度传感器数据
    float magValues[]=new float[3]; //地磁传感器数据
    float r[]=new float[9]; //旋转矩阵，用来保存磁场和加速度的数据
    float oriValues[]=new float[3];//方向结果


    private SensorManager sensorManager;
    private Sensor acc_sensor;//加速度传感器
    private Sensor mag_sensor;//磁场传感器


    public AutoDotView(Context context) {

        super(context);
        Log.i(TAG, "AutoDotView: 2");
    }

    public AutoDotView(Context context,  AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "AutoDotView:1 ");
        pointer = BitmapFactory.decodeResource(getResources(),R.mipmap.pointer);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getResources().getColor(R.color.colorAccent));
//        matrix = new Matrix();
//        matrix.postRotate(0);

        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        acc_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //给传感器注册监听
        sensorManager.registerListener(this, acc_sensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, mag_sensor, SensorManager.SENSOR_DELAY_GAME);

    }

    public AutoDotView(Context context,  AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        Drawable drawable = AppCompatResources.getDrawable(context,R.mipmap.ic_launcher);

        Log.i(TAG, "AutoDotView: ");

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
        viewHeight = getHeight();
        viewWidth = getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {

//        int fff = (int) Math.toDegrees(oriValues[0]);


        canvas.rotate( -degrees, viewWidth/2, viewHeight / 2);
        mPath.reset();

        mPath.moveTo(viewWidth/2, viewHeight / 2-3*length);
        mPath.lineTo(viewWidth/2-length,viewHeight / 2);
        mPath.lineTo(viewWidth/2+length,viewHeight / 2);
        mPath.close();
        canvas.drawPath(mPath,paint);

        super.onDraw(canvas);
//        invalidate();

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            accValues=event.values;
        }
        else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
            magValues=event.values;
        }

        SensorManager.getRotationMatrix(r, null, accValues, magValues);
        SensorManager.getOrientation(r, oriValues);

//        Log.i(TAG, "onSensorChanged: oriValues[0]:"+oriValues[0]);
//        matrix.postRotate(0);
//        if (oriValues[0]>0){
////            matrix.postRotate(180 - oriValues[0]);
//            degrees = 180 - Math.toDegrees(oriValues[0]);
//        }else {
//            degrees = -180 - Math.toDegrees(oriValues[0]);
////            matrix.postRotate(-180 - oriValues[0]);
//        }
//        Bitmap temp = Bitmap.createBitmap(pointer, 0, 0, pointer.getWidth(), pointer.getHeight(), matrix, true);
//        pointer.recycle();
//        pointer = null;
//        pointer = temp;

        degrees = (int) Math.toDegrees(oriValues[0]);
        Log.i(TAG, "onDraw: degrees:"+degrees);
        if (Math.abs(degrees - lastDegrees) <4 ){
            return;
        }else {
            lastDegrees =degrees;
        }
        invalidate();


//        Log.i(TAG, "x:"+Math.toDegrees(oriValues[0])+" y:"+Math.toDegrees(oriValues[1])+" z:"+Math.toDegrees(oriValues[2]));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

        Log.e(TAG, "onAccuracyChanged: 精度改变："+accuracy );

    }


    /**
     * 获取当前时间
     */
    private void getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        float milliSecond = calendar.get(Calendar.MILLISECOND);
        float second = calendar.get(Calendar.SECOND) + milliSecond / 1000;
        float minute = calendar.get(Calendar.MINUTE) + second / 60;
        float hour = calendar.get(Calendar.HOUR) + minute / 60;
//        degrees = second / 60 * 360;
//        mMinuteDegree = minute /60 * 360;
//        mHourDegree = hour / 12 * 360;
    }
}
