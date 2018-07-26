package com.example.asus.searchingfilmdemo.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 杨淋 on 2018/6/28.
 * Describe：
 */

public class MovieOfCity implements Serializable{

    public String title;
    public String url;
    public ArrayList<Movie> data;
    public String date;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setData(ArrayList<Movie> data) {
        this.data = data;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<Movie> getData() {
        return data;
    }

    public String getDate() {
        return date;
    }
}
