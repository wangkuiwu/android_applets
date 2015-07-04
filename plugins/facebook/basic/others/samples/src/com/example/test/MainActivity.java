package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

/**
 * Facebook SDK 测试程序
 */
public class MainActivity extends FragmentActivity 
    implements View.OnClickListener {
    private static final String TAG = "##skywang-Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate use Login Button");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.btn_demo1).setOnClickListener(this);
        findViewById(R.id.btn_demo2).setOnClickListener(this);
        findViewById(R.id.btn_demo3).setOnClickListener(this);
        findViewById(R.id.btn_demo4).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_demo1:
                startActivity(new Intent(this, Demo1.class));
                break;
            case R.id.btn_demo2:
                startActivity(new Intent(this, Demo2.class));
                break;
            case R.id.btn_demo3:
                startActivity(new Intent(this, Demo3.class));
                break;
            case R.id.btn_demo4:
                startActivity(new Intent(this, Demo4.class));
                break;
        }
    }
}
