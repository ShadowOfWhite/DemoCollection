package com.example.asus.broadcastdemo;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.List;

/*登陆活动不放在活动管理器里面，这样当点击了登出后，就会自动回到起始界面*/
public class MainActivity extends BaseActivity {

    Button logoutButton;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    List<String>  list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        logoutButton = findViewById(R.id.Button_Logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.asus.broadcastDemo.offlone");
                sendBroadcast(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter();
    }

    private void initData() {
    }
}
