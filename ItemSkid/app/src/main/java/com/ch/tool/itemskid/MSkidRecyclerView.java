package com.ch.tool.itemskid;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ViewSwitcher;

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
  private int mMark = 0;

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    return super.dispatchTouchEvent(ev);
  }

  private static final String TAG = "MSkidRecyclerView";
  MotionEvent motionEvent;
  @Override
  public boolean onInterceptTouchEvent(MotionEvent e) {


    switch (e.getAction()) {
      case MotionEvent.ACTION_DOWN:
        motionEvent = e;
//        Log.e(TAG, "onInterceptTouchEvent: --------"+motionEvent.getAction() );
        mPressX = e.getX();
        mPressY = e.getY();
        Log.e(TAG, "onInterceptTouchEvent: X坐标："+mPressX+",Y坐标："+mPressY );
        int mItemPosition = getChildAdapterPosition(findChildViewUnder(e.getX(), e.getY()));//获取view视图在adapt中的position
        Log.e(TAG, "onInterceptTouchEvent: 点击了第"+mItemPosition+"个视图" );
        ViewHolder mViewHolder = findViewHolderForAdapterPosition(mItemPosition);
        if (mViewHolder != null && mViewHolder.itemView != null) {
          View mSlidView = mViewHolder.itemView;
//          Log.e(TAG, "onInterceptTouchEvent: "+(mSlidView == findChildViewUnder(e.getX(),e.getY())));
//          Log.e(TAG, "onInterceptTouchEvent: "+(findChildViewUnder(e.getX(),e.getY()) instanceof MSkidLayout) );
          if (mSlidView instanceof MSkidLayout) {
            mSkidLayout = (MSkidLayout) mSlidView;
          }
        }
        Log.e("测试滑动-recyclerview", "onInterceptTouchEvent_down");
        super.onInterceptTouchEvent(e);
        break;
      case MotionEvent.ACTION_MOVE:
//        ViewCompat.offsetLeftAndRight()
        Rect rect = new Rect();
        Log.e("测试滑动-recyclerview", "onInterceptTouchEvent_move");
        Log.e("滑动判断-recyclerview", "" + "Y:" + Math.abs(e.getY() - mPressY) +
            "X:" + Math.abs(e.getX() - mPressX)
            + "slop：" + mViewConfiguration.getScaledTouchSlop());


        if (Math.abs(e.getY() - mPressY) > mViewConfiguration.getScaledTouchSlop() && mMark == 0) {
          mMark = 1;
          if (mSkidLayout != null) {
            Log.e("侧滑事件传递", "静止滑动");

            mSkidLayout.toolSendSkidKey(false);
            return true;
          }
        } else if (Math.abs(e.getX() - mPressX) > mViewConfiguration.getScaledTouchSlop() && mMark == 0) {
          Log.e("侧滑事件传递", "允许滑动");
          mSkidLayout.toolSendSkidKey(true);
//          setLayoutManager(new LinearLayoutManager(context) {
//            @Override
//            public boolean canScrollVertically() {
//              return false;
//            }
//          });
          mMark = 2;
          return false;
        }
        Log.e("测试侧滑事件传递", "滑动"+mMark);
        break;
      case MotionEvent.ACTION_UP:
        Log.e("测试滑动-recyclerview", "onInterceptTouchEvent_up");
//        setLayoutManager(new LinearLayoutManager(context) {
//          @Override
//          public boolean canScrollVertically() {
//            return true;
//          }
//        });
        mMark = 0;
        break;
      case MotionEvent.ACTION_CANCEL:
        Log.e("测试滑动-recyclerview", "onInterceptTouchEvent_CANCEL");
        break;
      default:
        Log.e(TAG, "测试onInterceptTouchEvent: 其他motionEvent" );
        break;
    }
    if (mMark == 1) {
      return true;
    } else {
      return false;
//            return super.onInterceptTouchEvent(e);
    }

  }

  /**
   * 事件拦截
   *
   * @param e
   * @return
   */
  /**/Boolean flag = false;

  @Override
  public boolean onTouchEvent(MotionEvent e) {
    switch (e.getAction()) {
      case MotionEvent.ACTION_DOWN:
        Log.e("测试滑动-recyclerview", "onTouchEvent_down");
        break;
      case MotionEvent.ACTION_MOVE:
        Log.e("测试滑动-recyclerview", "onTouchEvent_move");
//        if (!flag){
//          Log.e(TAG, "测试onTouchEvent: 模拟按下"+ MotionEventCompat.getActionMasked(motionEvent)+","+motionEvent.getAction() );
//          e.setAction(MotionEvent.ACTION_DOWN);
//          super.onInterceptTouchEvent(e);
//          flag = true;
//        }
        break;
      case MotionEvent.ACTION_UP:
        Log.e("测试滑动-recyclerview", "onTouchEvent_up");
//        mMark = 1;
        mMark = 0;
        flag = false;
        break;
      case MotionEvent.ACTION_CANCEL:
        Log.e("测试滑动-recyclerview", "onTouchEvent_cancel");
        break;
      default:
        Log.e(TAG, "测试onTouchEvent: 其他motionEvent"+e.getAction() );
        break;
    }


    return super.onTouchEvent(e);
  }


}
