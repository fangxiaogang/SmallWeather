package com.example.xiaogang.smallweather.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.xiaogang.smallweather.R;

import java.lang.ref.WeakReference;

public class FirstActivity extends Activity {
    private SwitchHandler mHandler = new SwitchHandler(Looper.getMainLooper(), this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler.sendEmptyMessageDelayed(1, 1000);

    }
    class SwitchHandler extends Handler {
        private WeakReference<FirstActivity> mWeakReference;

        public SwitchHandler(Looper mLooper, FirstActivity activity) {
            super(mLooper);
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent i = new Intent(FirstActivity.this, MainActivity.class);
            FirstActivity.this.startActivity(i);
            //activity切换的淡入淡出效果
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            FirstActivity.this.finish();
        }
    }
}
