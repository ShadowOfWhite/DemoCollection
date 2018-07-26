package com.ch.tool.itemskid;

/**
 * Created by 今夜犬吠 on 2018/7/10.
 * 滑动删除状态回调
 */

public interface MSkidStatusCallBack {

  /**
   * 已打开
   */
  void toolOpened();

  /**
   * 打开中
   */
  void toolOpening();

  /**
   * 关闭
   */
  void toolClose();
}
