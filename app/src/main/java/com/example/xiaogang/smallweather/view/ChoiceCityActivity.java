package com.example.xiaogang.smallweather.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.xiaogang.smallweather.R;
import com.example.xiaogang.smallweather.model.City;
import com.example.xiaogang.smallweather.db.DBManager;
import com.example.xiaogang.smallweather.model.Province;
import com.example.xiaogang.smallweather.db.WeatherDB;
import com.example.xiaogang.smallweather.adapter.ChoiceAdapter;
import com.example.xiaogang.smallweather.util.SharedPreferenceUtil;
import com.example.xiaogang.smallweather.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ChoiceCityActivity extends AppCompatActivity {
    private RecyclerView recyclerview;
    private ArrayList<String> dataList = new ArrayList<>();
    private List<Province> provincesList = new ArrayList<>();
    private List<City> cityList;
    private ChoiceAdapter choiceAdapter;
    private DBManager dbManager;
    private Province selectedProvince;
    private   int LEVEL_PROVINCE = 1;
    private   int LEVEL_CITY = 2;
    private  int level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_city);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        dbManager = new DBManager(ChoiceCityActivity.this);
        dbManager.openDatabase();
        choiceAdapter = new ChoiceAdapter(ChoiceCityActivity.this,dataList);
        recyclerview.setAdapter(choiceAdapter);
        queryProvince();
        choiceAdapter.setOnItemClickListener(new ChoiceAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view) {
                if (level == LEVEL_PROVINCE){
                    int position = recyclerview.getChildAdapterPosition(view);
                    selectedProvince = provincesList.get(position);
                    recyclerview.smoothScrollToPosition(0);
                    System.out.println(selectedProvince.ProName);
                    queryCity();
                } else if (level == LEVEL_CITY){
                    int position2 = recyclerview.getChildAdapterPosition(view);
                    String city = Util.replaceCity(cityList.get(position2).CityName);
                    System.out.println(city);
                    SharedPreferenceUtil.saveData(ChoiceCityActivity.this,"city",city);

                    Object name = SharedPreferenceUtil.getData(ChoiceCityActivity.this,"city","深圳");

                    System.out.println(name+"耶耶");

                    Intent intent = new Intent();
                    intent.setAction("action.refreshcity");
                    sendBroadcast(intent);

                    finish();

                }

            }
        });

    }

    private void queryProvince() {
        getSupportActionBar().setTitle("选择省份");


        if (provincesList.isEmpty()) {
            provincesList.addAll(WeatherDB.loadProvinces(dbManager.getDatabase()));
        }
        dataList.clear();
        level = LEVEL_PROVINCE;
        System.out.println(provincesList.get(0).ProName+"222222"+provincesList.get(0).ProSort);
        for (int i = 0;i <provincesList.size();i++){
            dataList.add(provincesList.get(i).ProName);
        }
        recyclerview.smoothScrollToPosition(0);
        choiceAdapter.notifyDataSetChanged();



    }

    private void queryCity() {
                getSupportActionBar().setTitle("选择城市");
                dataList.clear();
                cityList = WeatherDB.loadCities(dbManager.getDatabase(), selectedProvince.ProSort);
                level = LEVEL_CITY;
                for (int i = 0;i <cityList.size();i++){
                dataList.add(cityList.get(i).CityName);
        }
        choiceAdapter.notifyDataSetChanged();
        recyclerview.smoothScrollToPosition(0);
    }



    @Override
    public void onBackPressed() {

        //super.onBackPressed();  http://www.eoeandroid.com/thread-275312-1-1.html 这里的坑
        if (level == LEVEL_PROVINCE) {
            finish();
        } else if(level == LEVEL_CITY){
            queryProvince();
//            recyclerview.smoothScrollToPosition(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.closeDatabase();
    }
}
