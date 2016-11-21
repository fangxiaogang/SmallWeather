package com.example.xiaogang.smallweather.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.xiaogang.smallweather.R;
import com.example.xiaogang.smallweather.net.GetData;
import com.example.xiaogang.smallweather.util.SharedPreferenceUtil;
import com.example.xiaogang.smallweather.util.Util;
import com.example.xiaogang.smallweather.model.Weather;
import com.example.xiaogang.smallweather.adapter.WeatherAdapter;
import com.example.xiaogang.smallweather.model.WeatherBean;

import rx.Subscriber;

public class MainActivity extends CheckPermissionsActivity
        implements NavigationView.OnNavigationItemSelectedListener,AMapLocationListener {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private WeatherAdapter weatherAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Weather weather = new Weather();

    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawer();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        weatherAdapter = new WeatherAdapter(MainActivity.this,weather);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiprefresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        init();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("action.refreshcity");
        registerReceiver(mRefreshBroadcastReceiver, intentFilter);


    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("action.refreshcity"))
            {
                swipeRefreshLayout.setRefreshing(true);
                getZhihuData();
            }
        }
    };

    private void init() {
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
//                swipeRefreshLayout.setRefreshing(true);
//                getZhihuData();
                Amaplocation();
            }
        },100);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        getZhihuData();


                    }
                },500);

            }
        });




    }

    private void initDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void getZhihuData(){
        Subscriber<WeatherBean> subscriber  = new Subscriber<WeatherBean>() {
            @Override
            public void onCompleted() {
                recyclerView.setAdapter(weatherAdapter);
                 //weatherAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("222222");
            }

            @Override
            public void onNext(WeatherBean weatherBean) {
                weather.status = weatherBean.mHeWeather.get(0).status;
//              weather.aqi = weatherBean.mHeWeather.get(0).aqi;   有的城市没有这一项数据返回
                weather.basic = weatherBean.mHeWeather.get(0).basic;
                weather.suggestion = weatherBean.mHeWeather.get(0).suggestion;
                weather.now = weatherBean.mHeWeather.get(0).now;
                weather.dailyForecast = weatherBean.mHeWeather.get(0).dailyForecast;
                weather.hourlyForecast = weatherBean.mHeWeather.get(0).hourlyForecast;
                getSupportActionBar().setTitle(weatherBean.mHeWeather.get(0).basic.city);
                weatherAdapter.notifyDataSetChanged();
                System.out.println(weatherBean.mHeWeather.get(0).now.cond.txt+"wendu"+weatherBean.mHeWeather.get(0).now.tmp);
                System.out.println(weather.basic.city);
                System.out.println(weather.now.tmp);
                System.out.println(weatherBean.mHeWeather.get(0).dailyForecast.get(0).tmp.max+"xiao"+
                        weatherBean.mHeWeather.get(0).dailyForecast.get(0).tmp.min);
            }
        };
        Object city = SharedPreferenceUtil.getData(MainActivity.this,"city","深圳");
        System.out.println(city+"要加载的城市");
        GetData.getInstance().getmeizidata(subscriber,String.valueOf(city));




    }


    private void Amaplocation(){
        swipeRefreshLayout.setRefreshing(true);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
//        mLocationOption.setInterval(2000);  不采用
        //获取一次定位结果：//该方法默认为false。
        mLocationOption.setOnceLocation(true);
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                SharedPreferenceUtil.saveData(MainActivity.this,"city", Util.replaceCity(aMapLocation.getCity()));
                String loctioncity =   aMapLocation.getCity();//城市信息
                System.out.println(loctioncity+"自动定位");
                Toast.makeText(MainActivity.this,"城市自动定位为"+aMapLocation.getCity(),Toast.LENGTH_SHORT).show();
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
            getZhihuData();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        System.out.println("onback");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
      
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_choicecity) {
            Intent intent = new Intent(MainActivity.this, ChoiceCityActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_addcity) {
            Toast.makeText(MainActivity.this,"writing",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            Toast.makeText(MainActivity.this,"writing",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("MainActivity onDestroy");
        unregisterReceiver(mRefreshBroadcastReceiver);
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }
    private long exitTime = 0;


}
