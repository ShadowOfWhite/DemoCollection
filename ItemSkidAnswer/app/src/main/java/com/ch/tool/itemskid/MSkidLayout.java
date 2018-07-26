package com.ch.tool.itemskid;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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

/**
 * Created by 今夜犬吠 on 2018/7/9.
 * 侧滑Item
 */

public class MSkidLayout extends FrameLayout {

  /*背景View*/private ViewGroup mBackViewG;
  /*前景View*/private ViewGroup mFrontViewG;

  /*控件宽*/private float mViewWidth;

  /*关闭时-抽屉位置*/private Point mClosePoint;
  /*打开时-抽屉位置*/private Point mOpenPoint;
  /*ViewGroup滑动辅助工具*/private ViewDragHelper mViewDragHelper;

  /*滑动状态-传递接口*/private MSkidStatusCallBack mSkidStatusCallBack;

  /*滑过的距离*/private int mSlidingDistance;

  /*是否滑动*/private boolean mIsSlid = true;

  /*是否手势滑动*/private boolean mGestureSlip = true;

  /*滑动状态*/
  public enum SkidStatus {
    Open//打开
    , Close//关闭
    , Opening;//正在打开
  }

  /*滑动状态-默认*/public SkidStatus mDefaultStatus = SkidStatus.Close;

  /*滑动状态-上一次状态*/private SkidStatus mBeforeStatus = SkidStatus.Close;

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
    Log.e("回弹-开始", "getLeft:" + mFrontViewG.getLeft()
        + "getMeW:" + mBackViewG.getMeasuredWidth());
    mClosePoint = new Point(mFrontViewG.getLeft(), mFrontViewG.getTop());
    mOpenPoint = new Point(mFrontViewG.getLeft() - mBackViewG.getMeasuredWidth(), mFrontViewG.getTop());
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    toolCorrectionView();
  }

  /**
   * 调整控件位置
   */
  private void toolCorrectionView() {
    if (mBackViewG != null) {
      mBackViewG.offsetLeftAndRight((int) mViewWidth - mBackViewG.getMeasuredWidth());
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
    mViewDragHelper = ViewDragHelper.create(this, 0.2f, new ViewDragHelper.Callback() {
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
        if (mGestureSlip) {
          if (mBeforeStatus == SkidStatus.Open) {
            mSlidingDistance = -(left + mBackViewG.getMeasuredWidth());
          } else {
            mSlidingDistance = left;
          }

        }
        if (mDefaultStatus != SkidStatus.Opening) {
          if (mDefaultStatus == SkidStatus.Open) {
            mBeforeStatus = SkidStatus.Open;
          } else {
            mBeforeStatus = SkidStatus.Close;
          }
          mDefaultStatus = SkidStatus.Opening;
        }
        if (left <= -mBackViewG.getMeasuredWidth()) {
          mGestureSlip = true;
          if (mDefaultStatus != SkidStatus.Open) {
            mDefaultStatus = SkidStatus.Open;
            mBeforeStatus = mDefaultStatus;
          }
        }
        if (left >= 0) {
          mGestureSlip = true;
          if (mDefaultStatus != SkidStatus.Close) {
            mDefaultStatus = SkidStatus.Close;
            mBeforeStatus = mDefaultStatus;
          }
        }
      }


      @Override
      public void onViewCaptured(View capturedChild, int activePointerId) {
        Log.e("滑动页", "onViewCaptured" + activePointerId);
      }


      @Override
      public void onViewReleased(View releasedChild, float xvel, float yvel) {
        Log.e("滑动页", "不再主动拖动时" + "xvel:" + xvel + "yvel:" + yvel);
        mGestureSlip = false;
        if (mDefaultStatus == SkidStatus.Opening) {
          toolViewRebound();
        }
      }

      @Override
      public int getViewHorizontalDragRange(View child) {
        return 1;
      }

      @Override
      public int getViewVerticalDragRange(View child) {
        return 0;
      }

      @Override
      public int clampViewPositionHorizontal(View child, int left, int dx) {
        Log.e("滑动页", "横向拖拽" + "left:" + left + "dx:");
        if (!mGestureSlip) {
          mGestureSlip = true;
        }
        if (left <= -mBackViewG.getMeasuredWidth()) {
          mDefaultStatus = SkidStatus.Open;
          return -mBackViewG.getMeasuredWidth();
        }
        if (left >= 0) {
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
        Log.e("滑动-layout", "onTouchEvent_down");
        break;
      case MotionEvent.ACTION_MOVE:
        Log.e("滑动-layout", "onTouchEvent_move");
        break;
      case MotionEvent.ACTION_UP:
        Log.e("滑动-layout", "onTouchEvent_up");
        break;
      case MotionEvent.ACTION_CANCEL:
        Log.e("滑动-layout", "onTouchEvent_cancel");
        break;
      default:
        break;
    }
    mViewDragHelper.processTouchEvent(e);
    Log.e("滑动-layout", "onTouchEvent_Help");
    return true;
  }


  /**
   * 滑动事件传递
   */
  public void toolSlide(MotionEvent motionEvent) {

    if (mViewDragHelper != null) {

    }
  }

  /**
   * 获取抽屉状态
   *
   * @return
   */
  public boolean getmDefaultStatus() {
    return mDefaultStatus == SkidStatus.Close;
  }

  /**
   * 回弹处理
   */
  private void toolViewRebound() {
    Log.e("滑动页", "关闭动画" + "已滑过" + Math.abs(mSlidingDistance) + " 背景4分之一："
        + mBackViewG.getMeasuredWidth() / 4);
    if (mSlidingDistance <= 0) {
      if (Math.abs(mSlidingDistance) >= mBackViewG.getMeasuredWidth() / 4) {
        if (mBeforeStatus == SkidStatus.Close) {
          toolOpen();
        } else {
          toolClos();
        }
      } else {
        if (mBeforeStatus == SkidStatus.Open) {
          toolOpen();
        } else {
          toolClos();
        }
      }

    }
  }

  /**
   * 开启-内部使用
   */
  private void toolOpen() {
    if (mFrontViewG != null) {
      if (mViewDragHelper != null) {
        mViewDragHelper.settleCapturedViewAt(mOpenPoint.x, mOpenPoint.y);
        invalidate();
      }
    }
  }

  /**
   * 开启-外部使用
   */
  public void toolOpenOfExternal() {
    if (mFrontViewG != null) {
      mFrontViewG.offsetLeftAndRight(mOpenPoint.x - Math.abs(mSlidingDistance));
    }
  }

  /**
   * 关闭-内部使用
   */
  private void toolClos() {
    if (mFrontViewG != null) {
      if (mViewDragHelper != null) {
        mViewDragHelper.settleCapturedViewAt(mClosePoint.x, mClosePoint.y);
        invalidate();
      }
    }
  }

  /**/private int mSkidMark = 0;

  /**
   * 关闭-外部使用
   */
  public void toolCloseOfExternal() {

    Log.e("回弹", "getLeft:" + mFrontViewG.getLeft()
        + "getMeW:" + mBackViewG.getMeasuredWidth());
    if (mFrontViewG != null && mDefaultStatus != SkidStatus.Close) {
      ValueAnimator valueAnimator = ValueAnimator.ofInt(0, -mFrontViewG.getLeft());
      valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
          int mOff = (int) animation.getAnimatedValue();
          Log.e("回弹", "getLeft:" + mOff);
          mFrontViewG.offsetLeftAndRight(mOff - mSkidMark);
          mSkidMark = mOff;
        }
      });
      valueAnimator.setDuration(100);
      valueAnimator.start();
      valueAnimator.addListener(new AnimatorListenerAdapter() {
        /**
         * {@inheritDoc}
         *
         * @param animation
         */
        @Override
        public void onAnimationEnd(Animator animation) {
          mGestureSlip = true;
          if (mDefaultStatus != SkidStatus.Close) {
            mDefaultStatus = SkidStatus.Close;
            mSkidMark = 0;
            mBeforeStatus = mDefaultStatus;
          }
        }
      });
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
}
