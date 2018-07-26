package com.ch.tool.itemskid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by 今夜犬吠 on 2018/7/9.
 * 侧滑-适配器
 */

public class MSkidRecyclerVAdapter
    extends RecyclerView.Adapter<MSkidRecyclerVAdapter.MViewHolder> implements MSkidStatusCallBack {

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
  public void onBindViewHolder(final MViewHolder holder, final int position) {
    holder.mAuthorTextV.setText(mSkidPoetryBeanList != null ? mSkidPoetryBeanList.get(position)
        .getmAuthor() : "无名");
    holder.mPoetryTextV.setText(mSkidPoetryBeanList != null ? mSkidPoetryBeanList.get(position)
        .getmPoetry() : "无词");
    holder.mDynasty.setText(mSkidPoetryBeanList != null ? mSkidPoetryBeanList.get(position)
        .getmDynasty() : "宋");
    holder.mDeleteTex.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MSkidLayout mSkidLayout = (MSkidLayout)holder.itemView;
        if (mSkidLayout.mDefaultStatus == MSkidLayout.SkidStatus.Open){
          if (mItemClickCallBack != null) {
            mItemClickCallBack.delete(position);
          }
        }
      }
    });
    holder.mGoUpTextV.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        MSkidLayout mSkidLayout = (MSkidLayout)holder.itemView;
        if (mSkidLayout.mDefaultStatus == MSkidLayout.SkidStatus.Open){
          if (mItemClickCallBack != null) {
            mItemClickCallBack.goUp(position);
          }
        }
      }
    });
  }

  @Override
  public int getItemCount() {
    return mSkidPoetryBeanList != null ? mSkidPoetryBeanList.size() : 0;
  }

  /**
   * 已打开
   */
  @Override
  public void toolOpened() {

  }

  /**
   * 打开中
   */
  @Override
  public void toolOpening() {

  }

  /**
   * 关闭
   */
  @Override
  public void toolClose() {

  }


  class MViewHolder extends RecyclerView.ViewHolder {

    TextView mAuthorTextV;
    TextView mPoetryTextV;
    TextView mDeleteTex;
    TextView mDynasty;
    TextView mGoUpTextV;

    public MViewHolder(View itemView) {
      super(itemView);
      mAuthorTextV = (TextView) itemView.findViewById(R.id.TextView_MSlideDeleteView_Author);
      mPoetryTextV = (TextView) itemView.findViewById(R.id.TextView_MSlideDeleteView_mPoetry);
      mDynasty = (TextView) itemView.findViewById(R.id.TextView_MSlideDeleteView_Dynasty);
      mDeleteTex = (TextView) itemView.findViewById(R.id.TextView_MSlideDeleteView_delete);
      mGoUpTextV = (TextView) itemView.findViewById(R.id.TextView_MSlideDeleteView_goUp);
    }
  }

  /*Item点击回调接口*/private MItemClickCallBack mItemClickCallBack;

  /**
   * 传递接口
   */
  public void toolSendCallBack(MItemClickCallBack mItemClickCallBack) {
    this.mItemClickCallBack = mItemClickCallBack;
  }

  public interface MItemClickCallBack {
    void delete(int mPosition);

    void goUp(int mPosition);
  }
}
