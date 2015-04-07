package com.skw.probartest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.util.Log;

public class ProBarTest extends Activity 
    implements View.OnClickListener {
    private static final String TAG="SKYWANG";

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
            startActivity(new Intent(this, ProgressBarDemo1.class));
            break;
        case R.id.btn_demo2:
            startActivity(new Intent(this, ProgressBarDemo2.class));
            break;
        case R.id.btn_demo3:
            startActivity(new Intent(this, ProgressBarDemo3.class));
            break;
        }
    }
}
