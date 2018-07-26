package com.example.asus.searchingfilmdemo.bean;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 杨淋 on 2018/6/28.
 * Describe：
 */

public class Movie implements Serializable {
    public ArrayList<Film> data;

    public ArrayList<Film> getData() {
        return data;
    }

    public class Film{

        public String getGrade() {
            return grade;
        }

        public String getIconaddress() {
            return iconaddress;
        }

        public String grade;
        public String iconaddress;
    }

}
