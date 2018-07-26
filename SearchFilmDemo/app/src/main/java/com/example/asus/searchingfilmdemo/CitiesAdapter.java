package com.example.asus.searchingfilmdemo;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨淋 on 2018/6/27.
 * Describe：
 */

public class CitiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int HEAD = 0;
    private final int WORD = 1;
    private final int CITY = 2;
    ArrayList<AllCity> datas;
    Activity activity;

    public CitiesAdapter(Activity activity, CityData cityData) {
        Log.e("ceshi:", "CitiesAdapter");
        this.activity = activity;
        datas = cityData.getDatas();
    }

    public static class HeadViewHolder extends RecyclerView.ViewHolder {
        public HeadViewHolder(View view) {

            super(view);
            Log.e("ceshi:", "HeadViewHolder");
        }
    }

    public static class WordViewHolder extends RecyclerView.ViewHolder {
        TextView textWord;

        public WordViewHolder(View view) {
            super(view);
            textWord = (TextView) view.findViewById(R.id.textWord);
            Log.e("ceshi:", "WordViewHolder");
        }
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {

        TextView textCity;

        public CityViewHolder(View view) {
            super(view);
            textCity = (TextView) view.findViewById(R.id.textCity);
            Log.e("ceshi:", "CityViewHolder");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.e("ceshi:", "onCreateViewHolder" + viewType + ",,,,,,,,,");

        if (viewType == HEAD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_index_select_city_top, parent, false);
            HeadViewHolder headViewHolder = new HeadViewHolder(view);
            return headViewHolder;
        } else if (viewType == WORD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_gradute_institution_word, parent, false);
            WordViewHolder wordViewHolder = new WordViewHolder(view);
            return wordViewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commen_textview, parent, false);
            final CityViewHolder cityViewHolder = new CityViewHolder(view);
            cityViewHolder.textCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("chooseCity", cityViewHolder.textCity.getText());
                    activity.setResult(Activity.RESULT_OK, intent);
                    activity.finish();
                }
            });
            return cityViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.e("ceshi:", "onBindViewHolder" + position + "////////");

        if (position == 0) {
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
//            initTopViewHolder(topViewHolder);
        } else {
            int count = 0;
            for (int i = 0; i < datas.size(); i++) {
                count += 1;
                if (position == count) {
                    WordViewHolder wordViewHolder = (WordViewHolder) holder;
                    wordViewHolder.textWord.setText(datas.get(i).getAlifName());
                } else {
                    List<CityBean> addressList = datas.get(i).getAddressList();
                    for (int j = 0; j < addressList.size(); j++) {
                        count += 1;
                        if (position == count) {
                            final CityBean addressListBean = addressList.get(j);
                            CityViewHolder schoolViewHolder = (CityViewHolder) holder;
                            schoolViewHolder.textCity.setText(addressListBean.getName());
                            schoolViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(v.getContext(), "点击了", Toast.LENGTH_SHORT);
                                    Log.e("点击测试", v.toString());
//                                    EventMessage message = new EventMessage();
//                                    message.setType(24);
//                                    message.setData(addressListBean.getName());
//                                    EventBus.getDefault().post(message);
//                                    ((Activity) context).finish();
                                }
                            });
                        }
                    }
                }
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        Log.e("ceshi:", "getItemViewType" + position + "...");
        int count = 0;
        if (position == 0) {
            return HEAD;
        }
        for (int i = 0; i < datas.size(); i++) {
            count++;
            if (position == count) {
                return WORD;
            }
            List<CityBean> addressList = datas.get(i).getAddressList();
            for (int j = 0; j < addressList.size(); j++) {
                count++;
                if (position == count) {
                    return CITY;
                }
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {

        if (datas == null)
            return 1;
        int childCount = datas.size();
        for (int i = 0; i < datas.size(); i++) {
            childCount += datas.get(i).getAddressList().size();
        }
        Log.e("ceshi:", "getItemCount" + (childCount + 1) + "*******");
        return childCount + 1;

    }
}
