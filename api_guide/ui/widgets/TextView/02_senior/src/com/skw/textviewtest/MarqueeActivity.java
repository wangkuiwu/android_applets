package com.skw.textviewtest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * 跑马灯测试程序
 */
public class MarqueeActivity extends Activity {
    private static final String TAG = "##skywang-MarqueeActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee);
    }
}
