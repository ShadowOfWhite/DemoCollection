package com.example.asus.searchingfilmdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

public class CityChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_choose);
        initView();
    }

    private void initView() {

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        Gson gson = new Gson();
        CityData cityData = gson.fromJson(Data.citiesJson,CityData.class);
        CitiesAdapter adapter = new CitiesAdapter(CityChooseActivity.this,cityData);
        recyclerView.setAdapter(adapter);


    }
}
