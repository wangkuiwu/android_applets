package com.skw.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

/**
 * WebView测试程序
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class MainActivity extends Activity
    implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        findViewById(R.id.btn_demo1).setOnClickListener(this);
        findViewById(R.id.btn_demo2).setOnClickListener(this);
        findViewById(R.id.btn_demo3).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.btn_demo1:
            startActivity(new Intent(this, Demo1.class));
            break;
        case R.id.btn_demo2:
            startActivity(new Intent(this, Demo2.class));
            break;
        case R.id.btn_demo3:
            startActivity(new Intent(this, Demo3.class));
            break;
        }
    }
}
