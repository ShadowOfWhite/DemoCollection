package com.example.asus.searchingfilmdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.asus.searchingfilmdemo.bean.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.content.ContentValues.TAG;

/**
 * Created by 杨淋 on 2018/6/28.
 * Describe：
 */

public class MovieAdapter extends RecyclerView.Adapter implements Handler.Callback {

    public ArrayList<Movie> data;
    private Context activity;
    private Handler mHandler;
    private MovieViewHolder holder;


    public MovieAdapter(Context macticty, ArrayList<Movie> list) {
        this.activity = macticty;
        this.data = list;
        mHandler = new Handler(this);

    }

    @Override
    public boolean handleMessage(Message message) {
        Log.e("UI线程", "准备刷新");
        if (message.what == 0) {
            Log.e("UI线程", "刷新前");
            if (message.obj != null) {

                LoadImageViewModel model = (LoadImageViewModel) message.obj;
                Log.e("UI线程", "刷新前" + model.bitmap.getWidth());
                model.toolSetImageView();
            }
        }
        return true;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public MovieViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.movieIcon);
            textView = view.findViewById(R.id.movieDirector);
        }

//        public MovieViewHolder(View itemView) {
//            super(itemView);
//        }
//
//        public TextView getTextView() {
//            return textView;
//        }
//
//        public void setTextView(TextView textView) {
//            this.textView = textView;
//        }
//
//        public ImageView getImageView() {
//            return imageView;
//        }
//
//        public void setImageView(ImageView imageView) {
//            this.imageView = imageView;
//        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movienews_item, null);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (data != null && !data.isEmpty()) {
            Movie movie = data.get(position);
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            Log.e("适配器", "图片路径:" + movie.getData().get(position).getIconaddress()
                    + "评分：" + movie.getData().get(position).getGrade());
            if (movie.getData().get(position).getGrade() != null) {
                if (!movie.getData().get(position).getGrade().contains("null")) {
                    movieViewHolder.textView.setText("评分:" + movie.getData().get(position).getGrade());
                }
            } else {
                movieViewHolder.textView.setText("评分:无");
            }
            try {
                if (data != null && !data.isEmpty()) {

                    getBitmap(movie.getData().get(position).getIconaddress(), movieViewHolder);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        Log.e("recycler子项数量：", data.size() + "");
        return data.size();

    }

    public void getBitmap(final String path, final MovieViewHolder movieViewHolder) throws IOException {
      //  if(holder!=null){
     //   }
        OkHttpClient client = new OkHttpClient();
        //获取请求对象
        Request request = new Request.Builder().url(path).build();
        //获取响应体
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                //获取流
                InputStream in = body.byteStream();
                //转化为bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                Message message = new Message();
                LoadImageViewModel model = new LoadImageViewModel();
                model.setBitmap(bitmap);
//                model.setImageView(movieViewHolder.getImageView());
                holder=movieViewHolder;
                model.setImageView( holder.imageView);
                model.setPath(path);
                message.what = 0;
                message.obj = model;
                if (mHandler != null) {
                    mHandler.sendMessage(message);
                }

            }
        });


    }

    public class LoadImageViewModel implements Serializable {
        private ImageView imageView;
        private Bitmap bitmap;
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public ImageView getImageView() {
            return imageView;
        }

        public void setImageView(ImageView imageView) {
            this.imageView = imageView;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public void toolSetImageView() {
            if (imageView != null && bitmap != null) {
                Log.e(TAG, "onResponse: 开始加载图片" );
                Glide.with(activity).load(path).into(imageView);
            }
        }
    }
}
