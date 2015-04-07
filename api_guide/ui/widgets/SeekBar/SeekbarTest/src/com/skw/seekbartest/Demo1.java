package com.skw.seekbartest;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * SeekBar的基本用法
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class Demo1 extends Activity {
    private static final String TAG = "SKYWANG";

    // 与“系统默认SeekBar”对应的TextView
    private TextView mTvDef;
    // “系统默认SeekBar”
    private SeekBar mSeekBarDef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo1);

        mTvDef = (TextView) findViewById(R.id.tv_def);
        mSeekBarDef = (SeekBar) findViewById(R.id.seekbar_def);
        mSeekBarDef.setOnSeekBarChangeListener(new MySeekBarChangeListener());
    }    

    private final class MySeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                boolean fromUser) {
            mTvDef.setText("Progress: "+String.valueOf(seekBar.getProgress()));
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
    }
}
