package com.ch.tool.itemskid;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by 今夜犬吠 on 2018/7/9.
 * 侧滑的Recyclerview
 */

public class MSkidRecyclerView extends RecyclerView {

  /*侧滑控件-当前点击的Item*/private MSkidLayout mSkidLayout;

  /*View配置常量*/private ViewConfiguration mViewConfiguration;

  private Context context;

  public MSkidRecyclerView(Context context) {
    super(context);
    this.context = context;
    toolInitObj(context);
  }

  public MSkidRecyclerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
    toolInitObj(context);
  }

  public MSkidRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.context = context;
    toolInitObj(context);
  }


  /**
   * 初始化对象
   */
  private void toolInitObj(Context context) {
    mViewConfiguration = ViewConfiguration.get(context);
  }

  /*按下的X*/ private float mPressX = 0;
  /*按下的Y*/private float mPressY = 0;
  /*上一次点击的Item下标*/private int mBeforeItem;
  /*分发状态标识*/ private int mMark = 0;

  @Override
  public boolean onInterceptTouchEvent(MotionEvent e) {
    switch (e.getAction()) {
      case MotionEvent.ACTION_DOWN:
        mPressX = e.getX();
        mPressY = e.getY();
        int mItemPosition = getChildAdapterPosition(findChildViewUnder(e.getX(), e.getY()));
        ViewHolder mViewHolder = findViewHolderForAdapterPosition(mItemPosition);
        if (mViewHolder != null && mViewHolder.itemView != null) {
          View mSlidView = mViewHolder.itemView;
          if (mSlidView instanceof MSkidLayout) {
            if (mBeforeItem != mItemPosition) {
              if (mSkidLayout != null) {
                if (!mSkidLayout.getmDefaultStatus()) {
                  mSkidLayout.toolCloseOfExternal();
                }
              }
            }
            mBeforeItem = mItemPosition;
            mSkidLayout = (MSkidLayout) mSlidView;
          }
        }
        return super.onInterceptTouchEvent(e);
      case MotionEvent.ACTION_MOVE:
        Log.e("滑动-recyclerview", "onInterceptTouchEvent_move");
        Log.e("滑动判断-recyclerview", "" + "Y:" + Math.abs(e.getY() - mPressY) +
            "X:" + Math.abs(e.getX() - mPressX)
            + "slop：" + mViewConfiguration.getScaledTouchSlop());

        if (Math.abs(e.getY() - mPressY) > Math.abs(e.getX() - mPressX) && mMark == 0
            && mSkidLayout.getmDefaultStatus()) {
          mMark = 1;
          if (mSkidLayout != null) {
            Log.e("侧滑事件传递", "静止滑动");
            return true;
          }
        } else if (Math.abs(e.getX() - mPressX) >= Math.abs(e.getY() - mPressY) && mMark == 0) {
          Log.e("侧滑事件传递", "允许滑动");
          mMark = 0;
          return false;
        }
        Log.e("侧滑事件传递", "滑动");
        break;
      case MotionEvent.ACTION_UP:
        Log.e("滑动-recyclerview", "onInterceptTouchEvent_up");
        break;
      case MotionEvent.ACTION_CANCEL:
        Log.e("滑动-recyclerview", "onInterceptTouchEvent_CANCEL");
        break;
      default:
        break;
    }
    if (mMark == 1) {
      return true;
    } else {
      return false;
    }

  }

  @Override
  public boolean onTouchEvent(MotionEvent e) {
    switch (e.getAction()) {
      case MotionEvent.ACTION_UP:
        mMark = 0;
        break;
      default:
        break;
    }
    return super.onTouchEvent(e);
  }
}
