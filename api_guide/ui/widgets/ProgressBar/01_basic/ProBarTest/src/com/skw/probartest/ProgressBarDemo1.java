package com.skw.probartest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.util.Log;

import java.lang.Runnable;

/**
 * ProgressBar示例程序
 *
 * @desc 每个200ms将进度条的进度增加2%
 *
 * @author skywang
 * @e-mail kuiwu-wang@163.com
 */
public class ProgressBarDemo1 extends Activity {
    private static final String TAG="SKYWANG";

    // 计数线程
    private CountThread mCountThread;
    // ProcessBar
    private ProgressBar mProgressBar;
    
    // 处理当ProcessBar的进度完成的情况
    private static final int MSG_PROGRESS_BAR_FULL = 1; 
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case MSG_PROGRESS_BAR_FULL: {
                Log.d(TAG, "make the ProgressBar Gone!");
                // 隐藏ProcessBar。
                mProgressBar.setVisibility(View.GONE);
                // 终止线程
                if (mCountThread != null)
                    mCountThread.interrupt();
                    break;
                }
            default:
                break;
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo01);

        mProgressBar = (ProgressBar) findViewById(R.id.pbar_def);
        
        // 开启计数线程
        mCountThread = new CountThread();
        mCountThread.start();
    }

    private class CountThread extends Thread {

        @Override
        public void run() {
            super.run();

            while (!isInterrupted()) {
                // 线程休眠200ms
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                int progress = mProgressBar.getProgress() + 2;
                int max = mProgressBar.getMax();

                Log.d(TAG, "progress : "+progress+" , max : "+max);
                if (progress < max)
                    mProgressBar.setProgress(progress);
                else {
                    mProgressBar.setProgress(max);
                    // 发送消息给handler，让它隐藏ProcessBar。
                    handler.sendEmptyMessage(MSG_PROGRESS_BAR_FULL);
                }
            }   
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 终止计数线程
        if (mCountThread != null)
            mCountThread.interrupt();
    }
}
