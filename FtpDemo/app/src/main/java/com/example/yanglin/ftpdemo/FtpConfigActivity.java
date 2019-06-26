package com.example.yanglin.ftpdemo;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class FtpConfigActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp_config);

        android.support.v7.app.ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null){
            supportActionBar.hide();
        }



    }

}
