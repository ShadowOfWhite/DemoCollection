package com.example.asus.searchingfilmdemo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.asus.searchingfilmdemo.bean.Movie;
import com.example.asus.searchingfilmdemo.bean.MovieNews;
import com.example.asus.searchingfilmdemo.bean.MovieOfCity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements Handler.Callback {

    /*城市名*/ private TextView cityName;
    /**/ private MovieNews movieNews;
    /**/private RecyclerView movieRecyclerView;
    /**/ MovieAdapter movieAdapter;
    /*数据集合*/private ArrayList<Movie> movieArrayList;
    /*Handle*/private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolInitView();
        toolSetLinstener();
        toolInitObj();
        requestNetwork("上海");
    }

    /**
     * 网络请求-请求电影咨询
     */
    public void requestNetwork(String cityName) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://op.juhe.cn/onebox/movie/pmovie?key=bca9b2df7c48656a0064ef01db6b14dd&city=" + cityName)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ceshi........", "失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    String news = response.body().string();
                    Gson gson = new Gson();
                    movieNews = gson.fromJson(news, MovieNews.class);
                    if (movieArrayList != null) {
                        if (!movieArrayList.isEmpty()) {
                            movieArrayList.clear();
                        }
                        if (movieNews != null
                                && movieNews.getResult().getData() != null
                                && !movieNews.getResult().getData().isEmpty()) {
                            movieArrayList.addAll(movieNews.getResult().getData());
                            if (!movieArrayList.isEmpty()) {
                                if (mHandler != null) {
                                    mHandler.sendEmptyMessage(0);
                                }
                            }
                        }
                    }
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String chooseCity = data.getStringExtra("chooseCity");
                    cityName.setText(chooseCity);
                    requestNetwork(chooseCity);
                }
                break;
            default:
        }
    }

    /**
     * 初始化控件
     */
    private void toolInitView() {
        cityName = (TextView) findViewById(R.id.cityName);
        movieRecyclerView = (RecyclerView) findViewById(R.id.movieRecyclerView);
    }

    /**
     * 设置监听
     */
    private void toolSetLinstener() {
        /*城市切换监听*/
        cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CityChooseActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    /**
     * 初始化对象
     */
    private void toolInitObj() {
        mHandler = new Handler(this);
        movieArrayList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        movieRecyclerView.setLayoutManager(layoutManager);
        movieAdapter = new MovieAdapter(MainActivity.this, movieArrayList);
        movieRecyclerView.setAdapter(movieAdapter);
    }


    @Override
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 0:
                if (movieAdapter != null) {
                    movieAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
        return false;
    }
}
