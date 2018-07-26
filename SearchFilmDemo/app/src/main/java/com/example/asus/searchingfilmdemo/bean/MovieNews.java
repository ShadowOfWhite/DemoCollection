package com.example.asus.searchingfilmdemo.bean;

import java.io.Serializable;

/**
 * Created by 杨淋 on 2018/6/28.
 * Describe：
 */

public class MovieNews implements Serializable {

    public String reason;
    public MovieOfCity result;

    public String getReason() {
        return reason;
    }

    public MovieOfCity getResult() {
        return result;
    }

    public String getError_code() {
        return error_code;
    }

    public String error_code;

    @Override
    public String toString() {
        return reason+","+error_code+",";
    }
}
