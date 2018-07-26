package com.ch.tool.itemskid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MSkidRecyclerVAdapter.MItemClickCallBack {

  /*侧滑列表*/private MSkidRecyclerView mSkidRecyclerView;

  /*适配器*/private MSkidRecyclerVAdapter mSkidRecyclerVAdapter;

  /*数据集*/private List<MSkidPoetryBean> mSkidRecyclerViewList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    toolInitView();
    toolInitObj();
    toolSetMonitor();
    toolGetTestData();
  }


  /**
   * 初始化控件
   */
  private void toolInitView() {
    mSkidRecyclerView = (MSkidRecyclerView) findViewById(R.id.MSkidRecyclerView_MainActivity_skid);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
      @Override
      public boolean canScrollVertically() {
        return true;
      }
    };
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    mSkidRecyclerView.setLayoutManager(layoutManager);
    mSkidRecyclerView.addItemDecoration(new DividerItemDecoration(this, 1));
  }

  /**
   * 初始化对象
   */
  private void toolInitObj() {
    mSkidRecyclerViewList = new ArrayList<>();
    mSkidRecyclerVAdapter = new MSkidRecyclerVAdapter(this, mSkidRecyclerViewList);
    mSkidRecyclerVAdapter.toolSendCallBack(this);
    mSkidRecyclerView.setAdapter(mSkidRecyclerVAdapter);
    mSkidRecyclerView.setItemAnimator(new DefaultItemAnimator());
  }

  /**
   * 设置监听
   */
  private void toolSetMonitor() {
    mSkidRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

      @Override
      public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
      }

      @Override
      public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        /*上拉加载更多*/
        if (mSkidRecyclerViewList != null && mSkidRecyclerViewList.size() < 80) {
          if (((double) recyclerView.computeVerticalScrollExtent() + (double) recyclerView.computeVerticalScrollOffset())
              / (double) recyclerView.computeVerticalScrollRange() * 100 >= 80) {
            toolGetTestData();
          }
        }
      }
    });
  }

  /*作者*/private String mAuthor[] = {"李白", "杜甫", "柳三变"
      , "李商隐", "王勃", "张若虚"
      , "李清照", "李煜", "纳兰容若"
      , "苏东坡", "孟郊"};
  /*朝代*/private String mDynasty[] = {"唐", "唐", "北宋"
      , "晚唐", "初唐", "初唐"
      , "宋", "南唐", "清"
      , "宋", "唐"};
  /*诗文*/private String mPoetry[] = {"十步杀一人，千里不留行", "亲朋无一字，老病有孤舟"
      , "执手相看泪眼，竟无语凝噎", "此情可待成追忆，只是当时已惘然"
      , "落霞与孤鹜齐飞，秋水共长天一色"
      , "春江花朝秋月夜", "莫道不销魂，帘卷西风，人比黄花瘦"
      , "砌下落梅如雪乱,拂了一身还满"
      , "人生若只如初见，当时只道是寻常"
      , "多情应笑我，早生华发"
      , "慈母手中线，游子身上衣"};

  /**
   * 获取测试数据
   */
  private void toolGetTestData() {
    if (mSkidRecyclerViewList != null) {
      for (int i = 0; i < 11; i++) {
        MSkidPoetryBean mSkidPoetryBean = new MSkidPoetryBean();
        mSkidPoetryBean.setmAuthor(mAuthor[i]);
        mSkidPoetryBean.setmDynasty(mDynasty[i]);
        mSkidPoetryBean.setmPoetry(mPoetry[i]);
        mSkidRecyclerViewList.add(mSkidPoetryBean);
      }
      if (mSkidRecyclerVAdapter != null) {
        mSkidRecyclerVAdapter.notifyDataSetChanged();
      }
    }
  }

  /**
   * 添加菜单栏
   */
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_new, menu);
    return true;
  }

  /**
   * 菜单点击监听
   *
   * @param item
   * @return
   */
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_add:
        if (mSkidRecyclerViewList != null) {
          mSkidRecyclerViewList.add(2, new MSkidPoetryBean("老子", "道可道，非常道", "春秋"));
          if (mSkidRecyclerVAdapter != null) {
            mSkidRecyclerVAdapter.notifyItemInserted(2);
          }
        }
        break;
      case R.id.action_delete:
        if (mSkidRecyclerViewList != null
            && mSkidRecyclerViewList.size() > 2) {
          mSkidRecyclerViewList.remove(2);
          if (mSkidRecyclerVAdapter != null) {
            mSkidRecyclerVAdapter.notifyItemRemoved(2);
          }
        }
        break;
      default:
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * 删除
   * @param mPosition
   */
  @Override
  public void delete(int mPosition) {
    if (mSkidRecyclerViewList != null) {
      if (mSkidRecyclerViewList.size() > mPosition) {
        mSkidRecyclerViewList.remove(mPosition);
        if (mSkidRecyclerVAdapter != null) {
          mSkidRecyclerVAdapter.notifyDataSetChanged();
        }
      }
    }
  }

  /**
   * 置顶
   * @param mPosition
   */
  @Override
  public void goUp(int mPosition) {
    if (mSkidRecyclerViewList != null) {
      if (mSkidRecyclerViewList.size() > mPosition) {
        MSkidPoetryBean mSkidPoetryBean=mSkidRecyclerViewList.get(mPosition);
        mSkidRecyclerViewList.remove(mPosition);
        mSkidRecyclerViewList.add(0,mSkidPoetryBean);
        if (mSkidRecyclerVAdapter != null) {
          mSkidRecyclerVAdapter.notifyDataSetChanged();
          mSkidRecyclerView.smoothScrollToPosition(0);
        }
      }
    }
  }
}
