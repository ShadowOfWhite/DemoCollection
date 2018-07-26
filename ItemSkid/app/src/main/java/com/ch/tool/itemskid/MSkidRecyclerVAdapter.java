package com.ch.tool.itemskid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 今夜犬吠 on 2018/7/9.
 * 侧滑-适配器
 */

public class MSkidRecyclerVAdapter extends RecyclerView.Adapter<MSkidRecyclerVAdapter.MViewHolder> {

  /*数据集*/private List<MSkidPoetryBean> mSkidPoetryBeanList;

  /*上下文*/private Context mContext;

  public MSkidRecyclerVAdapter(Context mContext, List<MSkidPoetryBean> mSkidPoetryBeanList) {
    this.mContext = mContext;
    this.mSkidPoetryBeanList = mSkidPoetryBeanList;
  }

  @Override
  public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View mView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_skid, parent, false);
    MViewHolder mViewHolder = new MViewHolder(mView);
    return mViewHolder;
  }

  @Override
  public void onBindViewHolder(MViewHolder holder, int position) {
    holder.mAuthorTextV.setText(mSkidPoetryBeanList != null ? mSkidPoetryBeanList.get(position)
        .getmAuthor() : "无名");
    holder.mPoetryTextV.setText(mSkidPoetryBeanList != null ? mSkidPoetryBeanList.get(position)
        .getmPoetry() : "无词");
    holder.mDynasty.setText(mSkidPoetryBeanList != null ? mSkidPoetryBeanList.get(position)
        .getmDynasty() : "宋");
  }

  @Override
  public int getItemCount() {
    return mSkidPoetryBeanList != null ? mSkidPoetryBeanList.size() : 0;
  }


  class MViewHolder extends RecyclerView.ViewHolder {

    TextView mAuthorTextV;
    TextView mPoetryTextV;
    TextView mDynasty;

    public MViewHolder(View itemView) {
      super(itemView);
      mAuthorTextV = (TextView) itemView.findViewById(R.id.TextView_MSlideDeleteView_Author);
      mPoetryTextV = (TextView) itemView.findViewById(R.id.TextView_MSlideDeleteView_mPoetry);
      mDynasty = (TextView) itemView.findViewById(R.id.TextView_MSlideDeleteView_Dynasty);

    }
  }
}
