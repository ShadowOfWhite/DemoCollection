package com.ch.tool.itemskid;

import java.io.Serializable;

/**
 * Created by 今夜犬吠 on 2018/7/9.
 * 诗歌-实体
 */

public class MSkidPoetryBean implements Serializable{

  /*作者*/private String mAuthor;

  /*诗文*/private String mPoetry;

  /*朝代*/private String mDynasty;

  public MSkidPoetryBean() {
  }

  public MSkidPoetryBean(String mAuthor, String mPoetry, String mDynasty) {
    this.mAuthor = mAuthor;
    this.mPoetry = mPoetry;
    this.mDynasty = mDynasty;
  }

  public String getmAuthor() {
    return mAuthor;
  }

  public void setmAuthor(String mAuthor) {
    this.mAuthor = mAuthor;
  }

  public String getmPoetry() {
    return mPoetry;
  }

  public void setmPoetry(String mPoetry) {
    this.mPoetry = mPoetry;
  }

  public String getmDynasty() {
    return mDynasty;
  }

  public void setmDynasty(String mDynasty) {
    this.mDynasty = mDynasty;
  }
}
