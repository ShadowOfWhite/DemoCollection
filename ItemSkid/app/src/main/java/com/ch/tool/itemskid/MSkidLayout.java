package com.ch.tool.itemskid;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import static android.content.ContentValues.TAG;

/**
 * Created by 今夜犬吠 on 2018/7/9.
 * 侧滑Item
 */

public class MSkidLayout extends FrameLayout {

  /*背景View*/private ViewGroup mBackViewG;
  /*前景View*/private ViewGroup mFrontViewG;

  /*控件宽*/private float mViewWidth;
  /*背景宽*/private float mBackWidth;

  /*关闭时-抽屉位置*/private Point mClosePoint;
  /*打开时-抽屉位置*/private Point mOpenPoint;
  /*ViewGroup滑动辅助工具*/private ViewDragHelper mViewDragHelper;

  /*滑动状态-传递接口*/private MSkidStatusCallBack mSkidStatusCallBack;

  /*滑过的距离*/private int mSlidingDistance;

  /*是否滑动*/private boolean mIsSlid=true;

  /*滑动状态*/
  private enum SkidStatus {
    Open//打开
    , Close//关闭
    , Opening;//正在打开
  }

  /*滑动状态-默认*/private SkidStatus mDefaultStatus = SkidStatus.Close;
  private static final String TAG = "MSkidLayout";

  public MSkidLayout(@NonNull Context context) {
    super(context);
    toolInitObj();
  }

  public MSkidLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    toolInitObj();
  }

  /**
   * 传递状态回调接口
   *
   * @param mSkidStatusCallBack
   */
  public void toolSendCallBack(MSkidStatusCallBack mSkidStatusCallBack) {
    this.mSkidStatusCallBack = mSkidStatusCallBack;
  }

  /**
   * XML映射完成
   */
  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    /*初始化控件*/
    toolInitView();
  }

  /**
   * 控件大小变化
   */
  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    mViewWidth = getMeasuredWidth();
    mClosePoint = new Point(mFrontViewG.getLeft(), mFrontViewG.getTop());
//    Log.e(TAG, "onSizeChanged: (mFrontViewG.getLeft(), mFrontViewG.getTop())"+ mFrontViewG.getLeft()+" "+mFrontViewG.getTop());
    mOpenPoint = new Point(mFrontViewG.getLeft() - mBackViewG.getWidth(), mFrontViewG.getTop());
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    setOnTouchListener(new OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        return false;
      }
    });

    toolCorrectionView();
  }

  /**
   * 调整控件位置
   */
  private void toolCorrectionView() {
    if (mBackViewG != null) {
      mBackViewG.offsetLeftAndRight((int) (mViewWidth - mBackViewG.getMeasuredWidth()));
    }
  }

  /**
   * 滑动处理
   */
  @Override
  public void computeScroll() {
    if (mViewDragHelper != null && mViewDragHelper.continueSettling(true)) {
      invalidate();
    }
  }

  /**
   * 初始化控件
   */
  private void toolInitView() {
    if (getChildCount() >= 2) {
      if (getChildAt(0) instanceof ViewGroup
          && getChildAt(1) instanceof ViewGroup) {
        mBackViewG = (ViewGroup) findViewById(R.id.LinearLayout_MSlideDeleteView_back);
        mFrontViewG = (ViewGroup) findViewById(R.id.LinearLayout_MSlideDeleteView_prospect);
      }
    }
  }

  /**
   * 初始化对象
   */
  private void toolInitObj() {
    mViewDragHelper = ViewDragHelper.create(this, 1f, new ViewDragHelper.Callback() {
      @Override
      public boolean tryCaptureView(View child, int pointerId) {
        Log.e("滑动页", "捕获滑动视图时");
        return child == mFrontViewG;
      }


      @Override
      public void onViewDragStateChanged(int state) {
        Log.e("滑动页", "滑动视图拖动状态改变" + state);
      }


      @Override
      public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        Log.e("滑动页", "滑动视图位置改变" + "left:" + left
            + "top:" + top
            + "dx:" + dx
            + "dy:" + dy);
      }


      @Override
      public void onViewCaptured(View capturedChild, int activePointerId) {
        Log.e("滑动页", "onViewCaptured" + activePointerId);
      }


      @Override
      public void onViewReleased(View releasedChild, float xvel, float yvel) {
        Log.e("滑动页", "不再主动拖动时" + "xvel:" + xvel + "yvel:" + yvel);
      }

      @Override
      public int getViewHorizontalDragRange(View child) {
        return 1;//为什么返回1，我改为0后也没变化
      }

      @Override
      public int getViewVerticalDragRange(View child) {
        return 0;
      }



      @Override
      public int clampViewPositionHorizontal(View child, int left, int dx) {
        Log.e(TAG, "clampViewPositionHorizontal: 滑动页" +"横向拖拽" + "left:" + left + "dx:");
        Log.e(TAG, "clampViewPositionHorizontal: 视图的宽"+mBackViewG.getMeasuredWidth() );
        if (left <= -mBackViewG.getMeasuredWidth()) {
          mDefaultStatus = SkidStatus.Open;
          return -mBackViewG.getMeasuredWidth();
        }
        if (left > 0) {
          mDefaultStatus = SkidStatus.Close;
          return 0;
        }
        return left;

      }

      @Override
      public int clampViewPositionVertical(View child, int top, int dy) {
        return 0;
      }
    });
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {

    return super.dispatchTouchEvent(ev);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent e) {
    return mViewDragHelper.shouldInterceptTouchEvent(e);
  }

  @Override
  public boolean onTouchEvent(MotionEvent e) {
    switch (e.getAction()) {
      case MotionEvent.ACTION_DOWN:
        Log.e("测试滑动-layout", "onTouchEvent_down");
        mIsSlid=true;
        break;
      case MotionEvent.ACTION_MOVE:
        Log.e("测试滑动-layout", "onTouchEvent_move");
        break;
      case MotionEvent.ACTION_UP:
        Log.e("测试滑动-layout", "onTouchEvent_up");
//        mIsSlid=false;//自己添加的代码
        break;
      case MotionEvent.ACTION_CANCEL:
        Log.e("测试滑动-layout", "onTouchEvent_cancel");
        mIsSlid=false;
        break;
      default:
        break;
    }
    if(mIsSlid){
      mViewDragHelper.processTouchEvent(e);//这个方法是干嘛的
      Log.e("测试滑动侧滑-layout", "onTouchEvent_Help");
    }
//    if(mIsSlid){
//      mViewDragHelper.processTouchEvent(e);//这个方法是干嘛的
//      Log.e("测试滑动侧滑-layout", "onTouchEvent_Help");
//    }
//    return true;
        return mIsSlid;
  }

  /**
   * 开启滑动
   */
  public void toolSendSkidKey(boolean mKey) {
    mIsSlid = mKey;
  }

  /**
   * 滑动事件传递
   */
  public void toolSlide(MotionEvent motionEvent) {

    if (mViewDragHelper != null) {

    }
  }

  /**
   * 开启
   */
  public void toolOpen() {
    if (mFrontViewG != null) {
      if (mViewDragHelper != null) {
        mViewDragHelper.settleCapturedViewAt(mOpenPoint.x, mOpenPoint.y);
        invalidate();
      }
    }
  }

  /**
   * 关闭
   */
  public void toolClos() {
    if (mFrontViewG != null) {
      if (mViewDragHelper != null) {
        mViewDragHelper.settleCapturedViewAt(mClosePoint.x, mClosePoint.y);
        invalidate();
      }
    }
  }
}
